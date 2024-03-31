package com.zyc.zdh.job;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zyc.rqueue.RQueueClient;
import com.zyc.rqueue.RQueueManager;
import com.zyc.rqueue.RQueueMode;
import com.zyc.zdh.dao.StrategyGroupInstanceMapper;
import com.zyc.zdh.dao.StrategyInstanceMapper;
import com.zyc.zdh.entity.StrategyGroupInstance;
import com.zyc.zdh.entity.StrategyInstance;
import com.zyc.zdh.entity.ZdhDownloadInfo;
import com.zyc.zdh.entity.task_num_info;
import com.zyc.zdh.util.Const;
import com.zyc.zdh.util.DAG;
import com.zyc.zdh.util.DateUtil;
import com.zyc.zdh.util.SpringContext;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.util.*;

/**
 * 检查策略及策略组,判定上下游依赖
 */
public class CheckStrategyDepJob implements CheckDepJobInterface{

    private final static String task_log_status="etl";
    private static Logger logger = LoggerFactory.getLogger(CheckStrategyDepJob.class);

    public static List<ZdhDownloadInfo> zdhDownloadInfos = new ArrayList<>();

    @Override
    public void setObject(Object o) {

    }

    @Override
    public void run() {
        try {
            MDC.put("logId", UUID.randomUUID().toString());
            logger.debug("开始检测策略组任务...");
            StrategyGroupInstanceMapper sgim=(StrategyGroupInstanceMapper) SpringContext.getBean("strategyGroupInstanceMapper");
            StrategyInstanceMapper sim=(StrategyInstanceMapper) SpringContext.getBean("strategyInstanceMapper");


            // 如果当前任务组无子任务则直接设置完成,或者是在线任务组状态更新成执行中
            List<StrategyGroupInstance> non_group=sgim.selectTaskGroupByStatus(new String[]{JobStatus.SUB_TASK_DISPATCH.getValue(),JobStatus.KILL.getValue()});
            for(StrategyGroupInstance group :non_group){
                List<StrategyInstance> sub_task=sim.selectByGroupInstanceId(group.getId(), null);
                if(sub_task == null || sub_task.size() < 1){
                    if(group.getStatus().trim().equalsIgnoreCase(JobStatus.KILL.getValue())){
                        group.setStatus(JobStatus.KILLED.getValue());
                    }else{
                        group.setStatus(JobStatus.FINISH.getValue());
                    }
                    //group.setProcess("100");
                    JobDigitalMarket.updateTaskLog(group,sgim);
                    logger.info("当前策略组没有子任务可执行,当前任务组设为完成");
                    JobDigitalMarket.insertLog(group,"INFO","当前策略组没有子任务可执行,当前策略组设为完成");
                }
            }

            //获取可执行的任务组
            List<StrategyGroupInstance> sgis=sgim.selectTaskGroupByStatus(new String[]{JobStatus.CHECK_DEP.getValue(),JobStatus.CREATE.getValue()});
            // 此处可做任务并发限制,当前未限制并发
            for(StrategyGroupInstance sgi :sgis){
                String tmp_status=sgim.selectByPrimaryKey(sgi.getId()).getStatus();
                if( !tmp_status.equalsIgnoreCase("kill") && !tmp_status.equalsIgnoreCase("killed") ){
                    //在检查依赖时杀死任务--则不修改状态
                    updateTaskGroupLogInstanceStatus(sgi);
                }
            }

            //检查子任务是否可以运行
            run_sub_task();
            //检测任务组是否已经完成
            create_group_final_status();
        } catch (Exception e) {
             logger.error("类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}", e);
             MDC.remove("logId");
        }

    }

    /**
     * 修改组任务状态及子任务状态
     * @param sgi
     */
    public static void updateTaskGroupLogInstanceStatus(StrategyGroupInstance sgi){
        StrategyGroupInstanceMapper sgim=(StrategyGroupInstanceMapper) SpringContext.getBean("strategyGroupInstanceMapper");
        StrategyInstanceMapper sim=(StrategyInstanceMapper) SpringContext.getBean("strategyInstanceMapper");
        sgi.setStatus(JobStatus.SUB_TASK_DISPATCH.getValue());
        //sgi.setProcess("7.5");
        //sgi.setServer_id(JobCommon2.web_application_id);//重新设置调度器标识,retry任务会定期检查标识是否有效,对于组任务只有只有CREATE 状态检查此标识才有用
        //更新任务依赖时间
        //process_time_info pti=tgli.getProcess_time2();
        //pti.setCheck_dep_time(DateUtil.getCurrentTime());
        //tgli.setProcess_time(pti);

        JobDigitalMarket.updateTaskLog(sgi,sgim);
        debugInfo(sgi);
    }

