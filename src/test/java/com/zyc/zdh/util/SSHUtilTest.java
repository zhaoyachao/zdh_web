package com.zyc.zdh.util;

import com.jcraft.jsch.JSchException;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

public class SSHUtilTest {

    @Test
    public void exec() throws IOException, JSchException {

        String username="zyc";
        String password="123456";
        String host="192.168.110.10";
        String port="22";
        SSHUtil sshUtil = new SSHUtil(username, password, host, Integer.parseInt(port));
        sshUtil.login();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(20000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("开始关闭");

            }
        }).start();
       //String[] result = sshUtil.exec("sh /home/zyc/online.sh");
        String result=SshUtils.exec("ssh://zyc:123456@192.168.110.10/home/zyc","sh /home/zyc/online.sh");

    }

    @Test
    public void e2(){



        String result=SshUtils.exec("ssh://zyc:123456@192.168.110.10/home/zyc", "kill -9 `ps -ef |grep 'sh /home/zyc/online.sh' |awk -F \" \" '{print $2}'`");
        System.out.println(result.split("\n")[0]);
    }
}