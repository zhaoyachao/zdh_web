package com.zyc.zdh.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zyc.zdh.dao.*;
import com.zyc.zdh.entity.*;
import com.zyc.zdh.job.SnowflakeIdWorker;
import com.zyc.zdh.quartz.QuartzManager2;
import com.zyc.zdh.shiro.RedisUtil;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.util.*;

@Controller
public class NodeController extends BaseController{

    @Autowired
    ZdhNginxMapper zdhNginxMapper;
    @Autowired
    RedisUtil redisUtil;
    @Autowired
    JdbcTemplate jdbcTemplate;
    @Autowired
    QuartzJobMapper quartzJobMapper;
    @Autowired
    ZdhHaInfoMapper zdhHaInfoMapper;
    @Autowired
    Environment ev;



    @RequestMapping(value = "/server_manager_index", method = RequestMethod.GET)
    public String permission_index() {

        return "admin/server_manager_index";
    }

    @RequestMapping(value = "/server_manager_list", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String server_manager_list(String online,String context) {
        //System.out.println(context);
        List<ZdhHaInfo> zdhHaInfos = zdhHaInfoMapper.selectServer(online,context);

        return JSONObject.toJSONString(zdhHaInfos);

    }

    @RequestMapping(value = "/server_manager_update", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String server_manager_update(String id,String online) {
        zdhHaInfoMapper.updateOnline(online,id);

        JSONObject json = new JSONObject();

        json.put("success", "200");
        return json.toJSONString();

    }

    @RequestMapping(value = "/server_add_index", method = RequestMethod.GET)
    public String server_add_index() {

        return "admin/server_add_index";
    }

    @RequestMapping(value = "/server_add", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String server_add(String id,String online) {
        zdhHaInfoMapper.updateOnline(online,id);

        JSONObject json = new JSONObject();

        json.put("success", "200");
        return json.toJSONString();

    }

    public User getUser() {
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        return user;
    }


    private void debugInfo(Object obj) {
        Field[] fields = obj.getClass().getDeclaredFields();
        for (int i = 0, len = fields.length; i < len; i++) {
            // 对于每个属性，获取属性名
            String varName = fields[i].getName();
            try {
                // 获取原来的访问控制权限
                boolean accessFlag = fields[i].isAccessible();
                // 修改访问控制权限
                fields[i].setAccessible(true);
                // 获取在对象f中属性fields[i]对应的对象中的变量
                Object o;
                try {
                    o = fields[i].get(obj);
                    System.err.println("传入的对象中包含一个如下的变量：" + varName + " = " + o);
                } catch (IllegalAccessException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                // 恢复访问控制权限
                fields[i].setAccessible(accessFlag);
            } catch (IllegalArgumentException ex) {
                ex.printStackTrace();
            }
        }
    }



}
