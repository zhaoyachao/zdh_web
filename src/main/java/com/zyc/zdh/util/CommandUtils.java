package com.zyc.zdh.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteException;
import org.apache.commons.exec.ExecuteWatchdog;
import org.apache.commons.exec.PumpStreamHandler;

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
    public static String exeCommand(String command) throws IOException {
        try{
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ByteArrayOutputStream error = new ByteArrayOutputStream();
            int exitCode = exeCommand(command, out,error);
            if (exitCode == 0) {
                System.out.println("命令运行成功!");
                System.out.println("out:"+out.toString(DEFAULT_CHARSET));
                System.out.println("error:"+error.toString(DEFAULT_CHARSET));
                return "success";
            } else {
                System.out.println("命令运行失败!");
                System.out.println("out:"+out.toString(DEFAULT_CHARSET));
                System.out.println("error:"+error.toString(DEFAULT_CHARSET));
            }
            return "out:"+out.toString(DEFAULT_CHARSET)+",error:"+error.toString(DEFAULT_CHARSET);
        }catch (Exception e){
            throw e;
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
    public static int exeCommand(String command, OutputStream out,OutputStream error) throws ExecuteException, IOException {
        CommandLine commandLine = CommandLine.parse(command);
        PumpStreamHandler pumpStreamHandler = null;
        if (null == out) {
            pumpStreamHandler = new PumpStreamHandler();
        } else {
            pumpStreamHandler = new PumpStreamHandler(out,error);
        }

        // 设置超时时间为10秒
        ExecuteWatchdog watchdog = new ExecuteWatchdog(10000);

        DefaultExecutor executor = new DefaultExecutor();
        executor.setExitValues(new int[]{0,1});
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

            String result = exeCommand("cmd.exe /k D:/ping.bat");
            System.out.println(result);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}