    /**
     * 检查子任务是否可以运行
     */
    public static void run_sub_task() {
        try {
            logger.debug("开始检测子任务依赖...");
            StrategyGroupInstanceMapper sgim=(StrategyGroupInstanceMapper) SpringContext.getBean("strategyGroupInstanceMapper");
            StrategyInstanceMapper sim=(StrategyInstanceMapper) SpringContext.getBean("strategyInstanceMapper");

            //获取所有实时类型的可执行子任务
            List<StrategyInstance> strategyInstanceOnLineList=sim.selectThreadByStatus1(new String[] {JobStatus.CREATE.getValue(),JobStatus.CHECK_DEP.getValue()}, Const.STRATEGY_GROUP_TYPE_ONLINE);
            for(StrategyInstance tl :strategyInstanceOnLineList){
                tl.setStatus(JobStatus.ETL.getValue());
                tl.setUpdate_time(new Timestamp(System.currentTimeMillis()));
                JobDigitalMarket.updateTaskLog(tl,sim);
            }

            //获取所有离线类型的可执行的非依赖类型子任务
            List<StrategyInstance> strategyInstanceList=sim.selectThreadByStatus1(new String[] {JobStatus.CREATE.getValue(),JobStatus.CHECK_DEP.getValue()}, Const.STRATEGY_GROUP_TYPE_OFFLINE);
            for(StrategyInstance tl :strategyInstanceList){
                try{
                    //如果skip状态,跳过当前策略实例,理论上不会有skip状态,策略的跳过是通过is_disenable=true实现
                    if(tl.getStatus().equalsIgnoreCase(JobStatus.SKIP.getValue())){
                        continue;
                    }
                    //如果上游任务kill,killed 设置本实例为kill(设置kill状态后,由后端处理模块监听并修改为killed状态)
                    String pre_tasks=tl.getPre_tasks();
                    if(!StringUtils.isEmpty(pre_tasks)){
                        String[] task_ids=pre_tasks.split(",");
                        //获取上游 杀死,失败,杀死中的任务
                        List<StrategyInstance> tlis=sim.selectByIds(task_ids);

                        int level= Integer.valueOf(tl.getDepend_level());
                        if(tlis!=null && tlis.size()>0 && level==0){
                            // 此处判定级别0：成功时运行,1:杀死时运行,2:失败时运行,3: 上游执行完即可运行(不关心上游是否成功), 默认成功时运行
                            // 上游存在失败,更新当前实例状态为kill
                            tl.setStatus(JobStatus.KILL.getValue());
                            JobDigitalMarket.updateTaskLog(tl,sim);
                            JobDigitalMarket.insertLog(tl,"INFO","当前任务依赖级别: 上游全部成功时触发,检测到上游任务:"+tlis.get(0).getId()+",失败或者已被杀死,更新本任务状态为kill");
                            continue;
                        }
                        if(level >= 1 && level <3){
                            // 此处判定级别0：成功时运行,1:杀死时运行,2:失败时运行,3: 上游执行完即可运行(不关心上游是否成功) 默认成功时运行
                            //杀死触发,如果所有上游任务都以完成finish/skip
                            List<StrategyInstance> tlis_finish= sim.selectByFinishIds(task_ids);
                            if(tlis_finish.size()==task_ids.length){
                                tl.setStatus(JobStatus.CHECK_DEP_FINISH.getValue());
                                String run_jsmind_data = tl.getRun_jsmind_data();
                                JSONObject jsonObject = JSON.parseObject(run_jsmind_data);
                                jsonObject.put("is_disenable","true");
                                tl.setRun_jsmind_data(jsonObject.toJSONString());
                                tl.setIs_disenable("true");
                                JobDigitalMarket.updateTaskLog(tl, sim);
                                //JobCommon2.updateTaskLog(tl,taskLogInstanceMapper);
                                JobDigitalMarket.insertLog(tl,"INFO","当前任务依赖级别: 上游存在失败或者杀死时触发,检测到上游任务:"+pre_tasks+",都以完成或者跳过,更新本任务状态为check_dep_finish");
                                continue;
                            }
                        }
                    }

                    //根据dag判断是否对当前任务进行
                    DAG dag=new DAG();
                    String group_instance_id=tl.getGroup_instance_id();
                    List<StrategyInstance> strategyInstanceList2=sim.selectByGroupInstanceId(group_instance_id, null);
                    Map<String,StrategyInstance> dagStrategyInstance=new HashMap<>();
                    //此处必须使用group_instance_id实例id查询,因可能有策略实例已完成
                    for(StrategyInstance t2 :strategyInstanceList2){
                        if(t2.getGroup_instance_id().equalsIgnoreCase(group_instance_id)) {
                            dagStrategyInstance.put(t2.getId(), t2);
                            String pre_tasks2=t2.getPre_tasks();
                            if (!StringUtils.isEmpty(pre_tasks2)) {
                                String[] task_ids = pre_tasks2.split(",");
                                for (String instance_id:task_ids){
                                    dag.addEdge(instance_id, t2.getId());
                                }
                            }
                        }
                    }
                    Set<String> parents = dag.getParent(tl.getId());
                    if(parents==null || parents.size()==0){
                        //无父节点直接运行即可
                        System.out.println("根节点模拟发放任务--开始");
                        System.out.println("=======================");
                        System.out.println(JSON.toJSONString(tl));
                        if(tl.getStatus().equalsIgnoreCase(JobStatus.SKIP.getValue())){
                            continue;
                        }
                        JobDigitalMarket.insertLog(tl,"INFO","当前策略任务:"+tl.getId()+",推送类型:"+tl.getTouch_type());
                        if(tl.getTouch_type()==null || !tl.getTouch_type().equalsIgnoreCase("queue")){
                            resovleStrategyInstance(tl);
                        }
                        System.out.println("根节点模拟发放任务--结束");
                        //更新任务状态为检查完成
                        tl.setStatus(JobStatus.CHECK_DEP_FINISH.getValue());
                        JobDigitalMarket.updateTaskLog(tl,sim);
                    }else{
                        boolean is_run=true;
                        int success_num = 0;
                        int error_num = 0;
                        int run_num = 0;
                        int killed_num = 0;
                        for (String parent:parents){
                            if(dagStrategyInstance.containsKey(parent)){
                                String status = dagStrategyInstance.get(parent).getStatus();
                                if(status.equalsIgnoreCase(JobStatus.FINISH.getValue()) || status.equalsIgnoreCase(JobStatus.SKIP.getValue())){
                                    success_num = success_num + 1;
                                }else if(status.equalsIgnoreCase(JobStatus.ERROR.getValue())){
                                    error_num = error_num + 1;
                                }else if(status.equalsIgnoreCase(JobStatus.KILLED.getValue())){
                                    killed_num = killed_num + 1;
                                }else{
                                    run_num = run_num + 1;
                                }
                            }
                        }

                        //根据执行级别判断上游任务
                        int level= Integer.valueOf(tl.getDepend_level());
                        String msg = "";
                        if(level == 0){
                            msg = "当前任务依赖级别: 上游全部执行成功触发";
                            //0 成功触发, 上游必须都执行成功/跳过
                            if(success_num != parents.size()){
                                //当前不可执行
                                is_run=false;
                            }
                        }else if(level == 3){
                            msg = "当前任务依赖级别: 上游全部执行后(成功/失败/杀死)可触发";
                            //上游执行完,即可完成触发
                            if(run_num == 0 && (killed_num+success_num+error_num)==parents.size()){

                            }else{
                                is_run = false;
                            }
                        }else{
                            msg = "当前任务依赖级别: 上游存在失败,杀死任务可触发";
                            //1:杀死时运行,2:失败时运行
                            if(run_num == 0 && (killed_num > 0 || error_num >0) && (success_num+error_num+killed_num)==parents.size()){

                            }else{
                                is_run = false;
                            }
                        }

                        if(is_run){
                            //检查是否tn策略,tn策略动态判断当前时间是否可执行
                            if(tl.getInstance_type().equalsIgnoreCase("tn")){
                                is_run = JobDigitalMarket.checkTnDepends(tl, dagStrategyInstance);
                                if(is_run || tl.getIs_disenable().equalsIgnoreCase(Const.TRUR)){
                                    //可执行,或者跳过任务(策略的跳过-通过is_disenable实现),更新任务状态为完成
                                    tl.setStatus(JobStatus.CHECK_DEP_FINISH.getValue());
                                    JobDigitalMarket.updateTaskLog(tl,sim);
                                    JobDigitalMarket.insertLog(tl,"INFO",msg+",当前策略任务:"+tl.getId()+",检查完成:"+tl.getStatus());
                                    continue;
                                }else{
                                    tl.setStatus(JobStatus.CHECK_DEP.getValue());
                                    JobDigitalMarket.updateTaskLog(tl,sim);
                                    JobDigitalMarket.insertLog(tl,"INFO",msg+",当前策略任务:"+tl.getId()+",检查TN时间不满足,当前不可运行");
                                }
                            }
                        }

                        if(is_run){
                            //上游都以完成,可执行,任务发完执行集群 此处建议使用优先级队列 todo
                            System.out.println("模拟发放任务--开始");
                            JobDigitalMarket.insertLog(tl,"INFO","当前策略任务:"+tl.getId()+",推送类型:"+tl.getTouch_type());
                            if(tl.getStatus().equalsIgnoreCase(JobStatus.SKIP.getValue())){
                                continue;
                            }
                            if(tl.getTouch_type()==null || tl.getTouch_type().equalsIgnoreCase("queue")){
                                resovleStrategyInstance(tl);
                            }
                            System.out.println("=======================");
                            System.out.println(JSON.toJSONString(tl));
                            System.out.println("模拟发放任务--结束");

                            //更新任务状态为检查完成
                            tl.setStatus(JobStatus.CHECK_DEP_FINISH.getValue());
                            JobDigitalMarket.updateTaskLog(tl,sim);
                            JobDigitalMarket.insertLog(tl,"INFO",msg+",当前策略任务:"+tl.getId()+",检查完成:"+tl.getStatus());
                        }
                    }
                }catch (Exception e){
                    //任务未知异常,设置实例为失败
                    //更新任务状态为检查完成
                    tl.setStatus(JobStatus.ERROR.getValue());
                    JobDigitalMarket.updateTaskLog(tl,sim);
                    logger.error("类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}", e);
                }
            }

        } catch (Exception e) {
             logger.error("类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}", e);
        }

    }

