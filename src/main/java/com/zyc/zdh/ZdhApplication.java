package com.zyc.zdh;


import com.zyc.zdh.annotation.MyMark;
import com.zyc.zdh.controller.LoginController;
import com.zyc.zdh.run.SystemCommandLineRunner;
import com.zyc.zdh.util.ConfigUtil;
import com.zyc.zdh.util.DateUtil;
import com.zyc.zdh.util.LogUtil;
import org.apache.commons.lang3.StringUtils;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.FilterType;
import org.springframework.core.env.Environment;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;


@ComponentScan(basePackages = {"com.zyc.zdh"}, includeFilters = {@Filter(type = FilterType.ANNOTATION, value = MyMark.class)})
@MapperScan(basePackages = {"com.zyc.zdh.dao"})
@SpringBootApplication()
@EnableAspectJAutoProxy(exposeProxy = true, proxyTargetClass = true)
//@EnableAspectJAutoProxy exposeProxy:暴露代理对象,proxyTargetClass强制使用cglib代理
public class ZdhApplication {

    public static String start_time = DateUtil.getCurrentTime();

    public static void main(String[] args) throws SocketException {
        //检查必须配置的环境变量
        String run_mode = System.getenv("ZDH_RUN_MODE");
        if(StringUtils.isEmpty(run_mode)){
            System.err.println("启动项目前必须配置ZDH_RUN_MODE环境变量,变量值同spring.active.profile值保持一致, linux: export ZDH_RUN_MODE=prod, windows: set ZDH_RUN_MODE=prod");
            System.exit(-1);
        }

        if(!isJdk18()){
            System.err.println("项目依赖JDK版本1.8");
            System.exit(-1);
        }

        SpringApplication springApplication = new SpringApplication(ZdhApplication.class);
        ApplicationContext context = springApplication.run(args);
        LoginController.context = context;

        Environment env = context.getEnvironment();
        String version = env.getProperty(ConfigUtil.VERSION);
        String envPort = env.getProperty(ConfigUtil.SERVER_PORT);
        String envContext = env.getProperty(ConfigUtil.SERVER_SERVLET_CONTEXT_PATH, "");
        String port = envPort == null ? "8080" : envPort;
        String line = System.lineSeparator();
        String url = line +"Version:" + version +line;
        url = url + "Application Id:[" + SystemCommandLineRunner.web_application_id +"]"+ line;
        url = url + "Access URLs:" + line + "----------------------------------------------------------"+line;
        for (String host: getIpAddress()){
            url = url+String.format("web-URL: \t\thttp://%s:%s%s/login", host,port, envContext)+line;
        }

        LogUtil.info(ZdhApplication.class, url + line + "----------------------------------------------------------");
    }

    private static List<String> getIpAddress() throws SocketException {
        List<String> list = new LinkedList<>();
        Enumeration enumeration = NetworkInterface.getNetworkInterfaces();
        while (enumeration.hasMoreElements()) {
            NetworkInterface network = (NetworkInterface) enumeration.nextElement();
            if (network.isVirtual() || !network.isUp()) {
                continue;
            } else {
                Enumeration addresses = network.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    InetAddress address = (InetAddress) addresses.nextElement();
                    if (address != null && (address instanceof Inet4Address)) {
                        list.add(address.getHostAddress());
                    }
                }
            }
        }
        return list;
    }

    /**
     * 判断jdk 是否1.8
     * @return
     */
    public static boolean isJdk18() {
        String javaVersion = System.getProperty("java.version");
        // JDK 1.8版本格式："1.8.0" 或 "1.8.0_xxx"（xxx为补丁号）
        return Pattern.matches("1\\.8(\\.0)?(_\\d+)?", javaVersion);
    }
}