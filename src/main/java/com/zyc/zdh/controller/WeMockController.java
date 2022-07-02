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
public class WeMockController extends BaseController{

    @Autowired
    Environment ev;

    @RequestMapping("/wemock_index")
    public String wemock_index() {

        return "wemock/wemock_index";
    }

    @RequestMapping("/wemock_add_index")
    public String wemock_add_index() {

        return "wemock/wemock_add_index";
    }


    @RequestMapping(value = "/wemock_update", method = RequestMethod.POST,produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String wemock_update(String context,String receiver,String subject) {

        try{

            return ReturnInfo.createInfo(RETURN_CODE.SUCCESS.getCode(), "发送成功", "请检查邮箱是否收到发送成功通知,如果5分钟内没由收到邮件,则可能发送邮件失败,请尝试再次发信");
        }catch (Exception e){
            return ReturnInfo.createInfo(RETURN_CODE.FAIL.getCode(), "发送失败", e);
        }
    }

    @RequestMapping(value = "/wemock_add", method = RequestMethod.POST,produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String wemock_add(String context,String receiver,String subject) {

        try{

            return ReturnInfo.createInfo(RETURN_CODE.SUCCESS.getCode(), "发送成功", "请检查邮箱是否收到发送成功通知,如果5分钟内没由收到邮件,则可能发送邮件失败,请尝试再次发信");
        }catch (Exception e){
            return ReturnInfo.createInfo(RETURN_CODE.FAIL.getCode(), "发送失败", e);
        }
    }

    @RequestMapping(value = "/wemock_delete", method = RequestMethod.POST,produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String wemock_delete(String[] id) {

        try{
            return ReturnInfo.createInfo(RETURN_CODE.SUCCESS.getCode(), "发送成功", "请检查邮箱是否收到发送成功通知,如果5分钟内没由收到邮件,则可能发送邮件失败,请尝试再次发信");
        }catch (Exception e){
            return ReturnInfo.createInfo(RETURN_CODE.FAIL.getCode(), "发送失败", e);
        }
    }

}