    /**
     * 检测任务组是否已经完成,
     * 运行中+完成+失败=总数
     */
    public static void create_group_final_status(){
        StrategyGroupInstanceMapper sgim=(StrategyGroupInstanceMapper) SpringContext.getBean("strategyGroupInstanceMapper");
        StrategyInstanceMapper sim=(StrategyInstanceMapper) SpringContext.getBean("strategyInstanceMapper");
        List<StrategyGroupInstance> sgis=sgim.selectTaskGroupByStatus(new String[]{JobStatus.SUB_TASK_DISPATCH.getValue(),JobStatus.KILL.getValue()});

        for(StrategyGroupInstance sgi:sgis){

            //策略组实例到期自动结束
            if(sgi.getGroup_type().equalsIgnoreCase(Const.STRATEGY_GROUP_TYPE_ONLINE) && sgi.getStatus().equalsIgnoreCase(JobStatus.SUB_TASK_DISPATCH.getValue())){
                if(System.currentTimeMillis() > sgi.getEnd_time().getTime()){
                    sgim.updateStatusById3(JobStatus.FINISH.getValue(), DateUtil.getCurrentTime(), sgi.getId());
                    sim.updateStatusKillByGroupInstanceId(sgi.getId(), JobStatus.FINISH.getValue());
                }
            }

            //run_date 结构：run_date:[{task_log_instance_id,etl_task_id,etl_context,more_task}]
            //System.out.println(tgli.getRun_jsmind_data());
            if(StringUtils.isEmpty(sgi.getRun_jsmind_data())){
                continue;
            }
            JSONArray jary=JSON.parseObject(sgi.getRun_jsmind_data()).getJSONArray("run_data");
            List<String> tlidList=new ArrayList<>();
            for(Object obj:jary){
                String tlid=((JSONObject) obj).getString("strategy_instance_id");
                //System.out.println("task_log_instance_id:"+tlid);
                if(tlid!=null) {
                    tlidList.add(tlid);
                }
            }
            if (tlidList.size()<1) {
                continue;
            }

            List<task_num_info> lm=sim.selectStatusByIds(new String[]{sgi.getId()});
            int finish_num=0;
            int error_num=0;
            int kill_num=0;
            for(task_num_info tni:lm){
                if(tni.getStatus().equalsIgnoreCase(JobStatus.FINISH.getValue()) || tni.getStatus().equalsIgnoreCase(JobStatus.SKIP.getValue())){
                    finish_num=finish_num+tni.getNum();
                }
                if(tni.getStatus().equalsIgnoreCase(JobStatus.ERROR.getValue())){
                    error_num=tni.getNum();
                }
                if(tni.getStatus().equalsIgnoreCase(JobStatus.KILLED.getValue())){
                    kill_num=tni.getNum();
                }
            }

//            System.out.println("finish:"+finish_num);
//            System.out.println("kill_num:"+kill_num);
//            System.out.println("error_num:"+error_num);
            //如果 有运行状态，创建状态，杀死状态 则表示未运行完成
            //String process=((finish_num+error_num+kill_num)/tlidList.size())*100 > Double.valueOf(s.getProcess())? (((finish_num+error_num+kill_num)/tlidList.size())*100)+"":tgli.getProcess();
            //String msg="更新进度为:"+process;
            if(finish_num==tlidList.size()){
                //表示全部完成
                sgim.updateStatusById3(JobStatus.FINISH.getValue() ,DateUtil.getCurrentTime(),sgi.getId());
                //tglim.updateStatusById(JobStatus.FINISH.getValue(),tgli.getId());
                JobDigitalMarket.insertLog(sgi,"INFO","任务组已完成");
            }else if(kill_num==tlidList.size()){
                //表示组杀死
                sgim.updateStatusById3(JobStatus.KILLED.getValue() ,DateUtil.getCurrentTime(),sgi.getId());
               // tglim.updateStatusById(JobStatus.KILLED.getValue(),tgli.getId());
                JobDigitalMarket.insertLog(sgi,"INFO","任务组已杀死");
            }else if(finish_num+error_num == tlidList.size()){
                //存在失败
                sgim.updateStatusById3(JobStatus.ERROR.getValue() ,DateUtil.getCurrentTime(),sgi.getId());
                JobDigitalMarket.insertLog(sgi,"INFO","任务组以失败,具体信息请点击子任务查看");
            }else if(finish_num+error_num+kill_num == tlidList.size()){
                //存在杀死任务
                sgim.updateStatusById3(JobStatus.KILLED.getValue() ,DateUtil.getCurrentTime(),sgi.getId());
                JobDigitalMarket.insertLog(sgi,"INFO","任务组以完成,存在杀死任务,具体信息请点击子任务查看");
            }
        }

    }


