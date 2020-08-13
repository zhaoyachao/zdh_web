package com.zyc.zdh.job;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zyc.zdh.dao.QuartzJobMapper;
import com.zyc.zdh.dao.TaskLogsMapper;
import com.zyc.zdh.dao.ZdhHaInfoMapper;
import com.zyc.zdh.entity.*;
import com.zyc.zdh.quartz.QuartzManager2;
import com.zyc.zdh.service.EtlTaskService;
import com.zyc.zdh.service.ZdhLogsService;
import com.zyc.zdh.service.impl.DataSourcesServiceImpl;
import com.zyc.zdh.util.DBUtil;
import com.zyc.zdh.util.DateUtil;
import com.zyc.zdh.util.HttpUtil;
import com.zyc.zdh.util.SpringContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class JdbcJob extends JobCommon {

    private static String jobType = "JDBC";

    public static void run(QuartzJobInfo quartzJobInfo, Boolean is_retry) {

        Thread td = Thread.currentThread();
        td.setName(quartzJobInfo.getJob_context());
        long threadId = td.getId();
        System.out.println("线程id:" + threadId);
        String task_logs_id = SnowflakeIdWorker.getInstance().nextId() + "";
        String tk = myid + "_" + threadId + "_" + task_logs_id;

        ZdhLogsService zdhLogsService = (ZdhLogsService) SpringContext.getBean("zdhLogsServiceImpl");
        TaskLogsMapper taskLogsMapper = (TaskLogsMapper) SpringContext.getBean("taskLogsMapper");
        QuartzManager2 quartzManager2 = (QuartzManager2) SpringContext.getBean("quartzManager2");

        try {
            TaskLogs taskLogs = insertTaskLog(task_logs_id, quartzJobInfo, null, "running", "5", tk, taskLogsMapper);
            logger.info("开始执行[" + jobType + "] JOB");
            insertLog(quartzJobInfo.getJob_id(), "info",
                    "开始执行[" + jobType + "] JOB");
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
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JobCommon.chm.remove(tk);
        }


    }

    private static Boolean runCommand(QuartzJobInfo quartzJobInfo, ZdhLogsService zdhLogsService) {
        try {
            logger.info("开始执行调度命令判断是否可行");
            DBUtil dbUtil = new DBUtil();
            Boolean exe_status = true;
            /// /连接jdbc
            String params = quartzJobInfo.getParams().trim();
            if (params.equals("")) {
                exe_status = false;
                logger.info("参数不可为空,必须包含特定参数,zdh.jdbc.url,zdh.jdbc.driver,zdh.jdbc.username,zdh.jdbc.password");
                insertLog(quartzJobInfo.getJob_id(), "error",
                        "参数不可为空,必须包含特定参数,zdh.jdbc.url,zdh.jdbc.driver,zdh.jdbc.username,zdh.jdbc.password");

                return exe_status;
            }
            String url = JSON.parseObject(params).getString("zdh.jdbc.url");
            String driver = JSON.parseObject(params).getString("zdh.jdbc.driver");
            String username = JSON.parseObject(params).getString("zdh.jdbc.username");
            String password = JSON.parseObject(params).getString("zdh.jdbc.password");

            if (url == null || url.equals("") || driver == null || driver.equals("") || username == null || username.equals("") || password == null || password.equals("")) {
                exe_status = false;
                logger.info("[" + jobType + "] JOB ,参数不可为空");
                //插入日志
                insertLog(quartzJobInfo.getJob_id(), "error",
                        "参数不可为空,必须包含特定参数,zdh.jdbc.url,zdh.jdbc.driver,zdh.jdbc.username,zdh.jdbc.password");

                return exe_status;
            }

            String command = quartzJobInfo.getCommand();


            if (command.equals("")) {
                exe_status = true;
            }


            if (quartzJobInfo.getLast_time() == null) {
                //第一次执行,下次执行时间为起始时间+1
                if (quartzJobInfo.getStart_time() == null) {
                    logger.info("[" + jobType + "] JOB ,开始日期为空设置当前日期为开始日期");
                    quartzJobInfo.setStart_time(new Timestamp(new Date().getTime()));
                }
                quartzJobInfo.setNext_time(quartzJobInfo.getStart_time());
            }
            String date_nodash = DateUtil.formatNodash(quartzJobInfo.getNext_time());
            String date_time = DateUtil.formatTime(quartzJobInfo.getNext_time());
            String date = DateUtil.format(quartzJobInfo.getNext_time());
            String new_command = command.replace("zdh.date.nodash", date_nodash)
                    .replace("zdh.date.time", date_time)
                    .replace("zdh.date", date);
            List<String> results = dbUtil.R(driver, url, username, password, new_command, null);
            if (results.size() >= 1) {
                exe_status = true;
            }
            return exe_status;
        } catch (Exception ex) {
            ex.printStackTrace();
            //插入日志
            insertLog(quartzJobInfo.getJob_id(), "error",
                    "[调度平台]:" + ex.getMessage());
            logger.error(ex.getMessage());
            return false;
        }

    }

    /**
     * 执行时间序列
     *
     * @param quartzJobInfo
     */
    public static void runTimeSeq(QuartzJobInfo quartzJobInfo, String task_logs_id) {


        QuartzManager2 quartzManager2 = (QuartzManager2) SpringContext.getBean("quartzManager2");
        QuartzJobMapper quartzJobMapper = (QuartzJobMapper) SpringContext.getBean("quartzJobMapper");
        EtlTaskService etlTaskService = (EtlTaskService) SpringContext.getBean("etlTaskServiceImpl");
        DataSourcesServiceImpl dataSourcesServiceImpl = (DataSourcesServiceImpl) SpringContext.getBean("dataSourcesServiceImpl");
        ZdhLogsService zdhLogsService = (ZdhLogsService) SpringContext.getBean("zdhLogsServiceImpl");
        ZdhHaInfoMapper zdhHaInfoMapper = (ZdhHaInfoMapper) SpringContext.getBean("zdhHaInfoMapper");
        TaskLogsMapper taskLogsMapper = (TaskLogsMapper) SpringContext.getBean("taskLogsMapper");


        boolean end = isCount(jobType, quartzManager2, quartzJobInfo);
        if (end == true) {
            insertLog(quartzJobInfo.getJob_id(), "info", "[" + jobType + "] JOB ,结束调度任务");
            return;
        }

        Boolean exe_status = runCommand(quartzJobInfo, zdhLogsService);

        if (!exe_status) {
            jobFail(jobType, task_logs_id, quartzJobInfo, taskLogsMapper);
        }
        //拼接任务信息发送请求
        if (exe_status) {
            JobCommon.runTimeSeq(jobType, task_logs_id, exe_status, quartzJobInfo, taskLogsMapper, quartzManager2);
        }

        //更新任务信息
        debugInfo(quartzJobInfo);
        //如果执行失败 next_time 时间不变,last_time 不变
        quartzJobMapper.updateByPrimaryKey(quartzJobInfo);


    }


    /**
     * 执行一次任务
     *
     * @param quartzJobInfo
     */
    private static void runOnce(QuartzJobInfo quartzJobInfo, String task_logs_id) {

        logger.info("[" + jobType + "] JOB,任务模式为[ONCE]");

        QuartzManager2 quartzManager2 = (QuartzManager2) SpringContext.getBean("quartzManager2");
        QuartzJobMapper quartzJobMapper = (QuartzJobMapper) SpringContext.getBean("quartzJobMapper");
        ZdhLogsService zdhLogsService = (ZdhLogsService) SpringContext.getBean("zdhLogsServiceImpl");
        TaskLogsMapper taskLogsMapper = (TaskLogsMapper) SpringContext.getBean("taskLogsMapper");

        boolean end = isCount(jobType, quartzManager2, quartzJobInfo);
        if (end == true) {
            insertLog(quartzJobInfo.getJob_id(), "info", "[" + jobType + "] JOB ,结束调度任务");
            return;
        }

        Boolean exe_status = runCommand(quartzJobInfo, zdhLogsService);

        if (!exe_status) {
            jobFail(jobType, task_logs_id, quartzJobInfo, taskLogsMapper);
        }

        //拼接任务信息发送请求
        if (exe_status) {
            JobCommon.runOnce(jobType, task_logs_id, exe_status, quartzJobInfo, taskLogsMapper, quartzManager2);
        }

        quartzJobMapper.updateByPrimaryKey(quartzJobInfo);


    }


    /**
     * 执行重复任务
     *
     * @param quartzJobInfo
     */
    private static void runRepeat(QuartzJobInfo quartzJobInfo, String task_logs_id) {

        logger.info("[" + jobType + "] JOB,任务模式为[重复执行模式]");

        QuartzManager2 quartzManager2 = (QuartzManager2) SpringContext.getBean("quartzManager2");
        QuartzJobMapper quartzJobMapper = (QuartzJobMapper) SpringContext.getBean("quartzJobMapper");
        ZdhLogsService zdhLogsService = (ZdhLogsService) SpringContext.getBean("zdhLogsServiceImpl");
        TaskLogsMapper taskLogsMapper = (TaskLogsMapper) SpringContext.getBean("taskLogsMapper");

        boolean end = isCount(jobType, quartzManager2, quartzJobInfo);
        if (end == true) {
            insertLog(quartzJobInfo.getJob_id(), "info", "[" + jobType + "] JOB ,结束调度任务");
            return;
        }

        Boolean exe_status = false;
        exe_status = runCommand(quartzJobInfo, zdhLogsService);
        if (!exe_status) {
            jobFail(jobType, task_logs_id, quartzJobInfo, taskLogsMapper);
        }
        //拼接任务信息发送请求
        if (exe_status) {
            JobCommon.runRepeat(jobType, task_logs_id, exe_status, quartzJobInfo, taskLogsMapper, quartzManager2);
        }
        if (quartzJobInfo.getLast_time() == null) {
            //第一次执行,下次执行时间为起始时间+1
            quartzJobInfo.setNext_time(quartzJobInfo.getStart_time());
        }

        //更新任务信息
        debugInfo(quartzJobInfo);
        //如果执行失败 next_time 时间不变,last_time 不变
        quartzJobMapper.updateByPrimaryKey(quartzJobInfo);

    }

}
