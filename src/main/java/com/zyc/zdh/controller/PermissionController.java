package com.zyc.zdh.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zyc.zdh.dao.*;
import com.zyc.zdh.entity.*;
import com.zyc.zdh.job.JobModel;
import com.zyc.zdh.job.SnowflakeIdWorker;
import com.zyc.zdh.quartz.QuartzManager2;
import com.zyc.zdh.shiro.RedisUtil;
import com.zyc.zdh.util.DateUtil;
import org.apache.shiro.SecurityUtils;
import org.quartz.TriggerUtils;
import org.quartz.impl.triggers.CronTriggerImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
public class PermissionController extends BaseController{

    @Autowired
    ZdhNginxMapper zdhNginxMapper;
    @Autowired
    RedisUtil redisUtil;
    @Autowired
    JdbcTemplate jdbcTemplate;
    @Autowired
    QuartzJobMapper quartzJobMapper;
    @Autowired
    QuartzManager2 quartzManager2;
    @Autowired
    Environment ev;
    @Autowired
    EveryDayNoticeMapper everyDayNoticeMapper;
    @Autowired
    PermissionMapper permissionMapper;
    @Autowired
    ResourceTreeMapper resourceTreeMapper;


    @RequestMapping(value = "/permission_index", method = RequestMethod.GET)
    public String permission_index() {

        return "admin/permission_index";
    }

    @RequestMapping(value = "/user_list", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String user_list(String user_context) {
        System.out.println(user_context);
        List<PermissionUserInfo> users = permissionMapper.findAll(user_context);

        return JSONObject.toJSONString(users);

    }

    @RequestMapping(value = "/permission_add_index", method = RequestMethod.GET)
    public String permission_add_index() {

        return "admin/permission_add_index";
    }

    @RequestMapping(value = "/jstree_add_index", method = RequestMethod.GET)
    public String jstree_add_index() {

        return "admin/jstree_add_index";
    }

    @RequestMapping(value = "/jstree_add_node", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String jstree_add_node(String parent_id,String text,String icon,String url,String order,String level) {
        //{ "id" : "ajson1", "parent" : "#", "text" : "Simple root node" },
        String id = SnowflakeIdWorker.getInstance().nextId()+"";

        ResourceTreeInfo rti=new ResourceTreeInfo();
        rti.setId(id);
        rti.setParent(parent_id);
        rti.setText(text);
        rti.setCreate_time(new Timestamp(new Date().getTime()));
        rti.setUpdate_time(new Timestamp(new Date().getTime()));
        rti.setIs_enable("1");
        rti.setOrder(order);
        rti.setUrl(url);
        rti.setOwner(getUser().getId());
        rti.setIcon(icon);
        rti.setResource_desc("");
        rti.setLevel(level);
        debugInfo(rti);
        resourceTreeMapper.insert(rti);
        return JSON.toJSONString(rti);
    }

    @RequestMapping(value = "/jstree_node", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String jstree_node(String parent_id,String text) {
        //{ "id" : "ajson1", "parent" : "#", "text" : "Simple root node" },
        List<ResourceTreeInfo> rtis=resourceTreeMapper.selectAll();
        rtis.sort(Comparator.comparing(ResourceTreeInfo::getOrderN));

        return JSON.toJSONString(rtis);
    }

    @RequestMapping(value = "/jstree_get_node", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String jstree_get_node(String id,String text) {
        //{ "id" : "ajson1", "parent" : "#", "text" : "Simple root node" },
        return JSON.toJSONString(resourceTreeMapper.selectByPrimaryKey(id));
    }

    @RequestMapping(value = "/jstree_update_node", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String jstree_update_node(ResourceTreeInfo rti) {
        //{ "id" : "ajson1", "parent" : "#", "text" : "Simple root node" },
        rti.setUpdate_time(new Timestamp(new Date().getTime()));
        rti.setCreate_time(null);
        rti.setOwner(getUser().getId());
        debugInfo(rti);
        resourceTreeMapper.updateByPrimaryKey(rti);

        JSONObject json = new JSONObject();

        json.put("success", "200");
        return json.toJSONString();
    }

    @RequestMapping(value = "/jstree_del_node", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String jstree_del_node(String id) {
        //{ "id" : "ajson1", "parent" : "#", "text" : "Simple root node" },
        resourceTreeMapper.deleteByPrimaryKey(id);
        JSONObject json = new JSONObject();

        json.put("success", "200");
        return json.toJSONString();
    }


    @RequestMapping(value = "/jstree_update_parent", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String jstree_update_parent(String id,String parent_id) {
        //{ "id" : "ajson1", "parent" : "#", "text" : "Simple root node" },
        resourceTreeMapper.updateParentById(id,parent_id);
        JSONObject json = new JSONObject();

        json.put("success", "200");
        return json.toJSONString();
    }

    @RequestMapping(value = "/jstree_add_permission")
    @ResponseBody
    public String jstree_add_permission(String id,String[] resource_id) {
        //{ "id" : "ajson1", "parent" : "#", "text" : "Simple root node" },
        System.out.println("======================");
        System.out.println("======================");
        System.out.println("======================");
        System.out.println(Arrays.toString(resource_id));
        debugInfo(resource_id);
        List<UserResourceInfo> uris=new ArrayList<>();
        for(String rid:resource_id){
            UserResourceInfo uri=new UserResourceInfo();
            uri.setUser_id(id);
            uri.setResource_id(rid);
            uri.setCreate_time(new Timestamp(new Date().getTime()));
            uri.setUpdate_time(new Timestamp(new Date().getTime()));
            uris.add(uri);
        }
        resourceTreeMapper.deleteById(id);
        resourceTreeMapper.updateUserResource(uris);
        JSONObject json = new JSONObject();

        json.put("success", "200");
        return json.toJSONString();
    }


    @RequestMapping(value = "/jstree_permission_list")
    @ResponseBody
    public String jstree_permission_list(String id) {
        //{ "id" : "ajson1", "parent" : "#", "text" : "Simple root node" },
        List<UserResourceInfo> uris=new ArrayList<>();

        uris=resourceTreeMapper.selectByUserId(id);

        return JSON.toJSONString(uris);
    }

    @RequestMapping(value = "/jstree_permission_list2", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String jstree_permission_list2() {
        //{ "id" : "ajson1", "parent" : "#", "text" : "Simple root node" },
        List<UserResourceInfo2> uris=new ArrayList<>();
        uris=resourceTreeMapper.selectResourceByUserId(getUser().getId());
        uris.sort(Comparator.comparing(UserResourceInfo2::getOrderN));

        return JSON.toJSONString(uris);
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
