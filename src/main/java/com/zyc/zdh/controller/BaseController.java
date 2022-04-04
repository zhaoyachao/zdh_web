package com.zyc.zdh.controller;

import com.zyc.zdh.config.DateConverter;
import com.zyc.zdh.entity.User;
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

    /**
     * 返回 %conditon% 格式
     * @param condition
     * @return
     */
    public String getLikeCondition(String condition){
        return "%"+condition+"%";
    }
}
