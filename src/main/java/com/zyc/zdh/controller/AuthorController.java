package com.zyc.zdh.controller;

import com.zyc.zdh.entity.RETURN_CODE;
import com.zyc.zdh.entity.ReturnInfo;
import com.zyc.zdh.service.JemailService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

@Controller
public class AuthorController extends BaseController{

    @Autowired
    JemailService jemailService;
    @Autowired
    Environment ev;

    @RequestMapping(value = "/mail_compose", method = RequestMethod.GET)
    public String mail_compose() {
        return "admin/mail_compose";
    }

    @RequestMapping(value = "/send_email", method = RequestMethod.POST,produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String send_email(String context,String receiver,String subject) {

        try{
            if(StringUtils.isEmpty(context)){
                throw new Exception("内容不可为空");
            }
            new Thread(new Runnable() {
                @Override
                public void run() {
                    String to=ev.getProperty("spring.mail.username");
                    jemailService.sendHtmlEmail(new String[]{to},subject+":"+receiver,context);
                    if(!StringUtils.isEmpty(receiver))
                        jemailService.sendEmail(new String[]{receiver},"ZDH","系统已将信息通知作者,作者会尽快查看并回复");
                }
            }).start();

            return ReturnInfo.createInfo(RETURN_CODE.SUCCESS.getCode(), "发送成功", "请检查邮箱是否收到发送成功通知,如果5分钟内没由收到邮件,则可能发送邮件失败,请尝试再次发信");
        }catch (Exception e){
            return ReturnInfo.createInfo(RETURN_CODE.FAIL.getCode(), "发送失败", e);
        }
    }

    @RequestMapping(value = "/zdh_download_index", method = RequestMethod.GET)
    public String zdh_dowan() {
        return "other/zdh_download_index";
    }


    @RequestMapping(value = "/zdh_version", method = RequestMethod.POST,produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String zdh_version() {
        try{
            List<String> versions = new ArrayList<>();
            versions.add("4.7.11");
            versions.add("4.7.12");
            versions.add("4.7.13");
            versions.add("4.7.14");
            versions.add("4.7.15");
            versions.add("4.7.16");
            versions.add("4.7.17");
            versions.add("4.7.18");
            versions.add("5.0.0");
            return ReturnInfo.createInfo(RETURN_CODE.SUCCESS.getCode(), "查询成功", versions);
        }catch (Exception e){
            return ReturnInfo.createInfo(RETURN_CODE.FAIL.getCode(), "查询失败", e);
        }
    }

}
