package com.zyc.zdh.job;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.zyc.zdh.dao.TaskGroupLogInstanceMapper;
import com.zyc.zdh.dao.TaskLogInstanceMapper;
import com.zyc.zdh.entity.*;
import com.zyc.zdh.shiro.RedisUtil;
import com.zyc.zdh.util.DAG;
import com.zyc.zdh.util.DateUtil;
import com.zyc.zdh.util.HttpUtil;
import com.zyc.zdh.util.SpringContext;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.NameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * 检查任务及任务组,判定上下游依赖
 */
public class CheckDepJob implements CheckDepJobInterface{

    private static Logger logger = LoggerFactory.getLogger(CheckDepJob.class);

    private Object object;

    private final static String task_log_status="etl";

    private final static List<String> checkJobType = Lists.newArrayList("GROUP","JDBC", "HDFS");


    public static void run(QuartzJobInfo quartzJobInfo) {
        try {
            MDC.put("logId", UUID.randomUUID().toString());
            logger.debug("开始检测任务组任务...");
            TaskGroupLogInstanceMapper tglim=(TaskGroupLogInstanceMapper) SpringContext.getBean("taskGroupLogInstanceMapper");
            TaskLogInstanceMapper tlim=(TaskLogInstanceMapper) SpringContext.getBean("taskLogInstanceMapper");

            // 如果当前任务组无子任务则直接设置完成
            List<TaskGroupLogInstance> non_group=tglim.selectTaskGroupByStatus(new String[]{JobStatus.SUB_TASK_DISPATCH.getValue(),JobStatus.KILL.getValue()});
            for(TaskGroupLogInstance group :non_group){
                List<TaskLogInstance> sub_task=tlim.selectByGroupId(group.getId());
                if(sub_task == null || sub_task.size() < 1){
                    if(group.getStatus().trim().equalsIgnoreCase(JobStatus.KILL.getValue())){
                        group.setStatus(JobStatus.KILLED.getValue());
                    }else{
                        group.setStatus(JobStatus.FINISH.getValue());
                    }
                    group.setProcess("100");
                    JobCommon2.updateTaskLog(group,tglim);
                    logger.info("当前任务组没有子任务可执行,当前任务组设为完成");
                    JobCommon2.insertLog(group,"INFO","当前任务组没有子任务可执行,当前任务组设为完成");
                }
            }


            //获取可执行的任务组,并根据优先级排序,数字大小和优先级高低成正比
            List<TaskGroupLogInstance> tglims=tglim.selectTaskGroupByStatus(new String[]{JobStatus.CHECK_DEP.getValue(),JobStatus.CREATE.getValue()});
            // 此处可做任务并发限制,当前未限制并发
            for(TaskGroupLogInstance tgli :tglims){

                if(!StringUtils.isEmpty(tgli.getPre_tasks())){
                    //如果上游任务组杀死,失败,则杀死本任务组
                    int killed_size=tglim.selectByIds(tgli.getPre_tasks().split(","),JobStatus.KILLED.getValue()).size();
                    int error_size=tglim.selectByIds(tgli.getPre_tasks().split(","),JobStatus.ERROR.getValue()).size();
                    if(killed_size>0 || error_size >0){
                        JobCommon2.insertLog(tgli,"INFO","上游依赖任务组存在杀死或者失败任务,将杀死当前任务组");
                        tgli.setStatus(JobStatus.KILLED.getValue());
                        tglim.updateByPrimaryKeySelective(tgli);
                        tlim.updateStatusByGroupId(tgli.getId());
                        JobCommon2.insertLog(tgli,"INFO","当前任务组以杀死");
                    }

                    int tmp_size=tglim.selectByIds(tgli.getPre_tasks().split(","),JobStatus.FINISH.getValue()).size();
                    if(tgli.getPre_tasks().split(",").length!=tmp_size){
                        //JobCommon2.insertLog(tgli,"INFO","上游依赖任务组未完成,稍后再次检测");
                        continue;
                    }
                }

                String tmp_status=tglim.selectByPrimaryKey(tgli.getId()).getStatus();
                if( !tmp_status.equalsIgnoreCase("kill") && !tmp_status.equalsIgnoreCase("killed") ){
                    //在检查依赖时杀死任务--则不修改状态
                    updateTaskGroupLogInstanceStatus(tgli);
                }

            }

            //检查子任务是否可以运行
            run_sub_task("");
            //检测任务组是否已经完成
            create_group_final_status();
            //检测flink任务是否已经完成
            check_flink_job_final_status();
        } catch (Exception e) {
            logger.error("类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}", e);
            MDC.remove("logId");
        }

    }

