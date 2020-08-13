package com.zyc.zdh.job;

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
import org.springframework.core.env.Environment;

import java.io.*;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class ShellJob extends JobCommon {

    private static String jobType = "SHELL";

    public static void run(QuartzJobInfo quartzJobInfo,Boolean is_retry) {
        Thread td=Thread.currentThread();
        td.setName(quartzJobInfo.getJob_context());
        long threadId = td.getId();
        System.out.println("线程id:"+threadId);
        String task_logs_id = SnowflakeIdWorker.getInstance().nextId() + "";
        String tk=myid+"_"+threadId+"_"+task_logs_id;
        JobCommon.chm.put(tk,td);
        try{
            ZdhLogsService zdhLogsService = (ZdhLogsService) SpringContext.getBean("zdhLogsServiceImpl");
            TaskLogsMapper taskLogsMapper = (TaskLogsMapper) SpringContext.getBean("taskLogsMapper");
            QuartzJobMapper quartzJobMapper = (QuartzJobMapper) SpringContext.getBean("quartzJobMapper");
            QuartzManager2 quartzManager2 = (QuartzManager2) SpringContext.getBean("quartzManager2");

            TaskLogs taskLogs = insertTaskLog(task_logs_id, quartzJobInfo, null, "dispatch", "5",tk, taskLogsMapper);

            logger.info("开始执行[" + jobType + "] JOB");
            insertLog(quartzJobInfo.getJob_id(), "INFO", "开始执行[" + jobType + "] JOB");
            //debugInfo(quartzJobInfo);

            //检查任务状态,并初始化相应的初始值 比如执行日期
            boolean checks = checkStatus(jobType, quartzJobInfo, taskLogsMapper, quartzManager2);

            if (checks == false) return;

            //检查任务依赖
            boolean dep = checkDep(jobType, quartzJobInfo, taskLogsMapper);
            if (dep == false) return;

            updateTaskLogEtlDate(taskLogs,quartzJobInfo,taskLogsMapper);

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

    private static Boolean shellCommand(QuartzJobInfo quartzJobInfo) {
        Boolean exe_status = true;
        //执行命令
        try {
            //当前只支持检查文件是否存在 if [ ! -f "/data/filename" ];then echo "文件不存在"; else echo "true"; fi
            //日期替换zdh.date => yyyy-MM-dd 模式
            //日期替换zdh.date.nodash=> yyyyMMdd 模式
            logger.info("目前支持日期参数3种模式:zdh.date => yyyy-MM-dd ,zdh.date.nodash=> yyyyMMdd " +
                    ",zdh.date.time=> yyyy-MM-dd HH:mm:ss");
            insertLog(quartzJobInfo.getJob_id(), "info", "目前支持日期参数3种模式:zdh.date => yyyy-MM-dd ,zdh.date.nodash=> yyyyMMdd " +
                    ",zdh.date.time=> yyyy-MM-dd HH:mm:ss");
            if (quartzJobInfo.getLast_time() == null) {
                //第一次执行,下次执行时间为起始时间+1
                if (quartzJobInfo.getStart_time() == null) {
                    logger.info("[" + jobType + "] JOB ,开始日期为空设置当前日期为开始日期");
                    insertLog(quartzJobInfo.getJob_id(), "info", "[" + jobType + "] JOB ,开始日期为空设置当前日期为开始日期");
                    quartzJobInfo.setStart_time(new Timestamp(new Date().getTime()));
                }
                logger.info("上次执行日期,下次执行日期均为空,赋值为:" + quartzJobInfo.getStart_time());
                insertLog(quartzJobInfo.getJob_id(), "info", "上次执行日期,下次执行日期均为空,赋值为:" + quartzJobInfo.getStart_time());
                quartzJobInfo.setLast_time(quartzJobInfo.getStart_time());
                quartzJobInfo.setNext_time(quartzJobInfo.getStart_time());
            }

            if(quartzJobInfo.getJump_script()!=null && quartzJobInfo.getJump_script().equalsIgnoreCase("on")){
                logger.info("跳过脚本验证");
                insertLog(quartzJobInfo.getJob_id(),"info","跳过脚本验证");
                return exe_status;
            }

            if (!quartzJobInfo.getCommand().trim().equals("")) {
                logger.info("========+++++" + quartzJobInfo.getLast_time());
                String date_nodash = DateUtil.formatNodash(quartzJobInfo.getLast_time());
                String date_time = DateUtil.formatTime(quartzJobInfo.getLast_time());
                String date = DateUtil.format(quartzJobInfo.getLast_time());

                logger.info("[" + jobType + "] JOB ,COMMAND:" + quartzJobInfo.getCommand());
                insertLog(quartzJobInfo.getJob_id(), "info", "[" + jobType + "] JOB ,COMMAND:" + quartzJobInfo.getCommand());
                String result = "fail";
                if (quartzJobInfo.getCommand().trim().equals("")) {
                    result = "success";
                } else {
                    String system = System.getProperty("os.name");
                    String command = quartzJobInfo.getCommand().
                            replace("zdh.date.nodash", date_nodash).
                            replace("zdh.date.time", date_time).
                            replace("zdh.date", date);

                    String[] str = command.split("\r\n");
                    String newcommand = "";
                    String line = System.getProperty("line.separator");
                    for (String s : str) {
                        newcommand = newcommand + s + line;
                    }

                    //脚本执行
                    if (quartzJobInfo.getIs_script() != null && quartzJobInfo.getIs_script().equals("true")) {
                        logger.info("[" + jobType + "] JOB ,以脚本方式执行");
                        insertLog(quartzJobInfo.getJob_id(), "info", "[" + jobType + "] JOB ,以脚本方式执行");
                        String fileName = new Date().getTime() + "";
                        if (system.toLowerCase().startsWith("win")) {
                            fileName = fileName + ".bat";
                        } else {
                            fileName = fileName + ".sh";
                        }

                        File file = new File("shell_script/" + quartzJobInfo.getJob_id());
                        if (!file.exists()) {
                            file.mkdirs();
                        }

                        File file2 = new File("shell_script/" + quartzJobInfo.getJob_id() + "/" + fileName);
                        if (!file2.exists()) {
                            file2.createNewFile();
                        }
                        logger.info("生成脚本临时文件:" + file2.getAbsolutePath());
                        logger.info("脚本内容:" + line + newcommand);
                        BufferedWriter fileWritter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file2.getAbsolutePath(), true), "UTF-8"));
                        fileWritter.write(newcommand);
                        fileWritter.close();

                        if (system.toLowerCase().startsWith("win")) {
                            System.out.println("当前系统为：" + system);
                            result = CommandUtils.exeCommand("cmd.exe /k " + file2.getAbsolutePath());
                        } else {
                            result = CommandUtils.exeCommand("sh " + file2.getAbsolutePath());
                        }
                    } else {
                        //命令行执行
                        logger.info("[" + jobType + "] JOB ,以命令行方式执行");
                        insertLog(quartzJobInfo.getJob_id(), "info", "[" + jobType + "] JOB ,以命令行方式执行");
                        result = CommandUtils.exeCommand(newcommand);
                    }
                }
                logger.info("[" + jobType + "] JOB ,执行结果:" + result.trim());
                insertLog(quartzJobInfo.getJob_id(), "info", "[" + jobType + "] JOB ,执行结果:" + result.trim());
                if (!result.trim().contains("success")) {
                    throw new Exception("shell 命令/脚本执行失败");
                }
            } else {
                logger.info("[" + jobType + "] JOB ,执行命令为空,默认返回成功状态");
                insertLog(quartzJobInfo.getJob_id(), "info", "[" + jobType + "] JOB ,执行命令为空,默认返回成功状态");
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
        insertLog(quartzJobInfo.getJob_id(), "info", "[" + jobType + "] JOB,任务模式为[时间序列]");
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
        exe_status = shellCommand(quartzJobInfo);

        if(!exe_status){
            jobFail(jobType,task_logs_id,quartzJobInfo,taskLogsMapper);
        }
        //拼接任务信息发送请求
        if (exe_status) {
           JobCommon.runTimeSeq(jobType, task_logs_id, exe_status, quartzJobInfo, taskLogsMapper, quartzManager2);
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

        TaskLogsMapper taskLogsMapper = (TaskLogsMapper) SpringContext.getBean("taskLogsMapper");
        QuartzManager2 quartzManager2 = (QuartzManager2) SpringContext.getBean("quartzManager2");
        QuartzJobMapper quartzJobMapper = (QuartzJobMapper) SpringContext.getBean("quartzJobMapper");
        ZdhLogsService zdhLogsService = (ZdhLogsService) SpringContext.getBean("zdhLogsServiceImpl");

        logger.info("[" + jobType + "] JOB,任务模式为[ONCE]");
        insertLog(quartzJobInfo.getJob_id(), "info", "[" + jobType + "] JOB,任务模式为[ONCE]");

        boolean end = isCount(jobType, quartzManager2, quartzJobInfo);
        if (end == true) {
            insertLog(quartzJobInfo.getJob_id(), "info", "[" + jobType + "] JOB ,结束调度任务");
            return;
        }

        Boolean exe_status = false;
        exe_status = shellCommand(quartzJobInfo);

        if(!exe_status){
            jobFail(jobType,task_logs_id,quartzJobInfo,taskLogsMapper);
        }

        //拼接任务信息发送请求
        if (exe_status) {
            JobCommon.runOnce(jobType, task_logs_id, exe_status, quartzJobInfo, taskLogsMapper, quartzManager2);
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
        insertLog(quartzJobInfo.getJob_id(), "info", "[" + jobType + "] JOB,任务模式为[重复执行模式]");

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
        exe_status = shellCommand(quartzJobInfo);

        if(!exe_status){
            jobFail(jobType,task_logs_id,quartzJobInfo,taskLogsMapper);
        }

        //拼接任务信息发送请求
        if (exe_status) {
            JobCommon.runRepeat(jobType, task_logs_id, exe_status, quartzJobInfo, taskLogsMapper, quartzManager2);
        }

        //更新任务信息
        debugInfo(quartzJobInfo);

        //如果执行失败 next_time 时间不变,last_time 不变
        quartzJobMapper.updateByPrimaryKey(quartzJobInfo);

    }

    public static String run(String[] command) throws IOException {
        Scanner input = null;
        String result = "";
        Process process = null;
        try {
            process = Runtime.getRuntime().exec(command);
            try {
                //等待命令执行完成
                process.waitFor(10, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            InputStream is = process.getInputStream();
            input = new Scanner(is);
            while (input.hasNextLine()) {
                result += input.nextLine() + "\n";
            }
            //加上命令本身，打印出来
        } finally {
            if (input != null) {
                input.close();
            }
            if (process != null) {
                process.destroy();
            }
        }
        return result;
    }


}
