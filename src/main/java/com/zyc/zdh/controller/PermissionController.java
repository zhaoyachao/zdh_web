package com.zyc.zdh.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonObject;
import com.zyc.zdh.annotation.White;
import com.zyc.zdh.dao.*;
import com.zyc.zdh.entity.*;
import com.zyc.zdh.job.SnowflakeIdWorker;
import com.zyc.zdh.quartz.QuartzManager2;
import com.zyc.zdh.shiro.RedisUtil;
import com.zyc.zdh.util.Const;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
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
 * 权限服务
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
    @Autowired
    PermissionApplyMapper permissionApplyMapper;
    @Autowired
    ZdhProcessFlowController zdhProcessFlowController;
    @Autowired
    PermissionBigdataMapper permissionBigdataMapper;

    @Autowired
    ProductTagMapper productTagMapper;

    @Autowired
    EnumMapper enumMapper;

    /**
     * 权限首页(废弃)
     * @return
     */
    @RequestMapping(value = "/permission_index", method = RequestMethod.GET)
    public String permission_index() {

        return "admin/permission_index";
    }

    /**
     * 权限用户列表
     * @param product_code 产品代码
     * @param user_context 关键字
     * @return
     */
    @RequestMapping(value = "/user_list", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<List<PermissionUserInfo>> user_list(String product_code,String user_context) {
        List<PermissionUserInfo> result = new ArrayList<>();
        if(StringUtils.isEmpty(product_code)){
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "产品代码不可为空", result);
        }

        Example example=new Example(PermissionUserInfo.class);
        Example.Criteria criteria=example.createCriteria();

        criteria.andEqualTo("product_code", product_code);

        if(!StringUtils.isEmpty(user_context)){
            user_context = getLikeCondition(user_context);
            Example.Criteria criteria2=example.createCriteria();
            criteria2.andLike("user_account", user_context);
            criteria2.orLike("user_name", user_context);
            criteria2.orLike("email", user_context);
            criteria2.orLike("phone", user_context);
            criteria2.orLike("signature", user_context);
            criteria2.orLike("tag_group_code", user_context);
            example.and(criteria2);
        }

        List<PermissionUserInfo> users = permissionMapper.selectByExample(example);

        return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "查询成功", users);
    }

    /**
     * 启用/禁用用户
     * @param ids 用户ID数组
     * @param enable true/false
     * @return
     */
    @RequestMapping(value = "/user_enable", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public ReturnInfo<Object> user_enable(String[] ids, String enable) {

        try {
            int result = permissionMapper.updateEnable(ids, enable);
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "更新成功", getBaseException());
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            String error = "类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}";
            logger.error(error, e);
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "更新失败", getBaseException(e));
        }

    }

    /**
     * 菜单资源权限配置页面
     * @return
     */
    @RequestMapping(value = "/permission_add_index", method = RequestMethod.GET)
    public String permission_add_index() {

        return "admin/permission_add_index";
    }

    /**
     * 新增用户页面
     * @return
     */
    @RequestMapping(value = "/user_add_index", method = RequestMethod.GET)
    public String user_add_index() {

        return "admin/user_add_index";
    }

    /**
     * 查询权限用户明细
     * @param id 主键ID
     * @return
     */
    @RequestMapping(value = "/user_detail", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<PermissionUserInfo> user_detail(String id) {
        try {
            PermissionUserInfo user = permissionMapper.selectByPrimaryKey(id);
            user.setUser_password("");
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "查询成功", user);
        } catch (Exception e) {
            String error = "类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}";
            logger.error(error, e);
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "查询失败", getBaseException(e));
        }
    }

    /**
     * 权限用户信息更新
     * @param user
     * @return
     */
    @RequestMapping(value = "/user_update", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public ReturnInfo<Object> user_update(PermissionUserInfo user) {
        try {

            if (user.getId().equalsIgnoreCase("-1")) {
                //新增用户
                if (StringUtils.isEmpty(user.getUser_password())) {
                    throw new Exception("新增用户密码不可为空");
                }
                user.setEnable(Const.FALSE);
                user.setId(null);
                permissionMapper.insert(user);
            } else {
                PermissionUserInfo pui = permissionMapper.selectByPrimaryKey(user.getId());
                if (user.getUser_password().equalsIgnoreCase("")) {
                    user.setUser_password(pui.getUser_password());
                    user.setEnable(pui.getEnable());
                }
                permissionMapper.updateByPrimaryKey(user);
                //
            }
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "更新成功", getBaseException());
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            logger.error("类:" + Thread.currentThread().getStackTrace()[1].getClassName() + " 函数:" + Thread.currentThread().getStackTrace()[1].getMethodName() + " 异常: {}" , e);
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "更新失败", getBaseException(e));
        }
    }

    /**
     * 新增用户组页面
     * @return
     */
    @RequestMapping(value = "/user_group_add_index", method = RequestMethod.GET)
    public String user_group_add_index() {

        return "admin/user_group_add_index";
    }

    /**
     * 新增用户组
     * @param ugi
     * @return
     */
    @RequestMapping(value = "/user_group_add", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public ReturnInfo<Object> user_group_add(UserGroupInfo ugi) {
        try {

            checkParam(ugi.getProduct_code(), "product_code");

            List<UserGroupInfo> ugis = userGroupMapper.select(ugi);
            if (ugis != null && ugis.size() > 0) {
                throw new Exception("组名已经存在");
            }
            if(ugi.getId().equalsIgnoreCase("-1")){
                ugi.setId(null);
            }
            ugi.setEnable("true");
            ugi.setCreate_time(new Timestamp(new Date().getTime()));
            ugi.setUpdate_time(new Timestamp(new Date().getTime()));
            userGroupMapper.insert(ugi);
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "新增成功", null);
        } catch (Exception e) {
            logger.error("类:" + Thread.currentThread().getStackTrace()[1].getClassName() + " 函数:" + Thread.currentThread().getStackTrace()[1].getMethodName() + " 异常: {}" , e);
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "新增失败", e);
        }
    }

    /**
     *
     * @param enable 是否启用true/false
     * @param product_code 产品代码
     * @return
     */
    @RequestMapping(value = "/user_group_list", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public ReturnInfo<List<UserGroupInfo>> user_group_list(String enable, String product_code) {
        try {
            checkParam(product_code, "product_code");
            UserGroupInfo ugi = new UserGroupInfo();
            ugi.setEnable(enable);
            ugi.setProduct_code(product_code);
            List<UserGroupInfo> ugis = userGroupMapper.select(ugi);
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "查询成功", ugis);
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            logger.error("类:" + Thread.currentThread().getStackTrace()[1].getClassName() + " 函数:" + Thread.currentThread().getStackTrace()[1].getMethodName() + " 异常: {}" , e);
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "查询失败", e);
        }
    }

    /**
     * 角色首页
     * @return
     */
    @RequestMapping(value = "/role_index", method = RequestMethod.GET)
    public String role_index() {

        return "admin/role_index";
    }

    /**
     * 新增角色页面
     * @return
     */
    @RequestMapping(value = "/role_add_index", method = RequestMethod.GET)
    public String role_add_index() {

        return "admin/role_add_index";
    }

    /**
     * 角色列表
     * @param role_context 关键字
     * @param enable 是否启用true/false
     * @param product_code 产品代码
     * @return
     */
    @RequestMapping(value = "/role_list", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public List<RoleInfo> role_list(String role_context, String enable, String product_code) {
        if(!StringUtils.isEmpty(role_context)){
            role_context = getLikeCondition(role_context);
        }
        List<RoleInfo> users = roleDao.selectByContext(role_context, enable, product_code);
        return users;
    }

    /**
     * 角色批量启用
     * @param ids id数组
     * @param enable 是否启用 true/false
     * @return
     */
    @RequestMapping(value = "/role_enable", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public ReturnInfo<Object> role_enable(String[] ids, String enable) {

        try {
            int result = roleDao.updateEnable(ids, enable);
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "更新成功", getBaseException());
        } catch (Exception e) {
            String error = "类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}";
            logger.error(error, e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "更新失败", getBaseException(e));
        }

    }

    /**
     * 角色明细
     * @param id 主键ID
     * @return
     */
    @RequestMapping(value = "/role_detail", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<RoleInfo> role_detail(String id) {
        try {
            RoleInfo role = roleDao.selectByPrimaryKey(id);
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "查询成功", role);
        } catch (Exception e) {
            String error = "类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}";
            logger.error(error, e);
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "查询失败", getBaseException(e));
        }
    }

    /**
     * 根据角色code查询角色信息
     * @param code 角色code
     * @param product_code 产品代码
     * @return
     */
    @RequestMapping(value = "/role_by_code", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<RoleInfo> role_by_code(String code, String product_code) {
        try {
            RoleInfo roleInfo=new RoleInfo();
            roleInfo.setProduct_code(product_code);
            roleInfo.setCode(code);
            roleInfo = roleDao.selectOne(roleInfo);
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "查询成功", roleInfo);
        } catch (Exception e) {
            String error = "类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}";
            logger.error(error, e);
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "查询失败", getBaseException(e));
        }
    }

    /**
     * 资源树页面
     * @return
     */
    @RequestMapping(value = "/jstree_add_index", method = RequestMethod.GET)
    public String jstree_add_index() {

        return "admin/jstree_add_index";
    }

    /**
     * 资源树-新增资源
     * @param rti
     * @return
     */
    @RequestMapping(value = "/jstree_add_node", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<Object> jstree_add_node(ResourceTreeInfo rti) {
        //{ "id" : "ajson1", "parent" : "#", "text" : "Simple root node" },
        try {
            if (rti.getNotice_title()!=null && rti.getNotice_title().length() > 4) {
                return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "参数验证不通过-提示语长度不可超过4个汉字", null);
            }
            if (org.apache.commons.lang3.StringUtils.isEmpty(rti.getProduct_code())) {
                return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "产品代码不可为空", null);
            }
            String id = SnowflakeIdWorker.getInstance().nextId() + "";
            rti.setId(id);
            rti.setCreate_time(new Timestamp(new Date().getTime()));
            rti.setUpdate_time(new Timestamp(new Date().getTime()));
            rti.setIs_enable(Const.ENABLE);
            rti.setOwner(getOwner());
            rti.setResource_desc("");
            if(StringUtils.isEmpty(rti.getNotice_title())){
                rti.setNotice_title("");
            }
            if(StringUtils.isEmpty(rti.getEvent_code())){
                rti.setEvent_code("");
            }

            debugInfo(rti);
            resourceTreeMapper.insert(rti);
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), RETURN_CODE.SUCCESS.getDesc(), getBaseException());
        } catch (Exception e) {
            String error = "类:" + Thread.currentThread().getStackTrace()[1].getClassName() + " 函数:" + Thread.currentThread().getStackTrace()[1].getMethodName() + " 异常: {}";
            logger.error(error, e);
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), e.getMessage(), getBaseException(e));
        }

    }

    /**
     * 资源新增根节点
     * @param rti
     * @return
     */
    @RequestMapping(value = "/jstree_add_root_node", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<Object> jstree_add_root_node(ResourceTreeInfo rti) {
        //{ "id" : "ajson1", "parent" : "#", "text" : "Simple root node" },
        try {
            if (rti.getNotice_title()!=null && rti.getNotice_title().length() > 4) {
                return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "参数验证不通过-提示语长度不可超过4个汉字", null);
            }
            if (org.apache.commons.lang3.StringUtils.isEmpty(rti.getProduct_code())) {
                return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "产品代码不可为空", null);
            }

            //校验是否当前产品下已经存在根
            ResourceTreeInfo resourceTreeInfo=new ResourceTreeInfo();
            resourceTreeInfo.setProduct_code(rti.getProduct_code());
            resourceTreeInfo.setParent("#");
            resourceTreeInfo = resourceTreeMapper.selectOne(resourceTreeInfo);
            if(resourceTreeInfo != null){
                return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "当前产品下已存在根", null);
            }

            String id = SnowflakeIdWorker.getInstance().nextId() + "";
            rti.setId(id);
            rti.setCreate_time(new Timestamp(new Date().getTime()));
            rti.setUpdate_time(new Timestamp(new Date().getTime()));
            rti.setIs_enable(Const.ENABLE);
            rti.setOwner(getOwner());
            rti.setResource_desc("");
            if(StringUtils.isEmpty(rti.getNotice_title())){
                rti.setNotice_title("");
            }
            if(StringUtils.isEmpty(rti.getEvent_code())){
                rti.setEvent_code("");
            }
            if(StringUtils.isEmpty(rti.getIcon())){
                rti.setIcon("fa fa-folder");
            }

            debugInfo(rti);
            resourceTreeMapper.insert(rti);
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), RETURN_CODE.SUCCESS.getDesc(), getBaseException());
        } catch (Exception e) {
            String error = "类:" + Thread.currentThread().getStackTrace()[1].getClassName() + " 函数:" + Thread.currentThread().getStackTrace()[1].getMethodName() + " 异常: {}";
            logger.error(error, e);
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), e.getMessage(), getBaseException(e));
        }

    }

    /**
     * 获取资源信息
     * @param parent_id 未使用
     * @param text 未使用
     * @param product_code 产品代码
     * @return
     */
    @RequestMapping(value = "/jstree_node",method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<List<ResourceTreeInfo>> jstree_node(String parent_id, String text,String product_code) {
        //{ "id" : "ajson1", "parent" : "#", "text" : "Simple root node" },
        if(org.apache.commons.lang3.StringUtils.isEmpty(product_code)){
            return ReturnInfo.build("201","产品参数不可为空",getBaseException());
        }
        Example example=new Example(ResourceTreeInfo.class);
        Example.Criteria criteria=example.createCriteria();
        criteria.andEqualTo("product_code", product_code);

        List<ResourceTreeInfo> rtis = resourceTreeMapper.selectByExample(example);
        rtis.sort(Comparator.comparing(ResourceTreeInfo::getOrderN));
        return ReturnInfo.build("200","查询成功",rtis);
    }

    /**
     * 根据主键获取资源信息
     * @param id 主键ID
     * @param text
     * @return
     */
    @RequestMapping(value = "/jstree_get_node",method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String jstree_get_node(String id, String text) {
        //{ "id" : "ajson1", "parent" : "#", "text" : "Simple root node" },
        return JSON.toJSONString(resourceTreeMapper.selectByPrimaryKey(id));
    }

    /**
     * 更新资源信息
     * @param rti
     * @return
     */
    @RequestMapping(value = "/jstree_update_node", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<Object> jstree_update_node(ResourceTreeInfo rti) {
        //{ "id" : "ajson1", "parent" : "#", "text" : "Simple root node" },
        try {
            rti.setUpdate_time(new Timestamp(new Date().getTime()));
            rti.setCreate_time(null);
            rti.setOwner(getOwner());
            rti.setIs_enable(Const.ENABLE);
            rti.setResource_desc("");
            debugInfo(rti);
            resourceTreeMapper.updateByPrimaryKey(rti);

            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), RETURN_CODE.SUCCESS.getDesc(), getBaseException());
        } catch (Exception e) {
            String error = "类:" + Thread.currentThread().getStackTrace()[1].getClassName() + " 函数:" + Thread.currentThread().getStackTrace()[1].getMethodName() + " 异常: {}";
            logger.error(error, e);
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), e.getMessage(), getBaseException(e));
        }

    }

    /**
     * 删除资源信息
     * @param id 主键ID
     * @return
     */
    @RequestMapping(value = "/jstree_del_node", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<Object> jstree_del_node(String id) {
        //{ "id" : "ajson1", "parent" : "#", "text" : "Simple root node" },
        try {
            //校验是否根节点
            ResourceTreeInfo rti = resourceTreeMapper.selectByPrimaryKey(id);
            if(rti.getLevel().equalsIgnoreCase("1")){
                throw new Exception("根节点不可删除");
            }
            resourceTreeMapper.deleteByPrimaryKey(id);
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), RETURN_CODE.SUCCESS.getDesc(), getBaseException());
        } catch (Exception e) {
            String error = "类:" + Thread.currentThread().getStackTrace()[1].getClassName() + " 函数:" + Thread.currentThread().getStackTrace()[1].getMethodName() + " 异常: {}";
            logger.error(error, e);
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), e.getMessage(), getBaseException(e));
        }

    }


    /**
     * 更新资源层级
     * @param id 资源ID
     * @param parent_id 资源父ID
     * @param level 资源层级
     * @return
     */
    @RequestMapping(value = "/jstree_update_parent", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<Object> jstree_update_parent(String id, String parent_id, String level) {
        //{ "id" : "ajson1", "parent" : "#", "text" : "Simple root node" },
        try {
            resourceTreeMapper.updateParentById(id, parent_id, level);
            //递归修改层级
            update_level(id, Integer.parseInt(level));
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), RETURN_CODE.SUCCESS.getDesc(), getBaseException());
        } catch (Exception e) {
            String error = "类:" + Thread.currentThread().getStackTrace()[1].getClassName() + " 函数:" + Thread.currentThread().getStackTrace()[1].getMethodName() + " 异常: {}";
            logger.error(error, e);
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), e.getMessage(), getBaseException(e));
        }
    }

    public void update_level(String id, int level){
        Example example=new Example(ResourceTreeInfo.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("parent", id);
        List<ResourceTreeInfo> resourceTreeInfos = resourceTreeMapper.selectByExample(example);
        if(resourceTreeInfos!=null && resourceTreeInfos.size()>0){
            for (ResourceTreeInfo rti:resourceTreeInfos){
                int next_level = level+1;
                resourceTreeMapper.updateParentById(rti.getId(), id, String.valueOf(next_level));
                update_level(rti.getId(), next_level);
            }
        }


    }

    /**
     * 角色绑定资源
     * @param id 角色ID
     * @param resource_id 资源ID数组
     * @param code 角色code
     * @param name 角色name
     * @param product_code 产品代码
     * @return
     */
    @RequestMapping(value = "/jstree_add_permission", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public ReturnInfo<Object> jstree_add_permission(String id, String[] resource_id, String code, String name,String product_code) {
        //{ "id" : "ajson1", "parent" : "#", "text" : "Simple root node" },
        try {
            if (id.equalsIgnoreCase("-1")) {
                //检查code是否已经存在
                RoleInfo roleInfo=new RoleInfo();
                roleInfo.setProduct_code(product_code);
                roleInfo.setCode(code);
                roleInfo = roleDao.selectOne(roleInfo);
                if(roleInfo != null){
                    throw new Exception("角色code已经存在");
                }

                id = SnowflakeIdWorker.getInstance().nextId() + "";
                //新增角色
                RoleInfo role = new RoleInfo();
                role.setCode(code);
                role.setName(name);
                role.setId(id);
                role.setProduct_code(product_code);
                role.setCreate_time(new Timestamp(new Date().getTime()));
                role.setUpdate_time(new Timestamp(new Date().getTime()));
                roleDao.insert(role);
            }else{
                //获取旧角色信息
                RoleInfo roleInfo=roleDao.selectByPrimaryKey(id);
                roleInfo.setName(name);
                if(!roleInfo.getProduct_code().equalsIgnoreCase(product_code)){
                    throw new Exception("无法更新产品");
                }
                roleDao.updateByPrimaryKey(roleInfo);
            }

            debugInfo(resource_id);
            List<RoleResourceInfo> rris = new ArrayList<>();
            for (String rid : resource_id) {
                RoleResourceInfo rri = new RoleResourceInfo();
                rri.setRole_id(id);
                rri.setResource_id(rid);
                rri.setRole_code(code);
                rri.setCreate_time(new Timestamp(new Date().getTime()));
                rri.setUpdate_time(new Timestamp(new Date().getTime()));
                rris.add(rri);
            }
            resourceTreeMapper.deleteByRoleCode(code);
            resourceTreeMapper.updateUserResource(rris);
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), RETURN_CODE.SUCCESS.getDesc(), getBaseException());
        } catch (Exception e) {
            String error = "类:" + Thread.currentThread().getStackTrace()[1].getClassName() + " 函数:" + Thread.currentThread().getStackTrace()[1].getMethodName() + " 异常: {}";
            logger.error(error, e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), e.getMessage(), getBaseException(e));
        }
    }


    /**
     * 通过角色id获取资源
     * @param id
     * @return
     */
    @RequestMapping(value = "/jstree_permission_list", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public List<RoleResourceInfo> jstree_permission_list(String id,String code) {
        //{ "id" : "ajson1", "parent" : "#", "text" : "Simple root node" },
        List<RoleResourceInfo> uris = new ArrayList<>();

        uris = resourceTreeMapper.selectByRoleCode(code);

        return uris;
    }

    /**
     * 通过用户id获取资源
     * @return
     */
    @RequestMapping(value = "/jstree_permission_list2", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public List<UserResourceInfo2> jstree_permission_list2() {
        //{ "id" : "ajson1", "parent" : "#", "text" : "Simple root node" },
        List<UserResourceInfo2> uris = new ArrayList<>();
        String user_account = getUser().getUserName();
        uris = resourceTreeMapper.selectResourceByUserAccount(user_account);
        uris.sort(Comparator.comparing(UserResourceInfo2::getOrderN));

        return uris;
    }


    /**
     * 权限用户首页
     * @return
     */
    @RequestMapping(value = "/user_index", method = RequestMethod.GET)
    public String user_index() {

        return "admin/user_index";
    }

    /**
     * 获取当前系统的数据组标识
     * @return
     */
    @RequestMapping(value = "/user_tag_group_code", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<List<DataTagGroupInfo>> user_tag_group_code() {
        try {
            String tag_group_code = "";

            PermissionUserInfo permissionUserInfo=new PermissionUserInfo();
            permissionUserInfo.setUser_account(getUser().getUserName());
            Example example2=new Example(PermissionUserInfo.class);
            Example.Criteria criteria2 = example2.createCriteria();
            criteria2.andEqualTo("user_account",getUser().getUserName());
            criteria2.andEqualTo("product_code", ev.getProperty("zdp.product", "zdh"));
            criteria2.andEqualTo("enable",Const.TRUR);
            List<PermissionUserInfo> permissionUserInfos = permissionMapper.selectByExample(example2);

            if(permissionUserInfos!=null && permissionUserInfos.size()>=1){
                tag_group_code = permissionUserInfos.get(0).getTag_group_code();
            }

            if(StringUtils.isEmpty(tag_group_code)){
                return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "查询成功", new ArrayList<DataTagGroupInfo>());
            }
            Example example=new Example(DataTagGroupInfo.class);
            Example.Criteria criteria=example.createCriteria();
            criteria.andEqualTo("is_delete",Const.NOT_DELETE);
            criteria.andIn("tag_group_code", Arrays.asList(tag_group_code.split(",")));

            List<DataTagGroupInfo> dataTagGroupInfos = dataTagGroupMapper.selectByExample(example);

            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "查询成功", dataTagGroupInfos);
        } catch (Exception e) {
            String error = "类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}";
            logger.error(error, e);
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "查询失败", getBaseException(e));
        }
    }

    /**
     * 权限申请列表首页
     * @return
     */
    @RequestMapping(value = "/permission_apply_index", method = RequestMethod.GET)
    public String permission_apply_index() {

        return "admin/permission_apply_index";
    }

    /**
     * 权限新增申请首页
     * @return
     */
    @RequestMapping(value = "/permission_apply_add_index", method = RequestMethod.GET)
    public String permission_apply_add_index() {

        return "admin/permission_apply_add_index";
    }

    /**
     * 权限申请
     * @param product_code 产品代码
     * @param apply_type 申请类型 role,user_group,data_group
     * @return
     */
    @RequestMapping(value = "/permission_apply_by_product_code", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<Object> permission_apply_by_product_code(String product_code, String apply_type) {

        Object result=null;
        if(apply_type.equalsIgnoreCase("role")){
            //请求角色
            result = get_role_by_product_code(product_code);
        }else if(apply_type.equalsIgnoreCase("user_group")){
            result = get_user_group_by_product_code(product_code);
        }else if(apply_type.equalsIgnoreCase("data_group")){
            result = get_group_by_product_code(product_code);
        }else{

        }

        return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "查询成功", result);
    }

    /**
     * 权限申请列表
     * @param permission_context 关键字
     * @return
     */
    @RequestMapping(value = "/permission_apply_list", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<List<PermissionApplyInfo>> permission_apply_list(String permission_context){
        try{
            Example example=new Example(PermissionApplyInfo.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("is_delete", Const.NOT_DELETE);
            criteria.andEqualTo("owner", getUser().getUserName());

            if(!StringUtils.isEmpty(permission_context)){
                Example.Criteria criteria2 = example.createCriteria();
                criteria2.andLike("product_code", getLikeCondition(permission_context));
                criteria2.orLike("apply_type", getLikeCondition(permission_context));
                criteria2.orLike("apply_code", getLikeCondition(permission_context));
                criteria2.orLike("reason", getLikeCondition(permission_context));

                example.and(criteria2);
            }

            List<PermissionApplyInfo> permissionApplyInfos = permissionApplyMapper.selectByExample(example);
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "查询成功", permissionApplyInfos);
        }catch (Exception e){
            String error = "类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}";
            logger.error(error, e);
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "查询失败", getBaseException(e));
        }
    }

    /**
     * 新建申请,并创建审批流
     * @param permissionApplyInfo
     * @param apply_context 申请说明
     * @return
     */
    @RequestMapping(value = "/permission_apply_add", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional
    public ReturnInfo<PermissionApplyInfo> permission_apply_add(PermissionApplyInfo permissionApplyInfo, String apply_context) {
        try{
            permissionApplyInfo.setCreate_time(new Timestamp(new Date().getTime()));
            permissionApplyInfo.setUpdate_time(new Timestamp(new Date().getTime()));
            permissionApplyInfo.setOwner(getOwner());
            permissionApplyInfo.setIs_delete(Const.NOT_DELETE);
            permissionApplyInfo.setStatus(Const.PERMISSION_APPLY_INIT);
            permissionApplyInfo.setFlow_id("0");
            int result = permissionApplyMapper.insert(permissionApplyInfo);
            debugInfo(permissionApplyInfo);
            //开始审批流
            //校验账号是否有效
            PermissionUserInfo pui=new PermissionUserInfo();
            pui.setUser_account(getOwner());
            pui.setProduct_code(permissionApplyInfo.getProduct_code());
            pui=permissionMapper.selectOne(pui);
            if(pui == null){
                throw new Exception("user_account无效,请检查user_account是否正确,若无user_account可在权限管理->用户配置模块增加用户信息");
            }
            if(StringUtils.isEmpty(pui.getUser_group())){
                throw new Exception("无法找到用户对应的组信息,请检查权限管理->用户配置模块中,用户组信息是否未配置");
            }
            String event_code=EventCode.PERMISSION_APPLY.getCode();
            String event_context="权限申请-"+permissionApplyInfo.getApply_type()+'-'+apply_context;
            String event_id=permissionApplyInfo.getId();
            String flow_id = zdhProcessFlowController.createProcess(getUser().getUserName(),pui.getUser_group(),event_code, event_context, event_id, pui.getProduct_code());
            permissionApplyInfo.setFlow_id(flow_id);
            permissionApplyMapper.updateByPrimaryKey(permissionApplyInfo);
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "新增成功", permissionApplyInfo);
        }catch (Exception e){
            String error = "类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}";
            logger.error(error, e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "新增失败", getBaseException(e));
        }
    }

    /**
     * 删除申请,并撤销审批流
     * @param id 申请ID数组
     * @return
     */
    @RequestMapping(value = "/permission_apply_delete", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<List<PermissionApplyInfo>> permission_apply_delete(String[] id){
        try{
            Example example=new Example(PermissionApplyInfo.class);
            Example.Criteria criteria=example.createCriteria();
            criteria.andIn("id", Arrays.asList(id));
            List<PermissionApplyInfo> permissionApplyInfos=permissionApplyMapper.selectByExample(example);
            int result = permissionApplyMapper.deleteLogicByIds("permission_apply_info",id,new Timestamp(new Date().getTime()));
            //取消流程
            for (PermissionApplyInfo pai:permissionApplyInfos){
                ProcessFlowInfo pfi=new ProcessFlowInfo();
                pfi.setFlow_id(pai.getFlow_id());
                pfi.setStatus(Const.STATUS_RECALL);
                zdhProcessFlowController.process_flow_status2(pfi);
            }
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "查询成功", permissionApplyInfos);
        }catch (Exception e){
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "查询失败", getBaseException(e));
        }
    }

    /**
     * 权限申请明细
     * @param id 主键ID
     * @return
     */
    @RequestMapping(value = "/permission_apply_detail", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<PermissionApplyInfo> permission_apply_detail(String id){
        try{
            PermissionApplyInfo pai = permissionApplyMapper.selectByPrimaryKey(id);
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "查询成功", pai);
        }catch (Exception e){
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "查询失败", getBaseException(e));
        }
    }

    private  JSONArray get_role_by_product_code(String product_code){
        RoleInfo roleInfo=new RoleInfo();
        roleInfo.setProduct_code(product_code);
        List<RoleInfo> roleInfos = roleDao.select(roleInfo);
        JSONArray jsonArray=new JSONArray();
        if(roleInfos != null){
            for (RoleInfo ri: roleInfos){
                JSONObject jsonObject=new JSONObject();
                jsonObject.put("code", ri.getCode());
                jsonObject.put("name", ri.getName());
                jsonArray.add(jsonObject);
            }
        }
        return jsonArray;
    }

    private JSONArray get_user_group_by_product_code(String product_code){
        UserGroupInfo userGroupInfo=new UserGroupInfo();
        userGroupInfo.setProduct_code(product_code);

        List<UserGroupInfo> userGroupInfos = userGroupMapper.select(userGroupInfo);
        JSONArray jsonArray=new JSONArray();
        if(userGroupInfos != null){
            for (UserGroupInfo ri: userGroupInfos){
                JSONObject jsonObject=new JSONObject();
                jsonObject.put("code", ri.getId());
                jsonObject.put("name", ri.getName());
                jsonArray.add(jsonObject);
            }
        }
        return jsonArray;
    }

    private JSONArray get_group_by_product_code(String product_code){
        DataTagGroupInfo dataTagGroupInfo=new DataTagGroupInfo();
        dataTagGroupInfo.setProduct_code(product_code);

        List<DataTagGroupInfo> dataTagGroupInfos = dataTagGroupMapper.select(dataTagGroupInfo);
        JSONArray jsonArray=new JSONArray();
        if(dataTagGroupInfos != null){
            for (DataTagGroupInfo ri: dataTagGroupInfos){
                JSONObject jsonObject=new JSONObject();
                jsonObject.put("code", ri.getTag_group_code());
                jsonObject.put("name", ri.getTag_group_name());
                jsonArray.add(jsonObject);
            }
        }
        return jsonArray;
    }

    /**
     * 用户组首页
     * @return
     */
    @White
    @RequestMapping(value = "/user_group_index", method = RequestMethod.GET)
    public String user_group_index() {

        return "admin/user_group_index";
    }

    /**
     * 分页查询用户组列表
     * @param group_context
     * @param limit
     * @param offset
     * @return
     */
    @White
    @RequestMapping(value = "/user_group_list2", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<PageResult<List<UserGroupInfo>>> user_group_list2(String product_code,String group_context, int limit, int offset){
        try{
            Example example=new Example(UserGroupInfo.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("product_code", product_code);
            Example.Criteria criteria2=example.createCriteria();
            if(!StringUtils.isEmpty(group_context)){
                criteria2.andLike("product_code", getLikeCondition(group_context));
                criteria2.orLike("name", getLikeCondition(group_context));
                criteria2.orLike("code", getLikeCondition(group_context));
            }
            example.and(criteria2);
            example.setOrderByClause("create_time desc");
            RowBounds rowBounds=new RowBounds(offset,limit);
            List<UserGroupInfo> userGroupInfos = userGroupMapper.selectByExampleAndRowBounds(example, rowBounds);

            int total = userGroupMapper.selectCountByExample(example);
            PageResult<List<UserGroupInfo>> pageResult=new PageResult<>();
            pageResult.setTotal(total);
            pageResult.setRows(userGroupInfos);
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "查询成功",pageResult);
        }catch (Exception e){
            String error = "类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}";
            logger.error(error, e);
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "查询失败", getBaseException(e));
        }
    }


    /**
     * 查询用户组明细
     * @param id
     * @return
     */
    @White
    @RequestMapping(value = "/user_group_detail", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<UserGroupInfo> user_group_detail(String id) {

        try {
            UserGroupInfo userGroupInfo = userGroupMapper.selectByPrimaryKey(id);
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "查询成功", userGroupInfo);
        } catch (Exception e) {
            String error = "类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}";
            logger.error(error, e);
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "查询失败", e);
        }

    }


    /**
     * 权限申请列表首页
     * @return
     */
    @White
    @RequestMapping(value = "/permission_bigdata_index", method = RequestMethod.GET)
    public String permission_bigdata_index() {

        return "admin/permission_bigdata_index";
    }

    @White
    @RequestMapping(value = "/permission_bigdata_add_index", method = RequestMethod.GET)
    public String permission_bigdata_add_index() {

        return "admin/permission_bigdata_add_index";
    }

    @White
    @RequestMapping(value = "/permission_bigdata_list", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<PageResult<List<PermissionBigdataInfo>>> permission_bigdata_list(String permission_context, int limit, int offset){
        try{
            Example example=new Example(PermissionBigdataInfo.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("is_delete", Const.NOT_DELETE);
            criteria.andEqualTo("owner", getUser().getUserName());

            if(!StringUtils.isEmpty(permission_context)){
                Example.Criteria criteria2 = example.createCriteria();
                criteria2.andLike("product_code", getLikeCondition(permission_context));
                criteria2.orLike("apply_type", getLikeCondition(permission_context));
                criteria2.orLike("apply_code", getLikeCondition(permission_context));
                criteria2.orLike("reason", getLikeCondition(permission_context));

                example.and(criteria2);
            }

            RowBounds rowBounds=new RowBounds(offset,limit);
            int total = permissionBigdataMapper.selectCountByExample(example);
            List<PermissionBigdataInfo> permissionBigdataInfos = permissionBigdataMapper.selectByExampleAndRowBounds(example, rowBounds);

            PageResult<List<PermissionBigdataInfo>> pageResult=new PageResult<>();
            pageResult.setRows(permissionBigdataInfos);
            pageResult.setTotal(total);
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "查询成功", pageResult);
        }catch (Exception e){
            String error = "类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}";
            logger.error(error, e);
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "查询失败", getBaseException(e));
        }
    }

    @RequestMapping(value = "/permission_bigdata_detail", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<PermissionBigdataInfo> permission_bigdata_detail(String id){
        try{

            PermissionBigdataInfo permissionBigdataInfo= permissionBigdataMapper.selectByPrimaryKey(id);
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "查询成功", permissionBigdataInfo);
        }catch (Exception e){
            String error = "类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}";
            logger.error(error, e);
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "查询失败", getBaseException(e));
        }
    }

    @White
    @RequestMapping(value = "/permission_bigdata_add", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<PermissionBigdataInfo> permission_bigdata_add(PermissionBigdataInfo permissionBigdataInfo){
        try{

            permissionBigdataInfo.setCreate_time(new Timestamp(new Date().getTime()));
            permissionBigdataInfo.setOwner(getOwner());
            permissionBigdataInfo.setIs_delete(Const.NOT_DELETE);
            permissionBigdataInfo.setUpdate_time(new Timestamp(new Date().getTime()));

            int result = permissionBigdataMapper.insert(permissionBigdataInfo);
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "查询成功", permissionBigdataInfo);
        }catch (Exception e){
            String error = "类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}";
            logger.error(error, e);
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "查询失败", getBaseException(e));
        }
    }

    @White
    @RequestMapping(value = "/permission_bigdata_update", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<PermissionBigdataInfo> permission_bigdata_update(PermissionBigdataInfo permissionBigdataInfo){
        try{
            Example example=new Example(PermissionApplyInfo.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("is_delete", Const.NOT_DELETE);
            criteria.andEqualTo("owner", getUser().getUserName());

            PermissionBigdataInfo oldPermissionBigdataInfo = permissionBigdataMapper.selectByPrimaryKey(permissionBigdataInfo.getId());
            permissionBigdataInfo.setCreate_time(oldPermissionBigdataInfo.getCreate_time());
            permissionBigdataInfo.setOwner(oldPermissionBigdataInfo.getOwner());
            permissionBigdataInfo.setIs_delete(Const.NOT_DELETE);
            permissionBigdataInfo.setUpdate_time(new Timestamp(new Date().getTime()));

            int result = permissionBigdataMapper.updateByPrimaryKey(permissionBigdataInfo);
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "查询成功", permissionBigdataInfo);
        }catch (Exception e){
            String error = "类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}";
            logger.error(error, e);
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "查询失败", getBaseException(e));
        }
    }

    @RequestMapping(value = "/permission_bigdata_delete", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<List<PermissionApplyInfo>> permission_bigdata_delete(String[] ids){
        try{
            permissionBigdataMapper.deleteLogicByIds("permission_bigdata_info", ids, new Timestamp(new Date().getTime()));
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "查询成功", null);
        }catch (Exception e){
            String error = "类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}";
            logger.error(error, e);
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "查询失败", getBaseException(e));
        }
    }

    @White
    @RequestMapping(value = "/permission_rule_list", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<List<JSONObject>> permission_rule_list(String product_code){
        try{
            //获取产品类型
            ProductTagInfo productTagInfo=new ProductTagInfo();
            productTagInfo.setProduct_code(product_code);
            productTagInfo.setIs_delete(Const.NOT_DELETE);
            productTagInfo = productTagMapper.selectOne(productTagInfo);

            //根据产品类型,获取权限规则
            if(productTagInfo == null || StringUtils.isEmpty(productTagInfo.getProduct_type())){
                throw new Exception("获取产品异常,或者产品类型为空");
            }
            List<JSONObject> permissionRuleList=new ArrayList<>();
            String product_type = productTagInfo.getProduct_type();

            //根据type获取枚举信息
            EnumInfo enumInfo=new EnumInfo();
            enumInfo.setEnum_code("bigdata_"+product_type.toLowerCase());
            enumInfo = enumMapper.selectOne(enumInfo);
            JSONArray enumJson =  enumInfo.getEnum_json_object();
            if(enumJson != null){
                for (Object jsonObject: enumJson){
                    permissionRuleList.add((JSONObject) jsonObject);
                }
            }
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "查询成功", permissionRuleList);
        }catch (Exception e){
            String error = "类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}";
            logger.error(error, e);
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "查询失败", e);
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
                    logger.error("类:" + Thread.currentThread().getStackTrace()[1].getClassName() + " 函数:" + Thread.currentThread().getStackTrace()[1].getMethodName() + " 异常: {}" , e);
                }
                // 恢复访问控制权限
                fields[i].setAccessible(accessFlag);
            } catch (IllegalArgumentException e) {
                logger.error("类:" + Thread.currentThread().getStackTrace()[1].getClassName() + " 函数:" + Thread.currentThread().getStackTrace()[1].getMethodName() + " 异常: {}" , e);
            }
        }
    }


}
