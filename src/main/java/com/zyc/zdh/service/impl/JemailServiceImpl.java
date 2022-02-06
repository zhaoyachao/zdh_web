package com.zyc.zdh.service.impl;

import com.zyc.zdh.service.JemailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

@Service
public class JemailServiceImpl implements JemailService{

    public Logger logger= LoggerFactory.getLogger(this.getClass());
    @Value("${spring.mail.username}")
    private String fromEmail;

    @Autowired
    JavaMailSender javaMailSender;

    @Override
    public void sendEmail(String[] to,String subject, String context) {

        try{
            SimpleMailMessage simpleMailMessage=new SimpleMailMessage();
            simpleMailMessage.setFrom(fromEmail);
            simpleMailMessage.setTo(to);
            simpleMailMessage.setText(context);
            simpleMailMessage.setSubject(subject);

            javaMailSender.send(simpleMailMessage);
        }catch (Exception e){
            String error = "类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常:"+e.getMessage()+", 异常详情:{}";
            logger.error(error, e);
        }
    }

    @Override
    public void sendHtmlEmail(String[] to,String subject, String context) {

        MimeMessage mailMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(mailMessage);
        try {
            messageHelper.setFrom(fromEmail);
            messageHelper.setTo(to);
            messageHelper.setText("<html><head></head><body>"+context+"</body></html>",true);
            messageHelper.setSubject(subject);
            javaMailSender.send(mailMessage);
        } catch (MessagingException e) {
            String error = "类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常:"+e.getMessage()+", 异常详情:{}";
            logger.error(error, e);
        }

    }

}