    public static void resovleStrategyInstance(StrategyInstance strategyInstance) throws Exception {

        if(strategyInstance==null || StringUtils.isEmpty(strategyInstance.getInstance_type())){
            throw new Exception("策略实例及实例类型信息不可为空");
        }
        if(strategyInstance.getTouch_type()==null || !strategyInstance.getTouch_type().equalsIgnoreCase("queue")){

            return ;
        }

        RQueueClient<String> rQueueClient = RQueueManager.getRQueueClient(StrategyInstanceType.LABEL.getCode(), RQueueMode.PRIORITYQUEUE);

        String priority = strategyInstance.getPriority();
        if(StringUtils.isEmpty(priority)){
            priority="10";
        }
        rQueueClient.offer(JSON.toJSONString(strategyInstance), Integer.valueOf(priority));

    }

    public static void debugInfo(Object obj) {
        Field[] fields = obj.getClass().getDeclaredFields();
        for (int i = 0, len = fields.length; i < len; i++) {
            // 对于每个属性，获取属性名
            String varName = fields[i].getName();
            try {
                // 获取原来的访问控制权限
                boolean accessFlag = fields[i].isAccessible();
                // 修改访问控制权限
                fields[i].setAccessible(true);
                // 获取在对象f中属性fields[i]对应的对象中的变量
                Object o;
                try {
                    o = fields[i].get(obj);
                    logger.info("传入的对象中包含一个如下的变量：" + varName + " = " + o);
                } catch (IllegalAccessException e) {
                    // TODO Auto-generated catch block
                     logger.error("类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}", e);
                }
                // 恢复访问控制权限
                fields[i].setAccessible(accessFlag);
            } catch (IllegalArgumentException e) {
                 logger.error("类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}", e);
            }
        }
    }

}
