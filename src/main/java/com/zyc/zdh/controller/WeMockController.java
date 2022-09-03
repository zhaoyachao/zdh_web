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

/**
 * mock数据服务
 */
@Controller
public class WeMockController extends BaseController{

    @Autowired
    Environment ev;

    /**
     * mock数据首页
     * @return
     */
    @RequestMapping("/wemock_index")
    public String wemock_index() {

        return "wemock/wemock_index";
    }

    /**
     * mock数据新增页面
     * @return
     */
    @RequestMapping("/wemock_add_index")
    public String wemock_add_index() {

        return "wemock/wemock_add_index";
    }


    /**
     * mock数据更新
     * @param context
     * @param receiver
     * @param subject
     * @return
     */
    @RequestMapping(value = "/wemock_update", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo wemock_update(String context,String receiver,String subject) {

        try{

            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "发送成功", "请检查邮箱是否收到发送成功通知,如果5分钟内没由收到邮件,则可能发送邮件失败,请尝试再次发信");
        }catch (Exception e){
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "发送失败", e);
        }
    }

    /**
     * mock数据新增
     * @param context
     * @param receiver
     * @param subject
     * @return
     */
    @RequestMapping(value = "/wemock_add", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo wemock_add(String context,String receiver,String subject) {

        try{

            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "发送成功", "请检查邮箱是否收到发送成功通知,如果5分钟内没由收到邮件,则可能发送邮件失败,请尝试再次发信");
        }catch (Exception e){
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "发送失败", e);
        }
    }

    /**
     * mock数据删除
     * @param id 主键ID
     * @return
     */
    @RequestMapping(value = "/wemock_delete", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo wemock_delete(String[] id) {

        try{
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "发送成功", "请检查邮箱是否收到发送成功通知,如果5分钟内没由收到邮件,则可能发送邮件失败,请尝试再次发信");
        }catch (Exception e){
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "发送失败", e);
        }
    }

}
