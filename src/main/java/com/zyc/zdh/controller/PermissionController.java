package com.zyc.zdh.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zyc.zdh.dao.*;
import com.zyc.zdh.entity.*;
import com.zyc.zdh.job.SnowflakeIdWorker;
import com.zyc.zdh.quartz.QuartzManager2;
import com.zyc.zdh.shiro.RedisUtil;
import com.zyc.zdh.util.Const;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import tk.mybatis.mapper.entity.Example;

import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.util.*;

/**
 * 权限节点相关
 */
@Controller
public class PermissionController extends BaseController {

    public Logger logger = LoggerFactory.getLogger(this.getClass());

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

    @Autowired
    RoleDao roleDao;

    @Autowired
    UserGroupMapper userGroupMapper;

    @Autowired
    DataTagGroupMapper dataTagGroupMapper;


    @RequestMapping(value = "/permission_index", method = RequestMethod.GET)
    public String permission_index() {

        return "admin/permission_index";
    }

    @RequestMapping(value = "/user_list", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String user_list(String user_context) {
        if(!StringUtils.isEmpty(user_context)){
            user_context = getLikeCondition(user_context);
        }
        List<PermissionUserInfo> users = permissionMapper.findAll(user_context);

        return JSONObject.toJSONString(users);
    }

    @RequestMapping(value = "/user_enable", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public String user_enable(String[] ids, String enable) {

        try {
            int result = permissionMapper.updateEnable(ids, enable);
            return ReturnInfo.createInfo(RETURN_CODE.SUCCESS.getCode(), "更新成功", null);
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ReturnInfo.createInfo(RETURN_CODE.FAIL.getCode(), "更新失败", e);
        }

    }

    @RequestMapping(value = "/permission_add_index", method = RequestMethod.GET)
    public String permission_add_index() {

        return "admin/permission_add_index";
    }

    @RequestMapping(value = "/user_add_index", method = RequestMethod.GET)
    public String user_add_index() {

        return "admin/user_add_index";
    }

    @RequestMapping(value = "/user_detail", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String user_detail(String id) {
        try {
            PermissionUserInfo user = permissionMapper.selectByPrimaryKey(id);
            user.setPassword("");
            return ReturnInfo.createInfo(RETURN_CODE.SUCCESS.getCode(), "查询成功", user);
        } catch (Exception e) {
            String error = "类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}";
            logger.error(error, e);
            return ReturnInfo.createInfo(RETURN_CODE.FAIL.getCode(), "查询失败", e);
        }
    }

    @RequestMapping(value = "/user_update", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public String user_update(PermissionUserInfo user) {
        try {

            if (user.getId().equalsIgnoreCase("-1")) {
                //新增用户
                if (StringUtils.isEmpty(user.getPassword())) {
                    throw new Exception("新增用户密码不可为空");
                }
                user.setEnable("false");
                user.setId(null);
                permissionMapper.insert(user);
            } else {
                PermissionUserInfo pui = permissionMapper.selectByPrimaryKey(user.getId());
                if (user.getPassword().equalsIgnoreCase("")) {
                    user.setPassword(pui.getPassword());
                    user.setEnable(pui.getEnable());
                }
                permissionMapper.updateByPrimaryKey(user);
            }
            return ReturnInfo.createInfo(RETURN_CODE.SUCCESS.getCode(), "更新成功", null);
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            logger.error("类:" + Thread.currentThread().getStackTrace()[1].getClassName() + " 函数:" + Thread.currentThread().getStackTrace()[1].getMethodName() + " 异常: {}" + e);
            return ReturnInfo.createInfo(RETURN_CODE.FAIL.getCode(), "更新失败", e);
        }
    }

    @RequestMapping(value = "/user_group_add_index", method = RequestMethod.GET)
    public String user_group_add_index() {

        return "admin/user_group_add_index";
    }

    @RequestMapping(value = "/user_group_add", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public String user_group_add(UserGroupInfo ugi) {
        try {

            List<UserGroupInfo> ugis = userGroupMapper.select(ugi);
            if (ugis != null && ugis.size() > 0) {
                throw new Exception("组名已经存在");
            }
            ugi.setEnable("true");
            ugi.setCreate_time(new Timestamp(new Date().getTime()));
            ugi.setUpdate_time(new Timestamp(new Date().getTime()));
            userGroupMapper.insert(ugi);
            return ReturnInfo.createInfo(RETURN_CODE.SUCCESS.getCode(), "新增成功", null);
        } catch (Exception e) {
            logger.error("类:" + Thread.currentThread().getStackTrace()[1].getClassName() + " 函数:" + Thread.currentThread().getStackTrace()[1].getMethodName() + " 异常: {}" + e);
            return ReturnInfo.createInfo(RETURN_CODE.FAIL.getCode(), "新增失败", e.getMessage());
        }
    }

    @RequestMapping(value = "/user_group_list", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public String user_group_list(String enable) {
        try {
            UserGroupInfo ugi = new UserGroupInfo();
            ugi.setEnable(enable);
            List<UserGroupInfo> ugis = userGroupMapper.select(ugi);
            return ReturnInfo.createInfo(RETURN_CODE.SUCCESS.getCode(), "查询成功", ugis);
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            logger.error("类:" + Thread.currentThread().getStackTrace()[1].getClassName() + " 函数:" + Thread.currentThread().getStackTrace()[1].getMethodName() + " 异常: {}" + e);
            return ReturnInfo.createInfo(RETURN_CODE.FAIL.getCode(), "查询失败", e);
        }
    }

    @RequestMapping(value = "/role_index", method = RequestMethod.GET)
    public String role_index() {

        return "admin/role_index";
    }

    @RequestMapping(value = "/role_add_index", method = RequestMethod.GET)
    public String role_add_index() {

        return "admin/role_add_index";
    }

    @RequestMapping(value = "/role_list", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String role_list(String role_context, String enable) {
        if(!StringUtils.isEmpty(role_context)){
            role_context = getLikeCondition(role_context);
        }
        List<RoleInfo> users = roleDao.selectByContext(role_context, enable);
        return JSONObject.toJSONString(users);
    }

    @RequestMapping(value = "/role_enable", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public String role_enable(String[] ids, String enable) {

        try {
            int result = roleDao.updateEnable(ids, enable);
            return ReturnInfo.createInfo(RETURN_CODE.SUCCESS.getCode(), "更新成功", null);
        } catch (Exception e) {
            String error = "类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}";
            logger.error(error, e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ReturnInfo.createInfo(RETURN_CODE.FAIL.getCode(), "更新失败", e);
        }

    }

    @RequestMapping(value = "/role_detail", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String role_detail(String id) {
        try {
            RoleInfo role = roleDao.selectByPrimaryKey(id);
            return ReturnInfo.createInfo(RETURN_CODE.SUCCESS.getCode(), "查询成功", role);
        } catch (Exception e) {
            String error = "类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}";
            logger.error(error, e);
            return ReturnInfo.createInfo(RETURN_CODE.FAIL.getCode(), "查询失败", e);
        }
    }

    @RequestMapping(value = "/jstree_add_index", method = RequestMethod.GET)
    public String jstree_add_index() {

        return "admin/jstree_add_index";
    }

    @RequestMapping(value = "/jstree_add_node", method = RequestMethod.POST,produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String jstree_add_node(ResourceTreeInfo rti) {
        //{ "id" : "ajson1", "parent" : "#", "text" : "Simple root node" },
        try {
            if (rti.getNotice_title()!=null && rti.getNotice_title().length() > 4) {
                return ReturnInfo.createInfo(RETURN_CODE.FAIL.getCode(), "参数验证不通过-提示语长度不可超过4个汉字", null);
            }
            if (org.apache.commons.lang3.StringUtils.isEmpty(rti.getProduct_code())) {
                return ReturnInfo.createInfo(RETURN_CODE.FAIL.getCode(), "产品代码不可为空", null);
            }
            String id = SnowflakeIdWorker.getInstance().nextId() + "";
            rti.setId(id);
            rti.setCreate_time(new Timestamp(new Date().getTime()));
            rti.setUpdate_time(new Timestamp(new Date().getTime()));
            rti.setIs_enable(Const.ENABLE);
            rti.setOwner(getUser().getId());
            rti.setResource_desc("");
            debugInfo(rti);
            resourceTreeMapper.insert(rti);
            return ReturnInfo.createInfo(RETURN_CODE.SUCCESS.getCode(), RETURN_CODE.SUCCESS.getDesc(), null);
        } catch (Exception e) {
            String error = "类:" + Thread.currentThread().getStackTrace()[1].getClassName() + " 函数:" + Thread.currentThread().getStackTrace()[1].getMethodName() + " 异常: {}";
            logger.error(error, e);
            return ReturnInfo.createInfo(RETURN_CODE.FAIL.getCode(), e.getMessage(), null);
        }


    }

    @RequestMapping(value = "/jstree_node",method = RequestMethod.POST,produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String jstree_node(String parent_id, String text,String product_code) {
        //{ "id" : "ajson1", "parent" : "#", "text" : "Simple root node" },
        if(org.apache.commons.lang3.StringUtils.isEmpty(product_code)){
            return ReturnInfo.createInfo("201","产品参数不可为空","产品参数不可为空");
        }
        Example example=new Example(ResourceTreeInfo.class);
        Example.Criteria criteria=example.createCriteria();
        criteria.andEqualTo("product_code", product_code);

        List<ResourceTreeInfo> rtis = resourceTreeMapper.selectByExample(example);
        rtis.sort(Comparator.comparing(ResourceTreeInfo::getOrderN));
        return ReturnInfo.createInfo("200","查询成功",rtis);
    }

    @RequestMapping(value = "/jstree_get_node",method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String jstree_get_node(String id, String text) {
        //{ "id" : "ajson1", "parent" : "#", "text" : "Simple root node" },
        return JSON.toJSONString(resourceTreeMapper.selectByPrimaryKey(id));
    }

    @RequestMapping(value = "/jstree_update_node", method = RequestMethod.POST,produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String jstree_update_node(ResourceTreeInfo rti) {
        //{ "id" : "ajson1", "parent" : "#", "text" : "Simple root node" },
        try {
            rti.setUpdate_time(new Timestamp(new Date().getTime()));
            rti.setCreate_time(null);
            rti.setOwner(getUser().getId());
            rti.setIs_enable(Const.ENABLE);
            rti.setResource_desc("");
            debugInfo(rti);
            resourceTreeMapper.updateByPrimaryKey(rti);

            return ReturnInfo.createInfo(RETURN_CODE.SUCCESS.getCode(), RETURN_CODE.SUCCESS.getDesc(), null);
        } catch (Exception e) {
            String error = "类:" + Thread.currentThread().getStackTrace()[1].getClassName() + " 函数:" + Thread.currentThread().getStackTrace()[1].getMethodName() + " 异常: {}";
            logger.error(error, e);
            return ReturnInfo.createInfo(RETURN_CODE.FAIL.getCode(), e.getMessage(), null);
        }

    }

    @RequestMapping(value = "/jstree_del_node", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String jstree_del_node(String id) {
        //{ "id" : "ajson1", "parent" : "#", "text" : "Simple root node" },
        try {
            resourceTreeMapper.deleteByPrimaryKey(id);
            return ReturnInfo.createInfo(RETURN_CODE.SUCCESS.getCode(), RETURN_CODE.SUCCESS.getDesc(), null);
        } catch (Exception e) {
            String error = "类:" + Thread.currentThread().getStackTrace()[1].getClassName() + " 函数:" + Thread.currentThread().getStackTrace()[1].getMethodName() + " 异常: {}";
            logger.error(error, e);
            return ReturnInfo.createInfo(RETURN_CODE.FAIL.getCode(), e.getMessage(), null);
        }

    }


    @RequestMapping(value = "/jstree_update_parent", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String jstree_update_parent(String id, String parent_id, String level) {
        //{ "id" : "ajson1", "parent" : "#", "text" : "Simple root node" },
        try {
            resourceTreeMapper.updateParentById(id, parent_id, level);
            return ReturnInfo.createInfo(RETURN_CODE.SUCCESS.getCode(), RETURN_CODE.SUCCESS.getDesc(), null);
        } catch (Exception e) {
            String error = "类:" + Thread.currentThread().getStackTrace()[1].getClassName() + " 函数:" + Thread.currentThread().getStackTrace()[1].getMethodName() + " 异常: {}";
            logger.error(error, e);
            return ReturnInfo.createInfo(RETURN_CODE.FAIL.getCode(), e.getMessage(), null);
        }

    }

    @RequestMapping(value = "/jstree_add_permission", method = RequestMethod.POST)
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public String jstree_add_permission(String id, String[] resource_id, String code, String name,String product_code) {
        //{ "id" : "ajson1", "parent" : "#", "text" : "Simple root node" },
        try {
            if (id.equalsIgnoreCase("-1")) {
                id = SnowflakeIdWorker.getInstance().nextId() + "";
                //新增角色
                RoleInfo role = new RoleInfo();
                role.setCode(code);
                role.setName(name);
                role.setId(id);
                role.setProduct_code(product_code);
                roleDao.insert(role);
            }
            debugInfo(resource_id);
            List<RoleResourceInfo> rris = new ArrayList<>();
            for (String rid : resource_id) {
                RoleResourceInfo rri = new RoleResourceInfo();
                rri.setRole_id(id);
                rri.setResource_id(rid);
                rri.setCreate_time(new Timestamp(new Date().getTime()));
                rri.setUpdate_time(new Timestamp(new Date().getTime()));
                rris.add(rri);
            }
            resourceTreeMapper.deleteById(id);
            resourceTreeMapper.updateUserResource(rris);
            return ReturnInfo.createInfo(RETURN_CODE.SUCCESS.getCode(), RETURN_CODE.SUCCESS.getDesc(), null);
        } catch (Exception e) {
            String error = "类:" + Thread.currentThread().getStackTrace()[1].getClassName() + " 函数:" + Thread.currentThread().getStackTrace()[1].getMethodName() + " 异常: {}";
            logger.error(error, e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ReturnInfo.createInfo(RETURN_CODE.FAIL.getCode(), e.getMessage(), null);
        }
    }


    @RequestMapping(value = "/jstree_permission_list", method = RequestMethod.POST)
    @ResponseBody
    public String jstree_permission_list(String id) {
        //{ "id" : "ajson1", "parent" : "#", "text" : "Simple root node" },
        List<RoleResourceInfo> uris = new ArrayList<>();

        uris = resourceTreeMapper.selectByUserId(id);

        return JSON.toJSONString(uris);
    }

    @RequestMapping(value = "/jstree_permission_list2", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String jstree_permission_list2() {
        //{ "id" : "ajson1", "parent" : "#", "text" : "Simple root node" },
        List<UserResourceInfo2> uris = new ArrayList<>();
        uris = resourceTreeMapper.selectResourceByUserId(getUser().getId());
        uris.sort(Comparator.comparing(UserResourceInfo2::getOrderN));

        return JSON.toJSONString(uris);
    }


    @RequestMapping(value = "/user_index", method = RequestMethod.GET)
    public String user_index() {

        return "admin/user_index";
    }

    @RequestMapping(value = "/user_tag_group_code", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String user_tag_group_code() {
        try {
            String tag_group_code = getUser().getTag_group_code();

            if(org.apache.commons.lang3.StringUtils.isEmpty(tag_group_code)){
                return ReturnInfo.createInfo(RETURN_CODE.SUCCESS.getCode(), "查询成功", new ArrayList<DataTagGroupInfo>());
            }
            Example example=new Example(DataTagGroupInfo.class);
            Example.Criteria criteria=example.createCriteria();
            criteria.andEqualTo("is_delete",Const.NOT_DELETE);
            criteria.andIn("tag_group_code", Arrays.asList(tag_group_code.split(",")));

            List<DataTagGroupInfo> dataTagGroupInfos = dataTagGroupMapper.selectByExample(example);

            return ReturnInfo.createInfo(RETURN_CODE.SUCCESS.getCode(), "查询成功", dataTagGroupInfos);
        } catch (Exception e) {
            String error = "类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}";
            logger.error(error, e);
            return ReturnInfo.createInfo(RETURN_CODE.FAIL.getCode(), "查询失败", e);
        }
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
                    logger.error("类:" + Thread.currentThread().getStackTrace()[1].getClassName() + " 函数:" + Thread.currentThread().getStackTrace()[1].getMethodName() + " 异常: {}" + e);
                }
                // 恢复访问控制权限
                fields[i].setAccessible(accessFlag);
            } catch (IllegalArgumentException e) {
                logger.error("类:" + Thread.currentThread().getStackTrace()[1].getClassName() + " 函数:" + Thread.currentThread().getStackTrace()[1].getMethodName() + " 异常: {}" + e);
            }
        }
    }


}
