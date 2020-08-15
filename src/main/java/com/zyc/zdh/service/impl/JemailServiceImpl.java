package com.zyc.zdh.service.impl;

import com.zyc.zdh.service.JemailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Properties;

@Service
public class JemailServiceImpl implements JemailService{

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Autowired
    JavaMailSender javaMailSender;

    @Override
    public void sendEmail(String[] to,String subject, String context) {

        SimpleMailMessage simpleMailMessage=new SimpleMailMessage();
        simpleMailMessage.setFrom(fromEmail);
        simpleMailMessage.setTo(to);
        simpleMailMessage.setText(context);
        simpleMailMessage.setSubject(subject);

        javaMailSender.send(simpleMailMessage);

    }
}
