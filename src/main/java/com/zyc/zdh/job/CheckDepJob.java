package com.zyc.zdh.job;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zyc.zdh.dao.QuartzJobMapper;
import com.zyc.zdh.dao.TaskGroupLogInstanceMapper;
import com.zyc.zdh.dao.TaskLogInstanceMapper;
import com.zyc.zdh.dao.ZdhHaInfoMapper;
import com.zyc.zdh.entity.*;
import com.zyc.zdh.shiro.RedisUtil;
import com.zyc.zdh.util.*;
import jdk.nashorn.internal.scripts.JO;
import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.net.URI;
import java.sql.Timestamp;
import java.util.*;

//定期拉取重试任务
public class CheckDepJob {

    private final static String task_log_status="etl";
    private static Logger logger = LoggerFactory.getLogger(CheckDepJob.class);

    public static List<ZdhDownloadInfo> zdhDownloadInfos = new ArrayList<>();

    public static void run(QuartzJobInfo quartzJobInfo) {
        try {
            logger.info("开始检测任务组任务...");
            TaskGroupLogInstanceMapper tglim=(TaskGroupLogInstanceMapper) SpringContext.getBean("taskGroupLogInstanceMapper");
            TaskLogInstanceMapper tlim=(TaskLogInstanceMapper) SpringContext.getBean("taskLogInstanceMapper");
            //获取可执行的任务组
            List<TaskGroupLogInstance> tglims=tglim.selectTaskGroupByStatus(new String[]{JobStatus.CHECK_DEP.getValue(),JobStatus.CREATE.getValue()});
            for(TaskGroupLogInstance tgli :tglims){

                if(!StringUtils.isEmpty(tgli.getPre_tasks())){
                    int tmp_size=tglim.selectByIds(tgli.getPre_tasks().split(","),JobStatus.FINISH.getValue()).size();
                    if(tgli.getPre_tasks().split(",").length!=tmp_size){
                        continue;
                    }
                }

                String tmp_status=tglim.selectByPrimaryKey(tgli.getId()).getStatus();
                if( !tmp_status.equalsIgnoreCase("kill") && !tmp_status.equalsIgnoreCase("killed") ){
                    //在检查依赖时杀死任务--则不修改状态
                    updateTaskGroupLogInstanceStatus(tgli);
                }
//                if(JobCommon2.checkDep(tgli.getJob_type(),tgli)){
//                    String tmp_status=tglim.selectByPrimaryKey(tgli.getId()).getStatus();
//                    if( tmp_status=="kill" || tmp_status =="killed" ) continue; //在检查依赖时杀死任务
//
//                    updateTaskGroupLogInstanceStatus(tgli);
//
//                }else{
//                    //更新任务依赖时间
//                    process_time_info pti=tgli.getProcess_time2();
//                    pti.setCheck_dep_time(DateUtil.getCurrentTime());
//                    tgli.setProcess_time(pti);
//                    JobCommon2.updateTaskLog(tgli,tglim);
//                }
            }

            //检查子任务是否可以运行
            run2();
            //检测任务组是否已经完成
            run3();
        } catch (Exception e) {
            e.printStackTrace();
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
    public static void run2() {
        try {
            logger.info("开始检测子任务依赖...");
            TaskLogInstanceMapper taskLogInstanceMapper=(TaskLogInstanceMapper) SpringContext.getBean("taskLogInstanceMapper");
            //获取所有可执行的子任务
            List<TaskLogInstance> taskLogInstanceList=taskLogInstanceMapper.selectThreadByStatus1(new String[] {JobStatus.CREATE.getValue(),JobStatus.CHECK_DEP.getValue()});
            for(TaskLogInstance tl :taskLogInstanceList){
                //如果上游任务kill,killed 设置本实例为killed
                String pre_tasks=tl.getPre_tasks();
                if(!StringUtils.isEmpty(pre_tasks)){
                    String[] task_ids=pre_tasks.split(",");
                    List<TaskLogInstance> tlis=taskLogInstanceMapper.selectTliByIds(task_ids);
                    if(tlis!=null && tlis.size()>0){
                        tl.setStatus(JobStatus.KILLED.getValue());
                        JobCommon2.updateTaskLog(tl,taskLogInstanceMapper);
                        JobCommon2.insertLog(tl,"INFO","检测到上游任务:"+tlis.get(0).getId()+",失败或者已被杀死,更新本任务状态为killed");
                        continue;
                    }

                }
                boolean check=false;
                //检查组任务依赖
                if(tl.getJob_type().equalsIgnoreCase("group")){
                    //etl_task_id 代表任务的id(quartz_job_info的job_id)
                    String job_id=tl.getEtl_task_id();
                    String etl_date=tl.getEtl_date();
                    check=JobCommon2.checkDep2(tl.getJob_type(),tl);
                }else if(tl.getJob_type().equalsIgnoreCase("jdbc")){
                    // 检查jdbc 依赖
                    check=JobCommon2.checkDep3(tl.getJob_type(),tl);
                }else{
                    // 检查子任务依赖
                    check=JobCommon2.checkDep(tl.getJob_type(),tl);
                }

                if(tl.getStatus().equalsIgnoreCase(JobStatus.ERROR.getValue()) || tl.getStatus().equalsIgnoreCase(JobStatus.WAIT_RETRY.getValue())){
                    logger.info("检查依赖时发生异常,退出本次检查");
                    JobCommon2.insertLog(tl,"ERROR","检查依赖时发生异常,退出本次检查");
                    continue;
                }

                if(check){
                    String tmp_status=taskLogInstanceMapper.selectByPrimaryKey(tl.getId()).getStatus();
                    if( tmp_status=="kill" || tmp_status =="killed" ) continue; //在检查依赖时杀死任务
                    tl.setStatus(JobStatus.DISPATCH.getValue());
                    tl.setServer_id(JobCommon2.web_application_id);//重新设置调度器标识,retry任务会定期检查标识是否有效
                    //更新任务依赖时间
                    process_time_info pti=tl.getProcess_time2();
                    pti.setCheck_dep_time(DateUtil.getCurrentTime());
                    tl.setProcess_time(pti);

                    //debugInfo(tl);
                    if(!tl.getJob_type().equalsIgnoreCase("group") && !tl.getJob_type().equalsIgnoreCase("jdbc")){
                        JobCommon2.updateTaskLog(tl,taskLogInstanceMapper);
                        JobCommon2.chooseJobBean(tl);
                    }else{
                        //任务组依赖检查直接设置为已完成
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


            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 检测任务组是否已经完成,
     * 运行中+完成+失败=总数
     */
    public static void run3(){
        TaskGroupLogInstanceMapper tglim=(TaskGroupLogInstanceMapper) SpringContext.getBean("taskGroupLogInstanceMapper");
        TaskLogInstanceMapper tlim=(TaskLogInstanceMapper) SpringContext.getBean("taskLogInstanceMapper");
        List<TaskGroupLogInstance> tglis=tglim.selectTaskGroupByStatus(new String[]{JobStatus.SUB_TASK_DISPATCH.getValue(),JobStatus.KILL.getValue()});

        for(TaskGroupLogInstance tgli:tglis){
            //run_date 结构：run_date:[{task_log_instance_id,etl_task_id,etl_context,more_task}]
            System.out.println(tgli.getRun_jsmind_data());
            if(StringUtils.isEmpty(tgli.getRun_jsmind_data())){
                continue;
            }
            JSONArray jary=JSON.parseObject(tgli.getRun_jsmind_data()).getJSONArray("run_data");
            List<String> tlidList=new ArrayList<>();
            for(Object obj:jary){
                String tlid=((JSONObject) obj).getString("task_log_instance_id");
                System.out.println("task_log_instance_id:"+tlid);
                if(tlid!=null)
                  tlidList.add(tlid);
            }
            if (tlidList.size()<1)
                continue;

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

            System.out.println("finish:"+finish_num);
            System.out.println("kill_num:"+kill_num);
            System.out.println("error_num:"+error_num);
            //如果 有运行状态，创建状态，杀死状态 则表示未运行完成
            String process=(finish_num/tlidList.size())*100 > Double.valueOf(tgli.getProcess())? ((finish_num/tlidList.size())*100)+"":tgli.getProcess();
            String msg="更新进度为:"+process;
            if(finish_num==tlidList.size()){
                //表示全部完成
                tglim.updateStatusById3(JobStatus.FINISH.getValue(),process ,tgli.getId());
                //tglim.updateStatusById(JobStatus.FINISH.getValue(),tgli.getId());
                JobCommon2.insertLog(tgli,"INFO",msg);
            }else if(kill_num==tlidList.size()){
                //表示组杀死
                tglim.updateStatusById3(JobStatus.KILLED.getValue(),process ,tgli.getId());
               // tglim.updateStatusById(JobStatus.KILLED.getValue(),tgli.getId());
                JobCommon2.insertLog(tgli,"INFO",msg);
            }else if(finish_num+error_num == tlidList.size()){
                //存在失败
                tglim.updateStatusById3(JobStatus.ERROR.getValue(),process ,tgli.getId());
                JobCommon2.insertLog(tgli,"INFO",msg);
            }else if(finish_num+error_num+kill_num == tlidList.size()){
                //存在杀死任务
                tglim.updateStatusById3(JobStatus.KILLED.getValue(),process ,tgli.getId());
                JobCommon2.insertLog(tgli,"INFO",msg);
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
                    e.printStackTrace();
                }
                // 恢复访问控制权限
                fields[i].setAccessible(accessFlag);
            } catch (IllegalArgumentException ex) {
                ex.printStackTrace();
            }
        }
    }

}