    /**
     * 修改组任务状态及子任务状态
     * @param tgli
     */
    public static void updateTaskGroupLogInstanceStatus(TaskGroupLogInstance tgli){
        TaskGroupLogInstanceMapper tglim=(TaskGroupLogInstanceMapper) SpringContext.getBean("taskGroupLogInstanceMapper");
        TaskLogInstanceMapper tlim=(TaskLogInstanceMapper) SpringContext.getBean("taskLogInstanceMapper");
        tgli.setStatus(JobStatus.SUB_TASK_DISPATCH.getValue());
        tgli.setProcess("7.5");
        tgli.setServer_id(JobCommon2.web_application_id);//重新设置调度器标识,retry任务会定期检查标识是否有效,对于组任务只有只有CREATE 状态检查此标识才有用
        //更新任务依赖时间
        process_time_info pti=tgli.getProcess_time2();
        pti.setCheck_dep_time(DateUtil.getCurrentTime());
        tgli.setProcess_time(pti);

        JobCommon2.updateTaskLog(tgli,tglim);
        debugInfo(tgli);

    }

    /**
     * 检查子任务是否可以运行
     */
    public static void run_sub_task(String scheduleId) {
        try {
            logger.debug("开始检测子任务依赖,scheduleId: {} ....",scheduleId);
            TaskGroupLogInstanceMapper tglim=(TaskGroupLogInstanceMapper) SpringContext.getBean("taskGroupLogInstanceMapper");
            TaskLogInstanceMapper taskLogInstanceMapper=(TaskLogInstanceMapper) SpringContext.getBean("taskLogInstanceMapper");
            RedisUtil redisUtil=(RedisUtil) SpringContext.getBean("redisUtil");
            //执行-检查JDBC等依赖类型的任务
            List<TaskLogInstance> dep_tlis=taskLogInstanceMapper.selectTaskByJobType(new String[] {JobStatus.DISPATCH.getValue(),JobStatus.CREATE.getValue(),
                    JobStatus.CHECK_DEP.getValue()},checkJobType.toArray(new String[]{}));
            for(TaskLogInstance tli :dep_tlis) {
                try{
                    //调度器不相等跳过
                    if(!tli.getSchedule_id().equalsIgnoreCase(scheduleId)){
                        if(StringUtils.isEmpty(scheduleId) && !StringUtils.isEmpty(tli.getSchedule_id())){
                            if(!redisUtil.exists("schedule_"+tli.getSchedule_id())){
                                logger.error("run sub task: {}, not found schedule: {}", tli.getId(), tli.getSchedule_id());
                                if((System.currentTimeMillis() - tli.getUpdate_time().getTime()) >= (10 * 60 *1000)){
                                    logger.info("run sub task: {}, not found schedule: {}, update status error", tli.getId(), tli.getSchedule_id());
                                    taskLogInstanceMapper.updateStatusById(JobStatus.ERROR.getValue(),DateUtil.getCurrentTime(), tli.getId());
                                }
                            }
                        }
                        continue;
                    }
                    if(tli.getStatus()!=null && tli.getStatus().equalsIgnoreCase("skip")){
                        continue;
                    }
                    String action = "";
                    //如果上游任务kill,killed 设置本实例为killed
                    String pre_tasks=tli.getPre_tasks();
                    action = checkLevel(tli, pre_tasks);

                    if(StringUtils.isEmpty(action)){
                        continue;
                    }
                    boolean check = false;
                    if (tli.getJob_type().equalsIgnoreCase("group")) {
                        //etl_task_id 代表任务的id(quartz_job_info的job_id)
                        String job_id = tli.getEtl_task_id();
                        String etl_date = tli.getEtl_date();
                        check = JobCommon2.checkDep_group(tli.getJob_type(), tli);
                    } else if (tli.getJob_type().equalsIgnoreCase("jdbc")) {
                        // 检查jdbc 依赖
                        check = JobCommon2.checkDep_jdbc(tli.getJob_type(), tli);
                    } else if(tli.getJob_type().equalsIgnoreCase("hdfs")) {
                        // 检查hdfs 依赖
                        check = JobCommon2.checkDep_hdfs(tli.getJob_type(), tli);
                    }
                    if (check) {
                        tli.setStatus(JobStatus.FINISH.getValue());
                        tli.setProcess("100");
                        JobCommon2.updateTaskLog(tli, taskLogInstanceMapper);
                    }else{
                        if( !tli.getStatus().equalsIgnoreCase(JobStatus.ERROR.getValue()) && !tli.getStatus().equalsIgnoreCase(JobStatus.WAIT_RETRY.getValue())){
                            tli.setStatus(JobStatus.DISPATCH.getValue());
                            JobCommon2.updateTaskStatus(JobStatus.DISPATCH.getValue(),tli.getId(),"",taskLogInstanceMapper);
                        }

                        //JobCommon2.updateTaskLog(tli, taskLogInstanceMapper);
                    }
                }catch (Exception e){
                    JobCommon2.updateTaskStatus(JobStatus.ERROR.getValue(),tli.getId(),"",taskLogInstanceMapper);
                    logger.error("类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}", e);
                }
            }


            //获取所有可执行的非依赖类型子任务(排除了group, jdbc,hdfs 类的任务)
            List<TaskLogInstance> taskLogInstanceList=taskLogInstanceMapper.selectThreadByStatus1(new String[] {JobStatus.CREATE.getValue(),JobStatus.CHECK_DEP.getValue()}, checkJobType.toArray(new String[]{}));
            for(TaskLogInstance tl :taskLogInstanceList){
                try{
                    if(!tl.getSchedule_id().equalsIgnoreCase(scheduleId)){
                        if(StringUtils.isEmpty(scheduleId) && !StringUtils.isEmpty(tl.getSchedule_id())){
                            if(!redisUtil.exists("schedule_"+tl.getSchedule_id())){
                                logger.error("run sub task: {}, not found schedule: {}", tl.getId(), tl.getSchedule_id());
                                if((System.currentTimeMillis() - tl.getUpdate_time().getTime()) >= (10 * 60 *1000)){
                                    logger.info("run sub task: {}, not found schedule: {}, update status error", tl.getId(), tl.getSchedule_id());
                                    taskLogInstanceMapper.updateStatusById(JobStatus.ERROR.getValue(),DateUtil.getCurrentTime(), tl.getId());
                                }
                            }
                        }
                        continue;
                    }
                    if(tl.getStatus()!=null && tl.getStatus().equalsIgnoreCase("skip")){
                        continue;
                    }
                    String action = "";
                    //如果上游任务kill,killed 设置本实例为killed
                    String pre_tasks=tl.getPre_tasks();
                    action = checkLevel(tl, pre_tasks);
//                    if(!StringUtils.isEmpty(pre_tasks)){
//                        String[] task_ids=pre_tasks.split(",");
//                        List<TaskLogInstance> tlis=taskLogInstanceMapper.selectTliByIds(task_ids);
//
//                        int level= Integer.valueOf(tl.getDepend_level());
//                        if(tlis!=null && tlis.size()>0 && level==0){
//                            // 此处判定级别0：成功时运行,1:杀死时运行,2:失败时运行,默认成功时运行
//                            tl.setStatus(JobStatus.KILLED.getValue());
//                            JobCommon2.updateTaskLog(tl,taskLogInstanceMapper);
//                            JobCommon2.insertLog(tl,"INFO","检测到上游任务:"+tlis.get(0).getId()+",失败或者已被杀死,更新本任务状态为killed");
//                            continue;
//                        }
//                        if(level >= 1){
//                            // 此处判定级别0：成功时运行,1:杀死时运行,2:失败时运行,默认成功时运行
//                            //杀死触发,如果所有上游任务都以完成
//                            List<TaskLogInstance> tlis_finish= taskLogInstanceMapper.selectByFinishIds(task_ids);
//                            if(tlis_finish.size()==task_ids.length){
//                                tl.setStatus(JobStatus.SKIP.getValue());
//                                JobCommon2.updateTaskStatus(JobStatus.SKIP.getValue(),tl.getId(),"100",taskLogInstanceMapper);
//                                //JobCommon2.updateTaskLog(tl,taskLogInstanceMapper);
//                                JobCommon2.insertLog(tl,"INFO","检测到上游任务:"+pre_tasks+",都以完成或者跳过,更新本任务状态为SKIP");
//                                continue;
//                            }
//                        }
//
//                    }

                    if(StringUtils.isEmpty(action)){
                        continue;
                    }
                    //根据dag判断是否对当前任务进行
                    DAG dag=new DAG();
                    TaskGroupLogInstance tgli=tglim.selectByPrimaryKey(tl.getGroup_id());
                    if(StringUtils.isEmpty(tgli.getRun_jsmind_data())){
                        JSONObject run_jsmind_data=JSON.parseObject(tgli.getRun_jsmind_data());
                        JSONArray run_lines=run_jsmind_data.getJSONArray("run_line");
                        for(Object run_line: run_lines){
                            String from=((JSONObject) run_line).getString("from");
                            String to=((JSONObject) run_line).getString("to");
                            dag.addEdge(from,to);
                        }
                    }

                    boolean check=false;
                    // 检查ETL任务依赖
                    check=JobCommon2.checkDep(tl.getJob_type(),tl);

                    if(tl.getStatus().equalsIgnoreCase(JobStatus.ERROR.getValue()) || tl.getStatus().equalsIgnoreCase(JobStatus.WAIT_RETRY.getValue())){
                        logger.info("检查依赖时发生异常,退出本次检查");
                        JobCommon2.insertLog(tl,"ERROR","检查依赖时发生异常,退出本次检查");
                        continue;
                    }

                    if(check){
                        String tmp_status=taskLogInstanceMapper.selectByPrimaryKey(tl.getId()).getStatus();
                        if( tmp_status.equalsIgnoreCase("kill") || tmp_status.equalsIgnoreCase("killed") ) {
                            continue; //在检查依赖时杀死任务
                        }

                        if(tl.getJob_type().equalsIgnoreCase("shell")){
                            if(JobCommon2.check_thread_limit(tl)){
                                //增加告警通知,每5分钟告警一次
                                continue;
                            }
                        }

                        //检查spark 任务是否超过限制
                        if(tl.getJob_type().equalsIgnoreCase("etl")){
                            if(JobCommon2.check_spark_limit(tl)){
                                continue ;
                            }
                        }

                        tl.setStatus(JobStatus.DISPATCH.getValue());
                        tl.setServer_id(JobCommon2.web_application_id);//重新设置调度器标识,retry任务会定期检查标识是否有效
                        //更新任务依赖时间
                        process_time_info pti=tl.getProcess_time2();
                        pti.setCheck_dep_time(DateUtil.getCurrentTime());
                        tl.setProcess_time(pti);

                        //group,jdbc,hdfs为同步检查类的任务,
                        if(!tl.getJob_type().equalsIgnoreCase("group") && !tl.getJob_type().equalsIgnoreCase("jdbc")
                                && !tl.getJob_type().equalsIgnoreCase("hdfs")){
                            JobCommon2.updateTaskLog(tl,taskLogInstanceMapper);
                            JobCommon2.chooseJobBean(tl);
                        }else{
                            //任务组依赖检查直接设置为已完成--理论上此处不会执行,如果执行,代表数据异常
                            tl.setStatus(JobStatus.FINISH.getValue());
                            tl.setProcess("100");
                            JobCommon2.updateTaskLog(tl,taskLogInstanceMapper);
                        }

                    }else{
                        //更新任务依赖时间
                        process_time_info pti=tl.getProcess_time2();
                        pti.setCheck_dep_time(DateUtil.getCurrentTime());
                        tl.setProcess_time(pti);
                        JobCommon2.updateTaskLog(tl,taskLogInstanceMapper);
                    }
                }catch (Exception e){
                    JobCommon2.updateTaskStatus(JobStatus.ERROR.getValue(),tl.getId(),"",taskLogInstanceMapper);
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
        TaskGroupLogInstanceMapper tglim=(TaskGroupLogInstanceMapper) SpringContext.getBean("taskGroupLogInstanceMapper");
        TaskLogInstanceMapper tlim=(TaskLogInstanceMapper) SpringContext.getBean("taskLogInstanceMapper");
        List<TaskGroupLogInstance> tglis=tglim.selectTaskGroupByStatus(new String[]{JobStatus.SUB_TASK_DISPATCH.getValue(),JobStatus.KILL.getValue()});

        for(TaskGroupLogInstance tgli:tglis){
            //run_date 结构：run_date:[{task_log_instance_id,etl_task_id,etl_context,more_task}]
            //System.out.println(tgli.getRun_jsmind_data());
            if(StringUtils.isEmpty(tgli.getRun_jsmind_data())){
                continue;
            }
            JSONArray jary=JSON.parseObject(tgli.getRun_jsmind_data()).getJSONArray("run_data");
            List<String> tlidList=new ArrayList<>();
            for(Object obj:jary){
                String tlid=((JSONObject) obj).getString("task_log_instance_id");
                //System.out.println("task_log_instance_id:"+tlid);
                if(tlid!=null) {
                    tlidList.add(tlid);
                }
            }
            if (tlidList.size()<1) {
                continue;
            }

            List<task_num_info> lm=tlim.selectByIds(tlidList.toArray(new String[]{}));
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
            String process=((finish_num+error_num+kill_num)/tlidList.size())*100 > Double.valueOf(tgli.getProcess())? (((finish_num+error_num+kill_num)/tlidList.size())*100)+"":tgli.getProcess();
            String msg="更新进度为:"+process;
            if(finish_num==tlidList.size()){
                //表示全部完成
                tglim.updateStatusById3(JobStatus.FINISH.getValue(),process ,DateUtil.getCurrentTime(),tgli.getId());
                //tglim.updateStatusById(JobStatus.FINISH.getValue(),tgli.getId());
                JobCommon2.insertLog(tgli,"INFO",msg);
                JobCommon2.insertLog(tgli,"INFO","任务组已完成");
            }else if(kill_num==tlidList.size()){
                //表示组杀死
                tglim.updateStatusById3(JobStatus.KILLED.getValue(),process ,DateUtil.getCurrentTime(),tgli.getId());
               // tglim.updateStatusById(JobStatus.KILLED.getValue(),tgli.getId());
                JobCommon2.insertLog(tgli,"INFO",msg);
                JobCommon2.insertLog(tgli,"INFO","任务组已杀死");
            }else if(finish_num+error_num == tlidList.size()){
                //存在失败
                tglim.updateStatusById3(JobStatus.ERROR.getValue(),process ,DateUtil.getCurrentTime(),tgli.getId());
                JobCommon2.insertLog(tgli,"INFO",msg);
                JobCommon2.insertLog(tgli,"INFO","任务组以失败,具体信息请点击子任务查看");
            }else if(finish_num+error_num+kill_num == tlidList.size()){
                //存在杀死任务
                tglim.updateStatusById3(JobStatus.KILLED.getValue(),process ,DateUtil.getCurrentTime(),tgli.getId());
                JobCommon2.insertLog(tgli,"INFO",msg);
                JobCommon2.insertLog(tgli,"INFO","任务组以完成,存在杀死任务,具体信息请点击子任务查看");
            }


        }

    }

    /**
     * 检测flink任务是否完成
     * flink任务不受应用控制
     */
    public static void check_flink_job_final_status(){
        TaskGroupLogInstanceMapper tglim=(TaskGroupLogInstanceMapper) SpringContext.getBean("taskGroupLogInstanceMapper");
        TaskLogInstanceMapper tlim=(TaskLogInstanceMapper) SpringContext.getBean("taskLogInstanceMapper");

        List<TaskLogInstance> tlis = tlim.selectByMoreTask(MoreTask.FLINK.getValue());
        for(TaskLogInstance tli : tlis){
            //根据application_id（flink job_id）获取正在运行的状态
            String flink_web_ui = tli.getHistory_server();
            String flink_job_id=tli.getApplication_id();
            List<NameValuePair> npl=new ArrayList<>();

            if(StringUtils.isEmpty(flink_web_ui) || StringUtils.isEmpty(flink_job_id)){
                continue;
            }

            //服务端未确认,不进行状态验证
            if(tli.getServer_ack().equalsIgnoreCase("0")){
                continue;
            }
            try {
                String result = HttpUtil.getRequest(flink_web_ui+"/jobs/"+flink_job_id, npl);
                JSONObject jsonObject= JSON.parseObject(result);
                String state = jsonObject.getString("state").toLowerCase();
                String status = "";
                String process = "100";
                switch (state){
                    case "finished":
                        status=JobStatus.FINISH.getValue();
                        break;
                    case "failed":
                        status=JobStatus.ERROR.getValue();
                        break;
                    case "canceled":
                        status=JobStatus.ERROR.getValue();
                        break;
                    case "running":
                        status=JobStatus.ETL.getValue();
                        process = "70";
                        break;
                    default:
                        status="";
                }
                if(!status.equalsIgnoreCase("")){
                    JobCommon2.insertLog(tli, "INFO", "WEB模块更新当前状态:"+status);
                    tlim.updateStatusById4(status,process,tli.getId());
                }
            } catch (Exception e) {
                 logger.error("类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}", e);
                //判定任务是否可重试,如果无法获取flink信息 则认为flink任务以死亡,尝试自动拉起
                JobCommon2.jobFail("ETL",tli);
                //tlim.updateStatusById4(JobStatus.ERROR.getValue(),"100",tli.getId());
            }

        }

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

    @Override
    public void setObject(Object o) {
        this.object = o;
    }

    @Override
    public void run() {
        run((QuartzJobInfo) object);
    }

    /**
     * 检查依赖级别
     * @param tli
     * @return 返回do表示可触发
     */
    private static String checkLevel(TaskLogInstance tli, String pre_tasks){
        String action = "";
        TaskGroupLogInstanceMapper tglim=(TaskGroupLogInstanceMapper) SpringContext.getBean("taskGroupLogInstanceMapper");
        TaskLogInstanceMapper taskLogInstanceMapper=(TaskLogInstanceMapper) SpringContext.getBean("taskLogInstanceMapper");
        if(StringUtils.isEmpty(pre_tasks)){
            action = "do";
            return action;
        }
        if(!StringUtils.isEmpty(pre_tasks)){
            String[] task_ids=pre_tasks.split(",");
            List<TaskLogInstance> pre_task_log_instances = taskLogInstanceMapper.selectByIds2(task_ids);

            //获取kill,killed,error 任务
            List<TaskLogInstance> tlis=taskLogInstanceMapper.selectTliByIds(task_ids);
            int level= Integer.valueOf(tli.getDepend_level());
            //tlis!=null && tlis.size()>0 &&
            if(level==0){
                // 此处判定级别0：成功时运行,1:杀死时运行,2:失败时运行,3:执行结束后运行(成功/失败/跳过),默认成功时运行
                if(checkByInStatus(pre_task_log_instances, Lists.newArrayList("kill", "killed", "error"))){
                    //包含失败,杀死,杀死中, 设置状态为以杀死
                    tli.setStatus(JobStatus.KILLED.getValue());
                    JobCommon2.updateTaskLog(tli,taskLogInstanceMapper);
                    JobCommon2.insertLog(tli,"INFO","检测到上游任务:"+tlis.get(0).getId()+",失败或者已被杀死,更新本任务状态为killed");
                    return action;
                }else if(checkByNotInStatus(pre_task_log_instances, Lists.newArrayList("finish", "skip"))){
                    //不包含失败,杀死,杀死中任务,但是存在成功,跳过之外状态的任务也跳过
                    return action;
                }else{
                    action = "do";
                }
            }
            if(level == 1){
                // 此处判定级别0：成功时运行,1:杀死时运行,2:失败时运行,3:执行结束后运行(成功/失败/跳过),默认成功时运行
                if(!checkByNotInStatus(pre_task_log_instances, Lists.newArrayList("finish", "skip"))){
                    //上游状态都是finish,skip, 则当前任务设为跳过
                    tli.setStatus(JobStatus.SKIP.getValue());
                    JobCommon2.updateTaskStatus(JobStatus.SKIP.getValue(),tli.getId(),"100",taskLogInstanceMapper);
                    //JobCommon2.updateTaskLog(tli,taskLogInstanceMapper);
                    JobCommon2.insertLog(tli,"INFO","检测到上游任务:"+pre_tasks+",都以完成或者跳过,更新本任务状态为SKIP");
                    return action;
                }

                if(checkByInStatus(pre_task_log_instances, Lists.newArrayList("killed"))){
                    //触发
                    action = "do";
                }else if(checkByInStatus(pre_task_log_instances, Lists.newArrayList("error"))){
                    tli.setStatus(JobStatus.KILLED.getValue());
                    JobCommon2.updateTaskStatus(JobStatus.KILLED.getValue(),tli.getId(),"100",taskLogInstanceMapper);
                    //JobCommon2.updateTaskLog(tli,taskLogInstanceMapper);
                    JobCommon2.insertLog(tli,"INFO","检测到上游任务:"+pre_tasks+",存在失败,更新本任务状态为KILLED");
                    return action;
                }else {
                    return action;
                }
            }

            if(level == 2){
                // 此处判定级别0：成功时运行,1:杀死时运行,2:失败时运行,3:执行结束后运行(成功/失败/跳过),默认成功时运行
                //任务只包含finish,skip
                if(!checkByNotInStatus(pre_task_log_instances, Lists.newArrayList("finish", "skip"))){
                    //上游状态都是finish,skip, 则当前任务设为跳过
                    tli.setStatus(JobStatus.SKIP.getValue());
                    JobCommon2.updateTaskStatus(JobStatus.SKIP.getValue(),tli.getId(),"100",taskLogInstanceMapper);
                    JobCommon2.insertLog(tli,"INFO","检测到上游任务:"+pre_tasks+",都以完成或者跳过,更新本任务状态为SKIP");
                    return action;
                }

                if(checkByInStatus(pre_task_log_instances, Lists.newArrayList("killed"))){
                    tli.setStatus(JobStatus.KILLED.getValue());
                    JobCommon2.updateTaskStatus(JobStatus.KILLED.getValue(),tli.getId(),"100",taskLogInstanceMapper);
                    JobCommon2.insertLog(tli,"INFO","检测到上游任务:"+pre_tasks+",存在已杀死任务,更新本任务状态为KILLED");
                    return action;
                }

                //包含error, 且不包含finish,skip,error之前的状态任务表示上游都以完成,可进行判断是否触发
                if(checkByInStatus(pre_task_log_instances, Lists.newArrayList("error")) &&
                        !checkByNotInStatus(pre_task_log_instances, Lists.newArrayList("finish", "skip", "error"))){
                    action = "do";
                }else {
                    return action;
                }
            }
//                        if(level == 1 && level <= 2){
//                            // 此处判定级别0：成功时运行,1:杀死时运行,2:失败时运行,3:执行结束后运行(成功/失败/跳过),默认成功时运行
//                            //杀死触发,如果所有上游任务都以完成
//                            List<TaskLogInstance> tlis_finish= taskLogInstanceMapper.selectByFinishIds(task_ids);
//                            if(tlis_finish.size()==task_ids.length){
//                                tli.setStatus(JobStatus.SKIP.getValue());
//                                JobCommon2.updateTaskStatus(JobStatus.SKIP.getValue(),tli.getId(),"100",taskLogInstanceMapper);
//                                //JobCommon2.updateTaskLog(tli,taskLogInstanceMapper);
//                                JobCommon2.insertLog(tli,"INFO","检测到上游任务:"+pre_tasks+",都以完成或者跳过,更新本任务状态为SKIP");
//                                continue;
//                            }
//                        }

            if(level == 3){
                // 此处判定级别0：成功时运行,1:杀死时运行,2:失败时运行,3:执行结束后运行(成功/失败/跳过),默认成功时运行
                //上游都执行结束后触发,上游任务状态只包含(完成,失败,跳过),则触发当前任务
                if(!checkByNotInStatus(pre_task_log_instances, Lists.newArrayList("finish","skip","error"))){
                    action = "do";
                }else if(checkByInStatus(pre_task_log_instances, Lists.newArrayList("killed"))){
                    tli.setStatus(JobStatus.KILLED.getValue());
                    JobCommon2.updateTaskStatus(JobStatus.KILLED.getValue(),tli.getId(),"100",taskLogInstanceMapper);
                    //JobCommon2.updateTaskLog(tli,taskLogInstanceMapper);
                    JobCommon2.insertLog(tli,"INFO","检测到上游任务:"+pre_tasks+",存在已杀死任务,更新本任务状态为KILLED");
                    return action;
                }else {
                    return action;
                }
            }
        }
        return action;
    }

    /**
     * 检查任务中是否存在指定状态的任务
     * 存在指定的状态返回true, 其他返回false
     * @param tlis
     * @param status
     */
    private static boolean checkByInStatus(List<TaskLogInstance> tlis, List<String> status){
        for (TaskLogInstance tli: tlis){
            if(status.contains(tli.getStatus())){
                return true;
            }
        }
        return false;
    }

    /**
     * 检查任务中是否存在指定状态之外的任务
     *
     * 存在指定状态之外的状态,返回true,其他返回false
     *
     * @param tlis
     * @param status
     */
    private static boolean checkByNotInStatus(List<TaskLogInstance> tlis, List<String> status){
        for (TaskLogInstance tli: tlis){
            if(!status.contains(tli.getStatus())){
                return true;
            }
        }
        return false;
    }
}
