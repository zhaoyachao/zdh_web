package com.zyc.zdh.job;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zyc.zdh.dao.QuartzJobMapper;
import com.zyc.zdh.dao.TaskLogsMapper;
import com.zyc.zdh.entity.QuartzJobInfo;
import com.zyc.zdh.entity.RetryJobInfo;
import com.zyc.zdh.entity.TaskLogs;
import com.zyc.zdh.quartz.QuartzManager2;
import com.zyc.zdh.service.ZdhLogsService;
import com.zyc.zdh.util.CommandUtils;
import com.zyc.zdh.util.DateUtil;
import com.zyc.zdh.util.SpringContext;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import java.net.URI;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class HdfsJob extends JobCommon {

    private static String jobType = "HDFS";

    public static void run(QuartzJobInfo quartzJobInfo, Boolean is_retry) {

        Thread td=Thread.currentThread();

        long threadId = td.getId();
        System.out.println("线程id:"+threadId);
        String task_logs_id = SnowflakeIdWorker.getInstance().nextId() + "";
        String tk=myid+"_"+threadId+"_"+task_logs_id;

        TaskLogsMapper taskLogsMapper = (TaskLogsMapper) SpringContext.getBean("taskLogsMapper");
        QuartzJobMapper quartzJobMapper = (QuartzJobMapper) SpringContext.getBean("quartzJobMapper");
        QuartzManager2 quartzManager2 = (QuartzManager2) SpringContext.getBean("quartzManager2");

        try{

            if (quartzJobInfo.getLast_status() != null &&
                    (quartzJobInfo.getLast_status().equals("etl") || quartzJobInfo.getLast_status().equals("wait_retry"))) {
                logger.info("[" + jobType + "] JOB ,当前任务正在处理中,任务状态:" + quartzJobInfo.getLast_status());
                //此处不做处理,单独的超时告警监控
                return ;
            }

            TaskLogs taskLogs = insertTaskLog(task_logs_id, quartzJobInfo, null, InstanceStatus.DISPATCH.getValue(), "5",td.getId()+"", taskLogsMapper);

            //重要标识-必不可少-查询日志时使用
            quartzJobInfo.setTask_log_id(task_logs_id);
            quartzJobMapper.updateTaskLogId(quartzJobInfo.getJob_id(),quartzJobInfo.getTask_log_id());

            logger.info("开始执行[" + jobType + "] JOB");
            insertLog(quartzJobInfo, "INFO", "开始执行[" + jobType + "] JOB");
            //debugInfo(quartzJobInfo);

            //检查任务状态,并初始化相应的初始值 比如执行日期
            boolean checks = checkStatus(jobType, quartzJobInfo, taskLogsMapper, quartzManager2);

            if (checks == false) return;

            //检查任务依赖
            boolean dep = checkDep(jobType, quartzJobInfo, taskLogsMapper);
            if (dep == false) return;

            updateTaskLogEtlDate(taskLogs, quartzJobInfo, taskLogsMapper);

            if (quartzJobInfo.getJob_model().equals(JobModel.TIME_SEQ.getValue())) {
                runTimeSeq(quartzJobInfo, task_logs_id);
            } else if (quartzJobInfo.getJob_model().equals(JobModel.ONCE.getValue())) {
                runOnce(quartzJobInfo, task_logs_id);
            } else if (quartzJobInfo.getJob_model().equals(JobModel.REPEAT.getValue())) {
                runRepeat(quartzJobInfo, task_logs_id);
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            JobCommon.chm.remove(tk);
        }



    }

    private static Boolean hdfsCommand(QuartzJobInfo quartzJobInfo) {
        Boolean exe_status = true;
        //执行命令
        try {
            //当前只支持检查文件是否存在 if [ ! -f "/data/filename" ];then echo "文件不存在"; else echo "true"; fi
            //日期替换zdh.date => yyyy-MM-dd 模式
            //日期替换zdh.date.nodash=> yyyyMMdd 模式
            logger.info("目前支持日期参数3种模式:zdh.date => yyyy-MM-dd ,zdh.date.nodash=> yyyyMMdd " +
                    ",zdh.date.time=> yyyy-MM-dd HH:mm:ss");
            insertLog(quartzJobInfo, "info", "目前支持日期参数3种模式:zdh.date => yyyy-MM-dd ,zdh.date.nodash=> yyyyMMdd " +
                    ",zdh.date.time=> yyyy-MM-dd HH:mm:ss");
            if (quartzJobInfo.getLast_time() == null) {
                //第一次执行,下次执行时间为起始时间+1
                if (quartzJobInfo.getStart_time() == null) {
                    logger.info("[" + jobType + "] JOB ,开始日期为空设置当前日期为开始日期");
                    insertLog(quartzJobInfo, "info", "[" + jobType + "] JOB ,开始日期为空设置当前日期为开始日期");
                    quartzJobInfo.setStart_time(new Timestamp(new Date().getTime()));
                }
                logger.info("上次执行日期,下次执行日期均为空,赋值为:" + quartzJobInfo.getStart_time());
                insertLog(quartzJobInfo, "info", "上次执行日期,下次执行日期均为空,赋值为:" + quartzJobInfo.getStart_time());
                quartzJobInfo.setLast_time(quartzJobInfo.getStart_time());
                quartzJobInfo.setNext_time(quartzJobInfo.getStart_time());
            }
            //hdfs 调用参数配置,调度其他参数
            String params = quartzJobInfo.getParams().trim();

            JSONObject json = new JSONObject();
            if (!params.equals("")) {
                logger.info("[" + jobType + "]" + " JOB ,参数不为空判断是否有url 参数");
                json = JSON.parseObject(params);
            }

            if (!quartzJobInfo.getCommand().trim().equals("")) {
                logger.info("========+++++" + quartzJobInfo.getLast_time());
                String date_nodash = DateUtil.formatNodash(quartzJobInfo.getLast_time());
                String date_time = DateUtil.formatTime(quartzJobInfo.getLast_time());
                String date = DateUtil.format(quartzJobInfo.getLast_time());

                logger.info("[" + jobType + "] JOB ,COMMAND:" + quartzJobInfo.getCommand());
                insertLog(quartzJobInfo, "info", "[" + jobType + "] JOB ,COMMAND:" + quartzJobInfo.getCommand());
                String result = "fail";
                if (quartzJobInfo.getCommand().trim().equals("")) {
                    result = "success";
                } else {
                    Configuration conf = new Configuration();
                    String hadoop_user_name = "root";
                    String fs_defaultFS = "hdfs://localhost:40050";
                    if (json.containsKey("zdh.hadoop.user.name")) {
                        hadoop_user_name = json.getString("zdh.hadoop.user.name");
                    }
                    if (json.containsKey("zdh.fs.defaultFS")) {
                        fs_defaultFS = json.getString("zdh.fs.defaultFS");
                    }

                    Path path = new Path(quartzJobInfo.getCommand().
                            replace("zdh.date.nodash", date_nodash).
                            replace("zdh.date.time", date_time).
                            replace("zdh.date", date));

                    logger.info("[" + jobType + "] JOB ,开始连接hadoop,参数url:" + fs_defaultFS + ",用户:" + hadoop_user_name);
                    insertLog(quartzJobInfo, "info", "[" + jobType + "] JOB ,开始连接hadoop,参数url:" + fs_defaultFS + ",用户:" + hadoop_user_name);

                    FileSystem fs = FileSystem.get(new URI(fs_defaultFS), conf, hadoop_user_name);

                    String[] path_strs = quartzJobInfo.getCommand().
                            replace("zdh.date.nodash", date_nodash).
                            replace("zdh.date.time", date_time).
                            replace("zdh.date", date).split(",");
                    result = "success";
                    for (String path_str : path_strs) {
                        exe_status = fs.exists(new Path(path_str));
                        if (exe_status == false) {
                            result = "fail";
                            break;
                        }
                    }
                }
                logger.info("[" + jobType + "] JOB ,执行结果:" + result.trim());
                insertLog(quartzJobInfo, "info", "[" + jobType + "] JOB ,执行结果:" + result.trim());
                if (!result.trim().contains("success")) {
                    throw new Exception("文件不存在");
                }
            } else {
                logger.info("[" + jobType + "] JOB ,执行命令为空,默认返回成功状态");
                insertLog(quartzJobInfo, "info", "[" + jobType + "] JOB ,执行命令为空,默认返回成功状态");
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            exe_status = false;
        }
        return exe_status;
    }

    /**
     * 执行时间序列任务,会根据配置的起始时间和结束时间 按天执行
     *
     * @param quartzJobInfo
     */
    private static void runTimeSeq(QuartzJobInfo quartzJobInfo, String task_logs_id) {
        logger.info("[" + jobType + "] JOB,任务模式为[时间序列]");
        insertLog(quartzJobInfo, "info", "[" + jobType + "] JOB,任务模式为[时间序列]");
        QuartzManager2 quartzManager2 = (QuartzManager2) SpringContext.getBean("quartzManager2");
        QuartzJobMapper quartzJobMapper = (QuartzJobMapper) SpringContext.getBean("quartzJobMapper");
        ZdhLogsService zdhLogsService = (ZdhLogsService) SpringContext.getBean("zdhLogsServiceImpl");
        TaskLogsMapper taskLogsMapper = (TaskLogsMapper) SpringContext.getBean("taskLogsMapper");

        boolean end = isCount(jobType, quartzManager2, quartzJobInfo);
        if (end == true) {
            insertLog(quartzJobInfo, "info", "[" + jobType + "] JOB ,结束调度任务");
            return;
        }


        Boolean exe_status = false;
        exe_status = hdfsCommand(quartzJobInfo);
        if(!exe_status){
            jobFail(jobType,task_logs_id,quartzJobInfo,taskLogsMapper);
        }
        //拼接任务信息发送请求
        if (exe_status) {
            exe_status=JobCommon.runTimeSeq(jobType, task_logs_id, exe_status, quartzJobInfo, taskLogsMapper, quartzManager2);
        }
        //更新任务信息
        debugInfo(quartzJobInfo);

        quartzJobMapper.updateByPrimaryKey(quartzJobInfo);

    }


    /**
     * 执行一次任务
     *
     * @param quartzJobInfo
     */
    private static void runOnce(QuartzJobInfo quartzJobInfo, String task_logs_id) {

        QuartzManager2 quartzManager2 = (QuartzManager2) SpringContext.getBean("quartzManager2");
        QuartzJobMapper quartzJobMapper = (QuartzJobMapper) SpringContext.getBean("quartzJobMapper");
        ZdhLogsService zdhLogsService = (ZdhLogsService) SpringContext.getBean("zdhLogsServiceImpl");
        TaskLogsMapper taskLogsMapper = (TaskLogsMapper) SpringContext.getBean("taskLogsMapper");

        logger.info("[" + jobType + "] JOB,任务模式为[ONCE]");
        insertLog(quartzJobInfo, "info", "[" + jobType + "] JOB,任务模式为[ONCE]");
        boolean end = isCount(jobType, quartzManager2, quartzJobInfo);
        if (end == true) {
            insertLog(quartzJobInfo, "info", "[" + jobType + "] JOB ,结束调度任务");
            return;
        }

        Boolean exe_status = false;
        exe_status = hdfsCommand(quartzJobInfo);
        if(!exe_status){
            jobFail(jobType,task_logs_id,quartzJobInfo,taskLogsMapper);
        }
        //拼接任务信息发送请求
        if (exe_status) {
            exe_status=JobCommon.runOnce(jobType, task_logs_id, exe_status, quartzJobInfo, taskLogsMapper, quartzManager2);
        }

        //更新任务信息
        debugInfo(quartzJobInfo);
        quartzJobMapper.updateByPrimaryKey(quartzJobInfo);
    }


    /**
     * 重复执行
     *
     * @param quartzJobInfo
     */
    private static void runRepeat(QuartzJobInfo quartzJobInfo, String task_logs_id) {

        logger.info("[" + jobType + "] JOB,任务模式为[重复执行模式]");
        insertLog(quartzJobInfo, "info", "[" + jobType + "] JOB,任务模式为[重复执行模式]");

        QuartzManager2 quartzManager2 = (QuartzManager2) SpringContext.getBean("quartzManager2");
        QuartzJobMapper quartzJobMapper = (QuartzJobMapper) SpringContext.getBean("quartzJobMapper");
        ZdhLogsService zdhLogsService = (ZdhLogsService) SpringContext.getBean("zdhLogsServiceImpl");
        TaskLogsMapper taskLogsMapper = (TaskLogsMapper) SpringContext.getBean("taskLogsMapper");


        boolean end = isCount(jobType, quartzManager2, quartzJobInfo);

        if (end == true) {
            insertLog(quartzJobInfo, "info", "[" + jobType + "] JOB ,结束调度任务");
            return;
        }

        Boolean exe_status = false;
        exe_status = hdfsCommand(quartzJobInfo);
        if(!exe_status){
            jobFail(jobType,task_logs_id,quartzJobInfo,taskLogsMapper);
        }
        //拼接任务信息发送请求
        if (exe_status) {
            exe_status=JobCommon.runRepeat(jobType, task_logs_id, exe_status, quartzJobInfo, taskLogsMapper, quartzManager2);
        }

        //更新任务信息
        debugInfo(quartzJobInfo);

        //如果执行失败 next_time 时间不变,last_time 不变
        quartzJobMapper.updateByPrimaryKey(quartzJobInfo);


    }
}
