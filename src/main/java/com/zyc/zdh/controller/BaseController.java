package com.zyc.zdh.controller;

import com.zyc.zdh.config.DateConverter;
import com.zyc.zdh.entity.User;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import java.beans.PropertyEditorSupport;
import java.sql.Timestamp;

public class BaseController {

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(Timestamp.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) throws IllegalArgumentException {
                setValue(new DateConverter().convert(text));
            }
        });

    }


    public User getUser() {
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        return user;
    }

    public String getOwner() throws Exception {
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        if(user == null){
            throw new Exception("获取用户失败,请检查用户是否登录");
        }
        return user.getUserName();
    }

    /**
     * 返回 %conditon% 格式
     * @param condition
     * @return
     */
    public String getLikeCondition(String condition){
        return "%"+condition+"%";
    }


    /**
     * 检查参数是否为空,为空直接抛出异常
     * @param param
     * @param name
     * @return
     * @throws Exception
     */
    public boolean checkParam(String param, String name) throws Exception {
        if(StringUtils.isEmpty(param)){
            throw  new Exception(name+"参数不可为空");
        }
        return true;
    }

    public Exception getBaseException(){

        return getBaseException(null);
    }

    public Exception getBaseException(Exception e){
        return e;
    }
}
