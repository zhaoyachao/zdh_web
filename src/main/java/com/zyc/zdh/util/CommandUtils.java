package com.zyc.zdh.util;

import com.zyc.zdh.entity.TaskLogInstance;
import com.zyc.zdh.job.JobCommon2;
import org.apache.commons.exec.*;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 执行系统命令工具类
 *
 * @author zyc
 *
 */
public class CommandUtils {

    public static final String DEFAULT_CHARSET = "GBK";

    public static final long DEFAULT_TIMEOUT = 60 * 60 * 24;

    /**
     * 执行指定命令
     *
     * @param command 命令
     * @return 命令执行完成返回结果
     * @throws IOException 失败时抛出异常，由调用者捕获处理
     */
    public static Map<String,String> exeCommand(String command) throws IOException {
        try{
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ByteArrayOutputStream error = new ByteArrayOutputStream();
            LogOutputStream log = new LogOutputStream(){

                @Override
                protected void processLine(String s, int i) {

                        System.out.println(i+"-----------"+s);

                }
            };
            int exitCode = exeCommand(command, log,error);
            System.out.println("exitCode:"+exitCode);
            Map map=new HashMap<String,String>();
            if (exitCode == 0) {
                String out_str=out.toString(DEFAULT_CHARSET);
                String err_str=error.toString(DEFAULT_CHARSET);
                System.out.println("命令运行成功!");
                System.out.println("out:"+out_str);
                System.out.println("error:"+err_str);

                map.put("result","success");
                map.put("out",out_str);
                map.put("error",err_str);

                return map;
            } else if(exitCode !=0 && !StringUtils.isEmpty(error.toString(DEFAULT_CHARSET))) {
                System.out.println("命令运行失败!");
                System.out.println("out:"+out.toString(DEFAULT_CHARSET));
                System.out.println("error:"+error.toString(DEFAULT_CHARSET));
            }
            map.put("out",out.toString(DEFAULT_CHARSET));
            map.put("error",error.toString(DEFAULT_CHARSET));
            map.put("result","fail");
            return map;
        }catch (Exception e){
            throw e;
        }
    }

    /**
     * 当前执行仅适用于当前平台
     * @param tli
     * @param cmd
     * @param param
     * @param command
     * @param charsetName
     * @return
     * @throws IOException
     */
    public static Map<String,String> exeCommand2(TaskLogInstance tli, String cmd, String param, String command, String charsetName) throws IOException {
        Map map=new HashMap<String,String>();
        ProcessBuilder processBuilder = new ProcessBuilder();
        try {
            processBuilder.redirectErrorStream(true);//合并输出流
            processBuilder.command(cmd,param ,command);
            Process process = processBuilder.start();

            JobCommon2.chm_process.put(tli.getId(), process);
            new Thread(() -> readStream(tli,process.getInputStream(), charsetName)).start();

            int exitVal = -1;

            String timeout = tli.getTime_out();
            if(StringUtils.isEmpty(timeout)){
                //此处不设置超时,是非常危险的,可优化
                exitVal = process.waitFor();
            }else{
                boolean isCompleted = process.waitFor(Long.valueOf(timeout), TimeUnit.SECONDS);
                if (isCompleted) {
                    // 正常退出，返回退出码
                    exitVal = process.exitValue();
                } else {
                    // 超时，强制终止
                    try{
                        process.getInputStream().close();
                        process.destroy();
                    }catch (Exception e){
                        LogUtil.error(CommandUtils.class, e);
                    }finally {
                        process.destroyForcibly();
                    }
                    JobCommon2.insertLog(tli,"INFO","实时日志: 超时,已强制终止");
                }
            }

            if (exitVal == 0) {
                map.put("result","success");
                map.put("error","");
            } else {
                map.put("result","fail");
                map.put("out","");
            }

            if (process.isAlive()) {
                try{
                    process.getInputStream().close();
                    process.destroyForcibly();
                }catch (Exception e){
                }
                JobCommon2.insertLog(tli,"INFO","实时日志: 结束后检测到进程,再次强杀进程");
            }
        } catch (IOException e) {
            LogUtil.error(CommandUtils.class, e);
        } catch (InterruptedException e) {
            LogUtil.error(CommandUtils.class, e);
        }
        return map;
    }

    // 复用读取流工具方法
    private static void readStream(TaskLogInstance tli, InputStream is, String charsetName) {
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(is, charsetName))) {
            String line;
            while ((line = br.readLine()) != null) {
                if(Thread.currentThread().isInterrupted()){
                    throw new Exception("停止运行");
                }
                JobCommon2.insertLog(tli,"INFO","实时日志:"+line);
            }
        } catch (Exception e) {
            // 子进程被终止时，流会关闭，此处无需报错
            JobCommon2.insertLog(tli,"INFO","实时日志: 流已关闭（子进程终止）");
            System.out.println(" 流已关闭（子进程终止）");
        }
    }

    /**
     * 执行指定命令，输出结果到指定输出流中
     *
     * @param command 命令
     * @param out 执行结果输出流
     * @return 执行结果状态码：执行成功返回0
     * @throws ExecuteException 失败时抛出异常，由调用者捕获处理
     * @throws IOException 失败时抛出异常，由调用者捕获处理
     */
    public static int exeCommand(String command, OutputStream out, OutputStream error) throws ExecuteException, IOException {
        CommandLine commandLine = CommandLine.parse(command);
        PumpStreamHandler pumpStreamHandler = null;
        if (null == out) {
            pumpStreamHandler = new PumpStreamHandler();
        } else {
            pumpStreamHandler = new PumpStreamHandler(out);
        }

        // 设置超时时间为10秒
        ExecuteWatchdog watchdog = new ExecuteWatchdog(10000);

        DefaultExecutor executor = new DefaultExecutor();
        executor.setExitValues(new int[]{0});
        executor.setStreamHandler(pumpStreamHandler);
        executor.setWatchdog(watchdog);

        return executor.execute(commandLine);
    }

    public static void main(String[] args) {
        try {
//            String result = exeCommand("cmd.exe /c if exist D:/postal_fund_app.jar (\n" +
//                    "       echo true\n" +
//                    "    ) else (\n" +
//                    "        echo false\n" +
//                    "    )");
//            System.out.println(result);
            TaskLogInstance tli=new TaskLogInstance();
            tli.setId("1");
            tli.setJob_id("1");
            Map result = exeCommand2(tli,"cmd.exe", "/c", "java version","GBK");
            System.out.println(result.get("result"));

        } catch (IOException e) {
            LogUtil.error(CommandUtils.class, e);
        }
    }

}