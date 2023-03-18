package com.zyc.zdh;


import com.zyc.zdh.annotation.MyMark;
import com.zyc.zdh.controller.LoginController;
import com.zyc.zdh.util.DateUtil;
import org.assertj.core.util.Compatibility;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.FilterType;
import org.springframework.core.env.Environment;

import java.net.*;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;


@ComponentScan(basePackages = {"com.zyc.zdh"}, includeFilters = {@Filter(type = FilterType.ANNOTATION, value = MyMark.class)})
@MapperScan(basePackages = {"com.zyc.zdh.dao"})
@SpringBootApplication
@EnableAspectJAutoProxy(exposeProxy = true, proxyTargetClass = true)
//@EnableAspectJAutoProxy exposeProxy:暴露代理对象,proxyTargetClass强制使用cglib代理
public class ZdhApplication {
    public static Logger logger = LoggerFactory.getLogger(ZdhApplication.class);

    public static String start_time = DateUtil.getCurrentTime();

    public static void main(String[] args) throws SocketException {
        SpringApplication springApplication = new SpringApplication(ZdhApplication.class);
        ApplicationContext context = springApplication.run(args);
        LoginController.context = context;
        Environment env = context.getEnvironment();
        String envPort = env.getProperty("server.port");
        String envContext = env.getProperty("server.context-path", "");
        String port = envPort == null ? "8080" : envPort;
        String line = Compatibility.System.lineSeparator();
        String url = "Access URLs:" + line + "----------------------------------------------------------"+line;
        for (String host: getIpAddress()){
            url = url+String.format("web-URL: \t\thttp://%s:%s%s/login", host,port, envContext)+line;
        }
        logger.info(url+line+"----------------------------------------------------------");
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
}