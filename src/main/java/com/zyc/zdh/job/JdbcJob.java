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

public class JdbcJob extends JobCommon {

    private static String jobType="JDBC";

    public static void run(QuartzJobInfo quartzJobInfo) {

        ZdhLogsService zdhLogsService = (ZdhLogsService) SpringContext.getBean("zdhLogsServiceImpl");
        TaskLogsMapper taskLogsMapper = (TaskLogsMapper) SpringContext.getBean("taskLogsMapper");
        QuartzManager2 quartzManager2=(QuartzManager2)SpringContext.getBean("quartzManager2");

        String task_logs_id=SnowflakeIdWorker.getInstance().nextId()+"";
        TaskLogs taskLogs =insertTaskLog(task_logs_id,quartzJobInfo,null,"running","5",taskLogsMapper);
        logger.info("开始执行["+jobType+"] JOB");
        insertLog(quartzJobInfo.getJob_id(),"info",
                "开始执行["+jobType+"] JOB");
        //debugInfo(quartzJobInfo);

        //第一次 last_time 为空 赋值start_time
        if(quartzJobInfo.getLast_time()==null){
            quartzJobInfo.setLast_time(quartzJobInfo.getStart_time());
        }

        //last_status 表示 finish,etl,error
        //finish 表示成功,etl 表示正在处理,error 表示失败
        if (quartzJobInfo.getLast_status() != null && quartzJobInfo.getLast_status().equals("etl")) {
            logger.info("["+jobType+"] JOB ,当前任务正在处理中");
            insertLog(quartzJobInfo.getJob_id(),"info",
                    "["+jobType+"] JOB ,当前任务正在处理中");
            return;
        }

        //finish 状态 last_time 增加步长
        if (quartzJobInfo.getLast_status() != null && quartzJobInfo.getLast_status().equals("finish")) {
            Timestamp last = quartzJobInfo.getLast_time();
            Timestamp next = quartzJobInfo.getNext_time();
            String step_size = quartzJobInfo.getStep_size();
            int dateType = Calendar.DAY_OF_MONTH;
            int num = 1;
            if (step_size.endsWith("s")) {
                dateType = Calendar.SECOND;
                num = Integer.parseInt(step_size.split("s")[0]);
            }
            if (step_size.endsWith("m")) {
                dateType = Calendar.MINUTE;
                num = Integer.parseInt(step_size.split("m")[0]);
            }
            if (step_size.endsWith("h")) {
                dateType = Calendar.HOUR;
                num = Integer.parseInt(step_size.split("h")[0]);
            }
            if (step_size.endsWith("d")) {
                dateType = Calendar.DAY_OF_MONTH;
                num = Integer.parseInt(step_size.split("d")[0]);
            }
            //finish成功状态 判断last_time 是否超过结束日期,超过，删除任务,更新状态
            if (DateUtil.add(last, dateType, num).after(quartzJobInfo.getEnd_time())) {
                logger.info("["+jobType+"] JOB ,当前任务时间超过结束时间,任务结束");
                insertLog(quartzJobInfo.getJob_id(), "info", "[SHELL] JOB ,当前任务时间超过结束时间,任务结束");
                quartzJobInfo.setStatus("finish");
                //删除quartz 任务
                quartzManager2.deleteTask(quartzJobInfo, "finish");

                return;
            }

            if(quartzJobInfo.getStart_time().before(DateUtil.add(last, dateType, num)) ||
                    quartzJobInfo.getStart_time().equals(DateUtil.add(last, dateType, num)) ){
                logger.info("上次执行任务成功,计数新的执行日期:"+DateUtil.add(last, dateType, num));
                insertLog(quartzJobInfo.getJob_id(), "info", "上次执行任务成功,计数新的执行日期:"+DateUtil.add(last, dateType, num));
                quartzJobInfo.setLast_time(DateUtil.add(last, dateType, num));
                quartzJobInfo.setNext_time(DateUtil.add(quartzJobInfo.getLast_time(), dateType, num));
            }else{
                logger.info("上次执行任务成功,下次执行日期为起始日期:"+quartzJobInfo.getStart_time());
                insertLog(quartzJobInfo.getJob_id(), "info", "上次执行任务成功,下次执行日期为起始日期:"+quartzJobInfo.getStart_time());
            }
        }


        //error 状态  next_time 减一天
        if (quartzJobInfo.getLast_status() != null && quartzJobInfo.getLast_status().equals("error")) {
            logger.info("["+jobType+"] JOB ,上次任务处理失败,将重新执行,上次日期:"+quartzJobInfo.getLast_time());
            //插入日志
            insertLog(quartzJobInfo.getJob_id(), "info", "["+jobType+"] JOB ,上次任务处理失败,将重新执行,上次日期:"+quartzJobInfo.getLast_time());
        }

        String date = DateUtil.formatTime(quartzJobInfo.getLast_time());
        taskLogs.setEtl_date(date);
        taskLogs.setUpdate_time(new Timestamp(new Date().getTime()));
        updateTaskLog(taskLogs,taskLogsMapper);

        if (quartzJobInfo.getJob_model().equals(JobModel.TIME_SEQ.getValue())) {
            runTimeSeq(quartzJobInfo,task_logs_id);
        } else if (quartzJobInfo.getJob_model().equals(JobModel.ONCE.getValue())) {
            runOnce(quartzJobInfo,task_logs_id);
        } else if (quartzJobInfo.getJob_model().equals(JobModel.REPEAT.getValue())) {
            runRepeat(quartzJobInfo,task_logs_id);
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
                insertLog(quartzJobInfo.getJob_id(),"error",
                        "参数不可为空,必须包含特定参数,zdh.jdbc.url,zdh.jdbc.driver,zdh.jdbc.username,zdh.jdbc.password");

                return exe_status;
            }
            String url = JSON.parseObject(params).getString("zdh.jdbc.url");
            String driver = JSON.parseObject(params).getString("zdh.jdbc.driver");
            String username = JSON.parseObject(params).getString("zdh.jdbc.username");
            String password = JSON.parseObject(params).getString("zdh.jdbc.password");

            if (url == null || url.equals("") || driver == null || driver.equals("") || username == null || username.equals("") || password == null || password.equals("")) {
                exe_status = false;
                logger.info("["+jobType+"] JOB ,参数不可为空");
                //插入日志
                insertLog(quartzJobInfo.getJob_id(),"error",
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
                    logger.info("["+jobType+"] JOB ,开始日期为空设置当前日期为开始日期");
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
            insertLog(quartzJobInfo.getJob_id(),"error",
                    "[调度平台]:"+ex.getMessage());
            logger.error(ex.getMessage());
            return false;
        }

    }

    /**
     * 执行时间序列
     *
     * @param quartzJobInfo
     */
    public static void runTimeSeq(QuartzJobInfo quartzJobInfo,String task_logs_id) {


        QuartzManager2 quartzManager2 = (QuartzManager2) SpringContext.getBean("quartzManager2");
        QuartzJobMapper quartzJobMapper = (QuartzJobMapper) SpringContext.getBean("quartzJobMapper");
        EtlTaskService etlTaskService = (EtlTaskService) SpringContext.getBean("etlTaskServiceImpl");
        DataSourcesServiceImpl dataSourcesServiceImpl = (DataSourcesServiceImpl) SpringContext.getBean("dataSourcesServiceImpl");
        ZdhLogsService zdhLogsService = (ZdhLogsService) SpringContext.getBean("zdhLogsServiceImpl");
        ZdhHaInfoMapper zdhHaInfoMapper=(ZdhHaInfoMapper) SpringContext.getBean("zdhHaInfoMapper");
        TaskLogsMapper taskLogsMapper = (TaskLogsMapper) SpringContext.getBean("taskLogsMapper");
        //finish成功状态 判断next_time 是否超过结束日期,超过，删除任务,更新状态
        if (quartzJobInfo.getNext_time() != null && quartzJobInfo.getNext_time().after(quartzJobInfo.getEnd_time())) {
            logger.info("["+jobType+"] JOB ,下次时间超过结束时间,任务结束");
            quartzJobInfo.setStatus("finish");
            //删除quartz 任务
            quartzManager2.deleteTask(quartzJobInfo, "finish");

            return;
        }


        //判断次数
        quartzJobInfo.setCount(quartzJobInfo.getCount() + 1);
        boolean end=isCount("JDBC", quartzManager2, quartzJobInfo);
        if(end==true){
            insertLog(quartzJobInfo.getJob_id(), "info", "["+jobType+"] JOB ,结束调度任务");
            return;
        }

        Boolean exe_status = runCommand(quartzJobInfo, zdhLogsService);

        //拼接任务信息发送请求
        if (exe_status) {
            logger.info("["+jobType+"] JOB ,调度命令执行成功,准备发往任务到后台ETL执行");
            insertLog(quartzJobInfo.getJob_id(), "info", "["+jobType+"] JOB ,调度命令执行成功,准备发往任务到后台ETL执行");
            exe_status=sendZdh(task_logs_id,"["+jobType+"]",exe_status,quartzJobInfo);

            if (exe_status) {
                logger.info("["+jobType+"] JOB ,执行命令成功");

                if (quartzJobInfo.getEnd_time() == null) {
                    logger.info("["+jobType+"] JOB ,结束日期为空设置当前日期为结束日期");
                    quartzJobInfo.setEnd_time(new Timestamp(new Date().getTime()));
                }

            }else{
                //调度完成,拼接任务信息异常
                updateTaskLogError(task_logs_id,"12",taskLogsMapper);
                quartzJobInfo.setLast_status("error");
            }


        } else {
            logger.info("["+jobType+"] JOB ,调度命令执行失败未能发往任务到后台ETL执行");
            insertLog(quartzJobInfo.getJob_id(), "info", "["+jobType+"] JOB ,调度命令执行失败未能发往任务到后台ETL执行");
            //调度时异常
            updateTaskLogError(task_logs_id,"8",taskLogsMapper);
            quartzJobInfo.setLast_status("error");
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
    private static void runOnce(QuartzJobInfo quartzJobInfo,String task_logs_id) {

        logger.info("["+jobType+"] JOB,任务模式为[ONCE]");

        QuartzManager2 quartzManager2 = (QuartzManager2) SpringContext.getBean("quartzManager2");
        QuartzJobMapper quartzJobMapper = (QuartzJobMapper) SpringContext.getBean("quartzJobMapper");
        ZdhLogsService zdhLogsService = (ZdhLogsService) SpringContext.getBean("zdhLogsServiceImpl");
        TaskLogsMapper taskLogsMapper = (TaskLogsMapper) SpringContext.getBean("taskLogsMapper");
        //判断次数
        quartzJobInfo.setCount(quartzJobInfo.getCount() + 1);
        boolean end=isCount("JDBC", quartzManager2, quartzJobInfo);
        if(end==true){
            insertLog(quartzJobInfo.getJob_id(), "info", "["+jobType+"] JOB ,结束调度任务");
            return;
        }


        if (quartzJobInfo.getPlan_count().trim().equals("-1")) {
            logger.info("["+jobType+"] JOB ,当前任务未设置执行次数限制");
        }
        Boolean exe_status = runCommand(quartzJobInfo, zdhLogsService);

        //拼接任务信息发送请求
        if (exe_status) {
            logger.info("["+jobType+"] JOB ,调度命令执行成功,准备发往任务到后台ETL执行");
            insertLog(quartzJobInfo.getJob_id(), "info", "["+jobType+"] JOB ,调度命令执行成功,准备发往任务到后台ETL执行");
            exe_status=sendZdh(task_logs_id,"["+jobType+"]",exe_status,quartzJobInfo);

            if (exe_status) {
                System.out.println("["+jobType+"] JOB ,执行命令成功");
                if (quartzJobInfo.getEnd_time() == null) {
                    logger.info("["+jobType+"] JOB ,结束日期为空设置当前日期为结束日期");
                    quartzJobInfo.setEnd_time(new Timestamp(new Date().getTime()));
                }

                //更新任务信息
                debugInfo(quartzJobInfo);
                System.out.println("===================================");
                quartzJobInfo.setStatus("finish");
                //delete 里面包含更新
                quartzManager2.deleteTask(quartzJobInfo, "finish");
                //插入日志
                insertLog(quartzJobInfo.getJob_id(),"info","["+jobType+"] JOB ,结束调度任务");
            } else {
                //调度完成,拼接任务信息异常
                updateTaskLogError(task_logs_id,"12",taskLogsMapper);
                quartzJobInfo.setLast_status("error");
                //如果执行失败 next_time 时间不变,last_time 不变

            }

        } else {
            logger.info("["+jobType+"] JOB ,调度命令执行失败未能发往任务到后台ETL执行");
            insertLog(quartzJobInfo.getJob_id(), "info", "["+jobType+"] JOB ,调度命令执行失败未能发往任务到后台ETL执行");
            //调度时异常
            updateTaskLogError(task_logs_id,"8",taskLogsMapper);
            quartzJobInfo.setLast_status("error");
        }



        quartzJobMapper.updateByPrimaryKey(quartzJobInfo);


    }


    /**
     * 执行重复任务
     *
     * @param quartzJobInfo
     */
    private static void runRepeat(QuartzJobInfo quartzJobInfo,String task_logs_id) {

        logger.info("["+jobType+"] JOB,任务模式为[重复执行模式]");

        QuartzManager2 quartzManager2 = (QuartzManager2) SpringContext.getBean("quartzManager2");
        QuartzJobMapper quartzJobMapper = (QuartzJobMapper) SpringContext.getBean("quartzJobMapper");
        ZdhLogsService zdhLogsService = (ZdhLogsService) SpringContext.getBean("zdhLogsServiceImpl");
        TaskLogsMapper taskLogsMapper = (TaskLogsMapper) SpringContext.getBean("taskLogsMapper");

        //判断次数
        quartzJobInfo.setCount(quartzJobInfo.getCount() + 1);
        boolean end=isCount("JDBC", quartzManager2, quartzJobInfo);

        if(end==true){
            insertLog(quartzJobInfo.getJob_id(), "info", "["+jobType+"] JOB ,结束调度任务");
            return;
        }

        if (quartzJobInfo.getPlan_count().trim().equals("-1")) {
            logger.info("["+jobType+"] JOB ,当前任务未设置执行次数限制,此任务将一直执行");
        }
        Boolean exe_status = false;
        exe_status = runCommand(quartzJobInfo, zdhLogsService);

        //拼接任务信息发送请求
        if (exe_status) {
            logger.info("["+jobType+"] JOB ,调度命令执行成功,准备发往任务到后台ETL执行");
            insertLog(quartzJobInfo.getJob_id(), "info", "["+jobType+"] JOB ,调度命令执行成功,准备发往任务到后台ETL执行");
            exe_status=sendZdh(task_logs_id,"["+jobType+"]",exe_status,quartzJobInfo);

            if (exe_status) {
                System.out.println("["+jobType+"] JOB ,执行命令成功");

                if (quartzJobInfo.getEnd_time() == null) {
                    logger.info("["+jobType+"] JOB ,结束日期为空设置当前日期为结束日期");
                    quartzJobInfo.setEnd_time(new Timestamp(new Date().getTime()));
                }
            }else{
                updateTaskLogError(task_logs_id,"12",taskLogsMapper);
                quartzJobInfo.setLast_status("error");
            }

        } else {
            logger.info("["+jobType+"] JOB ,调度命令执行失败未能发往任务到后台ETL执行");
            insertLog(quartzJobInfo.getJob_id(), "info", "["+jobType+"] JOB ,调度命令执行失败未能发往任务到后台ETL执行");
            //调度时异常
            updateTaskLogError(task_logs_id,"8",taskLogsMapper);
            quartzJobInfo.setLast_status("error");
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
