package com.zyc.zdh.service.impl;

import com.zyc.zdh.service.JemailService;
import com.zyc.zdh.util.LogUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
public class JemailServiceImpl implements JemailService{


    @Value("${spring.mail.username}")
    private String fromEmail;

    @Autowired
    private JavaMailSender javaMailSender;

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
            LogUtil.error(this.getClass(), e);
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
            LogUtil.error(this.getClass(), e);
        }

    }

}
