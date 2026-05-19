package com.zyc.zdh.job;

import com.hubspot.jinjava.Jinjava;
import com.zyc.zdh.entity.TaskLogInstance;
import com.zyc.zdh.util.CommandUtils;
import com.zyc.zdh.util.Const;
import com.zyc.zdh.util.LogUtil;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class ShellJob extends JobCommon2 {

    public static String jobType = "SHELL";


    /**
     * 当前shell实现只支持同步类型的shell
     * @param tli
     * @return
     */
    public static Boolean shellCommand(TaskLogInstance tli) {
        Boolean exe_status = true;
        //执行命令
        try {
            LogUtil.info(ShellJob.class, "shell任务当前只支持同步shell,异步shell暂不支持");
            insertLog(tli,"info","shell任务当前只支持同步shell,异步shell暂不支持");
            //当前只支持检查文件是否存在 if [ ! -f "/data/filename" ];then echo "文件不存在"; else echo "true"; fi
            //日期替换zdh.date => yyyy-MM-dd 模式
            //日期替换zdh.date.nodash=> yyyyMMdd 模式
            Map<String, Object> jinJavaParam = getJinJavaParam(tli);

            Jinjava jj = new Jinjava();

            if(tli.getJump_script()!=null && tli.getJump_script().equalsIgnoreCase(Const.ON)){
                LogUtil.info(ShellJob.class, "跳过脚本验证");
                insertLog(tli,"info","跳过脚本验证");
                return exe_status;
            }

            if (!tli.getCommand().trim().equals("")) {
                LogUtil.info(ShellJob.class, "[" + jobType + "] JOB ,COMMAND:" + tli.getCommand());
                insertLog(tli, "info", "[" + jobType + "] JOB ,COMMAND:" + tli.getCommand());
                Map result = new HashMap<String,String>();
                result.put("result", "fail");
                if (tli.getCommand().trim().equals("")) {
                    result.put("result", "success");
                } else {
                    String system = System.getProperty("os.name");
                    String command=jj.render(tli.getCommand(), jinJavaParam);

                    String[] str = command.split("\r\n");
                    String newcommand = "";
                    String line = Const.LINE_SEPARATOR;
                    for (String s : str) {
                        newcommand = newcommand + s + line;
                        newcommand=newcommand.trim();
                    }

                    if (newcommand.toLowerCase().matches("(.*rm)\\s*-[rf | r | f].*")){
                        throw new Exception("shell命令/脚本,不允许使用rm命令");
                    }
                    //脚本执行
                    if (tli.getIs_script() != null && tli.getIs_script().equals("true")) {
                        LogUtil.info(ShellJob.class, "[" + jobType + "] JOB ,以脚本方式执行");
                        insertLog(tli, "info", "[" + jobType + "] JOB ,以脚本方式执行");
                        String fileName = System.currentTimeMillis() + "";
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
                        file2.setExecutable(true,false);
                        file2.setExecutable(true,false);
                        file2.setWritable(true,false);
                        LogUtil.info(ShellJob.class, "生成脚本临时文件:" + file2.getAbsolutePath());
                        LogUtil.info(ShellJob.class, "脚本内容:" + line + newcommand);
                        BufferedWriter fileWritter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file2.getAbsolutePath(), true), "UTF-8"));
                        fileWritter.write(newcommand);
                        fileWritter.close();
                        LogUtil.info(ShellJob.class, "当前系统为:" + system + ",command:" + newcommand);
                        if (system.toLowerCase().startsWith("win")) {
                            result = CommandUtils.exeCommand2(tli,"cmd.exe", "/c", file2.getAbsolutePath(), "GBK");
                        } else {
                            result = CommandUtils.exeCommand2(tli,"sh", "-c", file2.getAbsolutePath(),"GBK");
                        }
                    } else {
                        //命令行执行
                        LogUtil.info(ShellJob.class, "[" + jobType + "] JOB ,以命令行方式执行");
                        insertLog(tli, "info", "[" + jobType + "] JOB ,以命令行方式执行");
                        LogUtil.info(ShellJob.class, "当前系统为:" + system + ",command:" + newcommand + ",命令行方式执行;");
                        insertLog(tli, "info", "[" + jobType + "] JOB ,当前系统为:" + system+",command:"+newcommand+",命令行方式执行;");
                        if (system.toLowerCase().startsWith("win")) {
                            result = CommandUtils.exeCommand2(tli,"cmd.exe","/c" , newcommand,"GBK");
                        } else {
                            result = CommandUtils.exeCommand2(tli,"sh","-c",newcommand,"GBK");
                        }
                    }
                }
                LogUtil.info(ShellJob.class, "[" + jobType + "] JOB ,执行结果:" + result.get("result").toString().trim());
//                LogUtil.info(ShellJob.class, "[" + jobType + "] JOB ,正常输出:" + result.get("out").toString().trim());
//                LogUtil.info(ShellJob.class, "[" + jobType + "] JOB ,错误输出:" + result.get("error").toString().trim());
                insertLog(tli, "info", "[" + jobType + "] JOB ,执行结果:" + result.get("result").toString().trim());
//                insertLog(tli, "info", "[" + jobType + "] JOB ,正常输出:" + result.get("out").toString().trim());
//                insertLog(tli, "info", "[" + jobType + "] JOB ,错误输出:" + result.get("error").toString().trim());
                if (!result.get("result").toString().trim().contains("success")) {
                    throw new Exception("shell 命令/脚本执行失败");
                }
            } else {
                LogUtil.info(ShellJob.class, "[" + jobType + "] JOB ,执行命令为空,默认返回成功状态");
                insertLog(tli, "info", "[" + jobType + "] JOB ,执行命令为空,默认返回成功状态");
            }
        } catch (Exception e) {
            LogUtil.error(ShellJob.class, e);
            insertLog(tli, "error","[" + jobType + "] JOB ,"+ e.getMessage());
            jobFail(jobType,tli);
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
                LogUtil.error(ShellJob.class, e);
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
