package com.zyc.zdh.job;

import com.zyc.zdh.dao.TaskLogInstanceMapper;
import com.zyc.zdh.entity.TaskLogInstance;
import com.zyc.zdh.util.CommandUtils;
import com.zyc.zdh.util.DateUtil;
import com.zyc.zdh.util.SpringContext;

import java.io.*;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class ShellJob extends JobCommon {

    public static String jobType = "SHELL";

    public static void run(TaskLogInstance tli,int is_retry) {
        Thread td=Thread.currentThread();
        long threadId = td.getId();
        System.out.println("线程id:"+threadId);
        String tk=myid+"_"+threadId+"_"+tli.getId();
        JobCommon.chm.put(tk,td);
        TaskLogInstanceMapper tlim = (TaskLogInstanceMapper) SpringContext.getBean("taskLogInstanceMapper");
        tlim.updateThreadById(tk,tli.getId());
        tli.setThread_id(tk);
        try{
            JobCommon.chooseCommand(jobType,tli);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            JobCommon.chm.remove(tk);
        }


    }

    public static Boolean shellCommand(TaskLogInstance tli) {
        Boolean exe_status = true;
        //执行命令
        try {
            //当前只支持检查文件是否存在 if [ ! -f "/data/filename" ];then echo "文件不存在"; else echo "true"; fi
            //日期替换zdh.date => yyyy-MM-dd 模式
            //日期替换zdh.date.nodash=> yyyyMMdd 模式
            logger.info("目前支持日期参数3种模式:zdh.date => yyyy-MM-dd ,zdh.date.nodash=> yyyyMMdd " +
                    ",zdh.date.time=> yyyy-MM-dd HH:mm:ss");
            insertLog(tli, "info", "目前支持日期参数3种模式:zdh.date => yyyy-MM-dd ,zdh.date.nodash=> yyyyMMdd " +
                    ",zdh.date.time=> yyyy-MM-dd HH:mm:ss");
            if (tli.getCur_time() == null) {
                //第一次执行,下次执行时间为起始时间+1
                if (tli.getStart_time() == null) {
                    logger.info("[" + jobType + "] JOB ,开始日期为空设置当前日期为开始日期");
                    insertLog(tli, "info", "[" + jobType + "] JOB ,开始日期为空设置当前日期为开始日期");
                    tli.setStart_time(new Timestamp(new Date().getTime()));
                }
                logger.info("上次执行日期,下次执行日期均为空,赋值为:" + tli.getStart_time());
                insertLog(tli, "info", "上次执行日期,下次执行日期均为空,赋值为:" + tli.getStart_time());
                tli.setCur_time(tli.getStart_time());
                tli.setNext_time(tli.getStart_time());
            }

            if(tli.getJump_script()!=null && tli.getJump_script().equalsIgnoreCase("on")){
                logger.info("跳过脚本验证");
                insertLog(tli,"info","跳过脚本验证");
                return exe_status;
            }

            if (!tli.getCommand().trim().equals("")) {
                logger.info("========+++++" + tli.getCur_time());
                String date_nodash = DateUtil.formatNodash(tli.getCur_time());
                String date_time = DateUtil.formatTime(tli.getCur_time());
                String date = DateUtil.format(tli.getCur_time());

                logger.info("[" + jobType + "] JOB ,COMMAND:" + tli.getCommand());
                insertLog(tli, "info", "[" + jobType + "] JOB ,COMMAND:" + tli.getCommand());
                String result = "fail";
                if (tli.getCommand().trim().equals("")) {
                    result = "success";
                } else {
                    String system = System.getProperty("os.name");
                    String command = tli.getCommand().
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
                    if (tli.getIs_script() != null && tli.getIs_script().equals("true")) {
                        logger.info("[" + jobType + "] JOB ,以脚本方式执行");
                        insertLog(tli, "info", "[" + jobType + "] JOB ,以脚本方式执行");
                        String fileName = new Date().getTime() + "";
                        if (system.toLowerCase().startsWith("win")) {
                            fileName = fileName + ".bat";
                        } else {
                            fileName = fileName + ".sh";
                        }

                        File file = new File("shell_script/" + tli.getJob_id());
                        if (!file.exists()) {
                            file.mkdirs();
                        }

                        File file2 = new File("shell_script/" + tli.getJob_id() + "/" + fileName);
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
                        insertLog(tli, "info", "[" + jobType + "] JOB ,以命令行方式执行");
                        result = CommandUtils.exeCommand(newcommand);
                    }
                }
                logger.info("[" + jobType + "] JOB ,执行结果:" + result.trim());
                insertLog(tli, "info", "[" + jobType + "] JOB ,执行结果:" + result.trim());
                if (!result.trim().contains("success")) {
                    throw new Exception("shell 命令/脚本执行失败");
                }
            } else {
                logger.info("[" + jobType + "] JOB ,执行命令为空,默认返回成功状态");
                insertLog(tli, "info", "[" + jobType + "] JOB ,执行命令为空,默认返回成功状态");
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            exe_status = false;
        }
        return exe_status;
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
