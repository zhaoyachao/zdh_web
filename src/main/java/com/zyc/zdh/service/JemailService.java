package com.zyc.zdh.service;

public interface JemailService {

    public void sendEmail(String[] to,String subject,String context);
    public void sendHtmlEmail(String[] to,String subject,String context);
}
