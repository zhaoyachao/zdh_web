package com.zyc.zdh.job;

import com.zyc.zdh.util.CommandUtils;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.Date;

public class ShellJobTest {
    @Test
    public void run() throws Exception {

        String[] a={"/bin/sh","-c","[ -f C:/Users/zhaoyachao/Desktop/hive-site.xml ] && echo yes || echo no"};
        String[] b={"c:/Windows/System32/cmd.exe", "ping www.baidu.com"};

        String c="cmd.exe /c if exist d:/postal_fund_app.jar (echo true) else (echo false)";
        String d="dir c:/Users/zhaoyachao/Desktop/monitor.sh";
        System.out.println(CommandUtils.exeCommand(c));


    }
    @Test
    public void run2() throws IOException {
        String a=new Date().getTime()+".sh";
        File file=new File("shell_script/"+ "fdfsdsf"+"/");
        System.out.println(file.getPath());
        System.out.println(file.getParent());
        System.out.println(file.getAbsolutePath());
        System.out.println(file.getName());
        if(!file.exists()){
            file.mkdirs();
        }

        System.out.println(file.getPath());
    }

}