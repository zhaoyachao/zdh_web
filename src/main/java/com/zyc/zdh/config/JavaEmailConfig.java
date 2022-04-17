package com.zyc.zdh.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import java.util.Properties;


@Configuration
public class JavaEmailConfig {

    @Value("${spring.mail.host}")
    private String host;
    @Value("${spring.mail.username}")
    private String username;
    @Value("${spring.mail.password}")
    private String password;
    @Value("${spring.mail.port}")
    private String port;


    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Bean(name = "javaMailSender")
    public JavaMailSender getSender(){
        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
        javaMailSender.setUsername(username);
        javaMailSender.setHost(host);
        javaMailSender.setPort(Integer.parseInt(port));//①
        javaMailSender.setDefaultEncoding("UTF-8");
        Properties props = new Properties();//②
        props.setProperty("mail.smtp.host", host);
        props.setProperty("mail.smtp.auth", "true");
        javax.mail.Session session = javax.mail.Session.getDefaultInstance(props,new Authenticator(){
            @Override
            protected PasswordAuthentication getPasswordAuthentication(){
                return new PasswordAuthentication(username,password);
            }
        });
        javaMailSender.setSession(session);//③
        return javaMailSender;
    }

}
