package com.zyc.zdh.job;

import com.alibaba.fastjson.JSON;
import com.zyc.zdh.entity.EtlTaskLogInfo;
import com.zyc.zdh.entity.TaskLogInstance;
import com.zyc.zdh.util.Const;
import com.zyc.zdh.util.SFTPUtil;
import com.zyc.zdh.util.SSHUtil;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class FlumeJob extends JobCommon2 {

    public static String jobType = "FLUME";


    /**
     * 当前shell实现只支持同步类型的shell
     * @param tli
     * @return
     */
    public static Boolean flumeCommand(TaskLogInstance tli) {
        Boolean exe_status = true;
        //执行命令
        try {
            long t1 = System.currentTimeMillis();
            logger.info("flume任务当前只支持同步方式,异步方式暂不支持,当前仅支持flume采集,输出数据源暂未实现...");
            insertLog(tli,"info","flume任务当前只支持同步方式,异步方式暂不支持,当前仅支持flume采集,输出数据源暂未实现...");
            //当前只支持检查文件是否存在 if [ ! -f "/data/filename" ];then echo "文件不存在"; else echo "true"; fi
            //日期替换zdh.date => yyyy-MM-dd 模式
            //日期替换zdh.date.nodash=> yyyyMMdd 模式

            if(tli.getJump_script()!=null && tli.getJump_script().equalsIgnoreCase(Const.ON)){
                logger.info("跳过脚本验证");
                insertLog(tli,"info","跳过脚本验证");
                return exe_status;
            }

            //构造flume命令 bin/flume-ng agent -n a1 -c conf -f job/btrc_flume_kafka.conf -Dflume.root.log ger=INFO,console

            if(StringUtils.isEmpty(tli.getEtl_info())){
                throw new Exception("flume无法获取执行任务信息");
            }
            EtlTaskLogInfo taskLogInfo=JSON.parseObject(tli.getEtl_info(), EtlTaskLogInfo.class);
            if(StringUtils.isEmpty(taskLogInfo.getFlume_command())){
                throw new Exception("flume任务执行命令信息不可为空");
            }
            if(taskLogInfo.getFlume_path().contains("-f")){
                throw new Exception("flume命令行信息不可包含-f信息,-f信息将自动生成");
            }
            if(StringUtils.isEmpty(taskLogInfo.getFlume_path())){
                throw new Exception("flume路径信息不可为空");
            }
            if(StringUtils.isEmpty(taskLogInfo.getJob_config())){
                throw new Exception("flume配置信息不可为空");
            }

            logger.info("[" + jobType + "] JOB ,以脚本方式执行");
            insertLog(tli, "info", "[" + jobType + "] JOB ,以脚本方式执行");
            String fileName = "job_"+tli.getId();
            String system = System.getProperty("os.name");

            String[] str = taskLogInfo.getJob_config().split("\r\n|\n");
            String newcommand = "";
            String line = Const.LINE_SEPARATOR;
            for (String s : str) {
                newcommand = newcommand + s + "\n";
                newcommand=newcommand.trim();
            }

            File file3 = new File("shell_script/" + tli.getJob_id());
            if (!file3.exists()) {
                file3.mkdirs();
            }

            File file4 = new File("shell_script/" + tli.getJob_id() + "/" + fileName+".conf");
            if (!file4.exists()) {
                file4.createNewFile();
            }
            file4.setExecutable(true,false);
            file4.setExecutable(true,false);
            file4.setWritable(true,false);

            String ssh_cmd= "cd "+taskLogInfo.getFlume_path()+" && "+taskLogInfo.getFlume_command() +" -f "+taskLogInfo.getFlume_path()+"/jobs/"+file4.getName();

            String host = taskLogInfo.getHost();
            String port = taskLogInfo.getPort();
            String username = taskLogInfo.getUser_name();
            String password = taskLogInfo.getPassword();

            SFTPUtil sftpUtil = new SFTPUtil(username, password, host, Integer.parseInt(port));
            sftpUtil.login();

            try {
                newcommand = "";
                for (String s : str) {
                    newcommand = newcommand + s + line;
                }
                insertLog(tli, "DEBUG", "[调度平台]:FLUME,使用在线配置," + newcommand);
                sftpUtil.upload(taskLogInfo.getFlume_path()+"/jobs", file4.getName(), newcommand,"UTF-8");

            } catch (Exception e) {
                logger.error("类:" + Thread.currentThread().getStackTrace()[1].getClassName() + " 函数:" + Thread.currentThread().getStackTrace()[1].getMethodName() + " 异常: {}" , e);
                throw e;
            } finally {
                sftpUtil.logout();
            }

            SSHUtil sshUtil = new SSHUtil(username, password, host, Integer.parseInt(port));
            sshUtil.login();
            chm_ssh.put(tli.getId(), sshUtil);
            ssh_cmd = "echo task_id=" + tli.getId() + " && " + ssh_cmd;
            insertLog(tli, "DEBUG", "[调度平台]:FLUME,使用在线脚本," + ssh_cmd);
            String[] result = sshUtil.exec(ssh_cmd, tli.getId(), tli.getJob_id());
            String error = result[0];
            String out = result[1];
            if (chm_ssh.get(tli.getId()) != null) {
                chm_ssh.get(tli.getId()).logout();
            }
            chm_ssh.remove(tli.getId());
            long t2 = System.currentTimeMillis();

            insertLog(tli, "DEBUG", "[调度平台]:FLUME,任务执行结束,耗时:" + (t2 - t1) / 1000 + "s");

            if (!error.isEmpty()) {
                return false;
            }
            return true;
        } catch (Exception e) {
             logger.error("类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}", e);
            logger.error(e.getMessage());
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
                 logger.error("类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}", e);
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
