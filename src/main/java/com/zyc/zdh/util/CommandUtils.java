package com.zyc.zdh.util;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import com.zyc.zdh.entity.TaskLogInstance;
import com.zyc.zdh.job.JobCommon2;
import org.apache.commons.exec.*;

/**
 * 执行系统命令工具类
 *
 * @author Storm
 *
 */
public class CommandUtils {

    public static final String DEFAULT_CHARSET = "GBK";

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

    public static Map<String,String> exeCommand2(TaskLogInstance tli, String cmd, String param, String command, String charsetName) throws IOException {
        Map map=new HashMap<String,String>();
        ProcessBuilder processBuilder = new ProcessBuilder();
        try {
            processBuilder.command(cmd,param ,command);
            Process process = processBuilder.start();

//            StringBuilder output = new StringBuilder();

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream(),charsetName));
            BufferedReader reader_err = new BufferedReader(
                    new InputStreamReader(process.getErrorStream(),charsetName));

            String line;
            while ((line = reader.readLine()) != null) {
//                output.append(line + "\n");
                JobCommon2.insertLog(tli,"INFO","实时日志:"+line);
            }
            while ((line = reader_err.readLine()) != null) {
//                output.append(line + "\n");
                JobCommon2.insertLog(tli,"ERROR","实时日志:"+line);
            }

            int exitVal = process.waitFor();
            if (exitVal == 0) {
                System.out.println("Success!");
                map.put("result","success");
//                map.put("out",output);
                map.put("error","");
            } else {
                System.out.println("Error!");
                map.put("result","fail");
                map.put("out","");
//                map.put("error",output);
            }
//            System.out.println(output);
            process.destroy();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return map;

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
            e.printStackTrace();
        }
    }

}