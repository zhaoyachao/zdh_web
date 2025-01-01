package com.zyc.zdh.controller;

import cn.hutool.crypto.Mode;
import cn.hutool.crypto.Padding;
import cn.hutool.crypto.symmetric.AES;
import com.zyc.zdh.dao.*;
import com.zyc.zdh.entity.*;
import com.zyc.zdh.shiro.SessionDao;
import com.zyc.zdh.util.ConfigUtil;
import com.zyc.zdh.util.Const;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import tk.mybatis.mapper.entity.Example;

import java.sql.Timestamp;
import java.util.*;


/**
 * 权限api 统一业务逻辑处理
 */
@Controller("permissionApiController")
public class PermissionApiController {

    public Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SessionDao sessionDao;
    @Autowired
    private ProductTagMapper productTagMapper;
    @Autowired
    private ResourceTreeMapper resourceTreeMapper;
    @Autowired
    private PermissionMapper permissionMapper;
    @Autowired
    private UserGroupMapper userGroupMapper;
    @Autowired
    private RoleDao roleDao;
    @Autowired
    private DataTagMapper dataTagMapper;
    @Autowired
    private DataTagGroupMapper dataTagGroupMapper;
    @Autowired
    private PermissionDimensionMapper permissionDimensionMapper;
    @Autowired
    private PermissionDimensionValueMapper permissionDimensionValueMapper;
    @Autowired
    private PermissionUserDimensionValueMapper permissionUserDimensionValueMapper;
    @Autowired
    private PermissionUserGroupDimensionValueMapper permissionUserGroupDimensionValueMapper;
    @Autowired
    private Environment ev;



    /**
     * 申请产品 获取ak,sk, 暂未实现
     * @param user_account 用户账号
     * @param product_code 产品代码
     * @param ak  ak
     * @param sk  sk
     * @return
     */
    @RequestMapping(value = "apply_product_by_user", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo apply_product_by_user(String user_account, String product_code,String ak, String sk) {

        try{
            check_aksk(product_code, ak, sk);
            //提前创建好审批流


            //增加审批关系


            //返回统一信息
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "查询成功", null);
        }catch (Exception e){
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "查询失败", e);
        }
    }

    /**
     * 新增用户
     * @param product_code 产品代码
     * @param ak  ak
     * @param sk  sk
     * @param permissionUserInfo
     * @return
     */
    @RequestMapping(value = "add_user_by_product", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo add_user_by_product(String product_code,String ak, String sk, PermissionUserInfo permissionUserInfo) {

        try{
            //检查ak,sk
            check_aksk(product_code, ak, sk);

            permissionUserInfo.setProduct_code(product_code);
            //检查用户信息
            check_user(permissionUserInfo);

            //检查用户是否存在
            check_exist_user(product_code, permissionUserInfo.getUser_account());

            //新增用户
            permissionMapper.insertSelective(permissionUserInfo);

            //返回统一信息
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "新增成功", null);
        }catch (Exception e){
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "新增失败", e);
        }
    }

    /**
     * 更新用户信息
     * @param product_code 产品代码
     * @param ak  ak
     * @param sk  sk
     * @param permissionUserInfo
     * @return
     */
    @RequestMapping(value = "update_user_by_product", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo update_user_by_product(String product_code,String ak, String sk, PermissionUserInfo permissionUserInfo) {

        try{
            //检查ak,sk
            check_aksk(product_code, ak, sk);

           //检查product_code 和用户信息是否一致

            if(product_code != permissionUserInfo.getProduct_code()){
                throw new Exception("存在多个产品code");
            }

            //检查用户信息
            check_user(permissionUserInfo);

            //新增用户
            permissionMapper.updateByPrimaryKeySelective(permissionUserInfo);

            //返回统一信息
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "更新成功", null);
        }catch (Exception e){
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "更新失败", e);
        }
    }


    /**
     * 启用/禁用用户
     * @param product_code 产品代码
     * @param ak  ak
     * @param sk  sk
     * @param user_account 用户账号
     * @param enable 是否启用true/false
     * @return
     */
    @RequestMapping(value = "enable_user_by_product", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo enable_user_by_product(String product_code,String ak, String sk, String user_account, String enable) {

        try{
            //检查ak,sk
            check_aksk(product_code, ak, sk);

            //更新用户
            Example example=new Example(PermissionUserInfo.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("user_account", user_account);
            criteria.andEqualTo("product_code", product_code);
            PermissionUserInfo permissionUserInfo=new PermissionUserInfo();
            permissionUserInfo.setEnable(enable);
            permissionUserInfo.setUpdate_time(new Timestamp(System.currentTimeMillis()));

            permissionMapper.updateByExampleSelective(permissionUserInfo, example);

            //返回统一信息
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "更新成功", null);
        }catch (Exception e){
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "更新失败", e);
        }
    }

    /**
     * 批量启用/禁用用户
     * @param product_code 产品代码
     * @param ak  ak
     * @param sk  sk
     * @param user_account 用户账号
     * @param enable 是否启用true/false
     * @return
     */
    @RequestMapping(value = "update_batch_user_by_product", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo update_batch_user_by_product(String product_code,String ak, String sk, String[] user_account, String enable) {

        try{
            //检查ak,sk
            check_aksk(product_code, ak, sk);

            //更新用户
            Example example=new Example(PermissionUserInfo.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andIn("user_account", Arrays.asList(user_account));
            criteria.andEqualTo("product_code", product_code);
            PermissionUserInfo permissionUserInfo=new PermissionUserInfo();
            permissionUserInfo.setEnable(enable);
            permissionUserInfo.setUpdate_time(new Timestamp(System.currentTimeMillis()));
            permissionMapper.updateByExampleSelective(permissionUserInfo, example);

            //返回统一信息
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "更新成功", null);
        }catch (Exception e){
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "更新失败", e);
        }
    }


    /**
     * 根据用户名密码获取用户信息
     * @param product_code 产品代码
     * @param ak  ak
     * @param sk  sk
     * @param user_account 用户账号
     * @return
     */
    @RequestMapping(value = "auth", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo auth(String product_code,String ak, String sk, String user_account,String password, String auth_type,
                           Map<String,List<String>> in_auths, Map<String,List<String>> out_auths) {

        try{
            //检查ak,sk
            check_aksk(product_code, ak, sk);

            //获取产品类型
            //检查auth_type,密码校验还是权限认证
            if(auth_type.equalsIgnoreCase("Authen")){
                //密码验证
                return password(product_code, ak, sk, user_account, password);
            }else if(auth_type.equalsIgnoreCase("Author")){
                return auth_hive(product_code, ak, sk, user_account,in_auths,out_auths);
            }
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "查询失败", "不支持的权限校验");
        }catch (Exception e){
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "查询失败", e);
        }
    }

    private ReturnInfo password(String product_code,String ak, String sk, String user_account,String password){
        //password解密
        String user_password = aes(password, ConfigUtil.getValue(ConfigUtil.ZDH_AUTH_PASSWORD_KEY).toString(), ConfigUtil.getValue(ConfigUtil.ZDH_AUTH_PASSWORD_IV).toString());

        //更新用户
        Example example=new Example(PermissionUserInfo.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("user_account", user_account);
        criteria.andEqualTo("user_password", user_password);
        criteria.andEqualTo("product_code", product_code);
        criteria.andEqualTo("enable", Const.TRUR);

        List<PermissionUserInfo> permissionUserInfos = permissionMapper.selectByExample(example);

        if(permissionUserInfos!=null && permissionUserInfos.size()==1){
            permissionUserInfos.get(0).setUser_password("");
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "查询成功", permissionUserInfos.get(0));
        }
        return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "查询失败", "未找到用户信息");
    }

    private ReturnInfo auth_hive(String product_code,String ak, String sk, String user_account,
                                 Map<String,List<String>> in_auths, Map<String,List<String>> out_auths){
        //验证权限

        //此处待实现

        return null;
    }

    /**
     * 根据用户名密码获取用户信息
     * @param product_code 产品代码
     * @param ak  ak
     * @param sk  sk
     * @param user_account 用户账号
     * @return
     */
    @RequestMapping(value = "get_user_by_product_password", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<PermissionUserInfo> get_user_by_product_password(String product_code,String ak, String sk, String user_account,String password) {

        try{
            //检查ak,sk
            check_aksk(product_code, ak, sk);

            //password解密
            String user_password = aes(password, ConfigUtil.getValue(ConfigUtil.ZDH_AUTH_PASSWORD_KEY).toString(), ConfigUtil.getValue(ConfigUtil.ZDH_AUTH_PASSWORD_IV).toString());

            //更新用户
            Example example=new Example(PermissionUserInfo.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("user_account", user_account);
            criteria.andEqualTo("user_password", user_password);
            criteria.andEqualTo("product_code", product_code);
            criteria.andEqualTo("enable", Const.TRUR);

            List<PermissionUserInfo> permissionUserInfos = permissionMapper.selectByExample(example);

            if(permissionUserInfos!=null && permissionUserInfos.size()==1){
                permissionUserInfos.get(0).setUser_password("");
                return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "查询成功", permissionUserInfos.get(0));
            }
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "查询失败", new Exception("未找到用户信息"));
        }catch (Exception e){
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "查询失败", e);
        }
    }


    /**
     * 获取用户信息
     * @param product_code 产品代码
     * @param ak  ak
     * @param sk  sk
     * @param user_account 用户账号
     * @return
     */
    @RequestMapping(value = "get_user_by_product", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<PermissionUserInfo> get_user_by_product(String product_code,String ak, String sk, String user_account) {

        try{
            //检查ak,sk
            check_aksk(product_code, ak, sk);

            //更新用户
            Example example=new Example(PermissionUserInfo.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("user_account", user_account);
            criteria.andEqualTo("product_code", product_code);
            criteria.andEqualTo("enable", Const.TRUR);

            List<PermissionUserInfo> permissionUserInfos = permissionMapper.selectByExample(example);
            if(permissionUserInfos == null || permissionUserInfos.size()!= 1){
                throw new Exception("查找用户失败");
            }
            //返回统一信息
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "查询成功", permissionUserInfos.get(0));
        }catch (Exception e){
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "查询失败", e);
        }
    }


    /**
     * 批量获取用户信息
     * @param product_code 产品代码
     * @param ak  ak
     * @param sk  sk
     * @param user_account 用户账号
     * @return
     */
    @RequestMapping(value = "get_user_by_product_user", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<List<PermissionUserInfo>> get_user_list_by_product_users(String product_code,String ak, String sk, String[] user_account) {

        try{
            //检查ak,sk
            check_aksk(product_code, ak, sk);

            //更新用户
            Example example=new Example(PermissionUserInfo.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andIn("user_account", Arrays.asList(user_account));
            criteria.andEqualTo("product_code", product_code);
            criteria.andEqualTo("enable", Const.TRUR);

            List<PermissionUserInfo> permissionUserInfos = permissionMapper.selectByExample(example);

            //返回统一信息
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "查询成功", permissionUserInfos);
        }catch (Exception e){
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "查询失败", e);
        }
    }

    /**
     * 获取产品下所有用户
     * @param product_code 产品代码
     * @param ak  ak
     * @param sk  sk
     * @return
     */
    @RequestMapping(value = "get_user_list_by_product", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<List<PermissionUserInfo>> get_user_by_product(String product_code,String ak, String sk) {

        try{
            //检查ak,sk
            check_aksk(product_code, ak, sk);

            //更新用户
            Example example=new Example(PermissionUserInfo.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("product_code", product_code);
            criteria.andEqualTo("enable", Const.TRUR);


            List<PermissionUserInfo> permissionUserInfos = permissionMapper.selectByExample(example);

            //返回统一信息
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "查询成功", permissionUserInfos);
        }catch (Exception e){
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "查询失败", e);
        }
    }


    /**
     * 新增用户组
     * @param product_code 产品代码
     * @param ak  ak
     * @param sk  sk
     * @param user_group 用户组code
     * @return
     */
    @RequestMapping(value = "add_user_group_by_product", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo add_user_group_by_product(String product_code,String ak, String sk, String user_group) {

        try{
            //检查ak,sk
            check_aksk(product_code, ak, sk);

            //检查用户组是否存在
            Example example=new Example(UserGroupInfo.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("name", user_group);
            criteria.andEqualTo("product_code", product_code);

            List<UserGroupInfo> userGroupInfos = userGroupMapper.selectByExample(example);

            if(userGroupInfos !=null && userGroupInfos.size()>0){
                throw new Exception("用户组名在当前产品下已经存在");
            }

            UserGroupInfo userGroupInfo=new UserGroupInfo();

            userGroupInfo.setProduct_code(product_code);
            userGroupInfo.setName(user_group);
            userGroupInfo.setEnable(Const.TRUR);
            userGroupInfo.setCreate_time(new Timestamp(System.currentTimeMillis()));
            userGroupInfo.setUpdate_time(new Timestamp(System.currentTimeMillis()));

            userGroupMapper.insertSelective(userGroupInfo);
            //返回统一信息
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "更新成功", null);
        }catch (Exception e){
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "更新失败", e);
        }
    }


    /**
     * 增加角色
     * @param product_code 产品代码
     * @param ak  ak
     * @param sk  sk
     * @param role_info
     * @return
     */
    @RequestMapping(value = "add_role_by_product", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo add_role_by_product(String product_code,String ak, String sk, RoleInfo role_info) {

        try{
            //检查ak,sk
            check_aksk(product_code, ak, sk);

            //检查角色信息
            check_role(role_info);

            //检查角色code是否存在
            check_exist_role(product_code, role_info.getCode());

            role_info.setCreate_time(new Timestamp(System.currentTimeMillis()));
            role_info.setUpdate_time(new Timestamp(System.currentTimeMillis()));
            roleDao.insert(role_info);

            //返回统一信息
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "新增成功", null);
        }catch (Exception e){
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "新增失败", e);
        }
    }

    /**
     * 禁用/启用 角色
     * @param product_code 产品代码
     * @param ak  ak
     * @param sk  sk
     * @param role_info
     * @return
     */
    @RequestMapping(value = "enable_role_by_product", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo enable_role_by_product(String product_code,String ak, String sk, RoleInfo role_info) {

        try{
            //检查ak,sk
            check_aksk(product_code, ak, sk);

            //检查角色信息
            RoleInfo roleInfo = roleDao.selectByPrimaryKey(role_info.getId());
            roleInfo.setEnable(role_info.getEnable());
            roleInfo.setCreate_time(new Timestamp(System.currentTimeMillis()));
            roleInfo.setUpdate_time(new Timestamp(System.currentTimeMillis()));
            roleDao.updateByPrimaryKeySelective(roleInfo);

            //返回统一信息
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "更新成功", null);
        }catch (Exception e){
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "更新失败", e);
        }
    }

    /**
     * 角色增加资源
     * tips: 每次角色增加资源以全量方式增加,会提前删除当前角色下的资源配置
     * @param product_code 产品代码
     * @param ak  ak
     * @param sk  sk
     * @param role_id 角色ID
     * @param resource_ids 资源ID列表
     * @return
     */
    @RequestMapping(value = "add_resource_in_role_by_product", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public ReturnInfo enable_role_by_product(String product_code,String ak, String sk, String role_id, String[] resource_ids) {

        try{
            //检查ak,sk
            check_aksk(product_code, ak, sk);

            List<RoleResourceInfo> rris = new ArrayList<>();
            for (String rid : resource_ids) {
                RoleResourceInfo rri = new RoleResourceInfo();
                rri.setRole_id(role_id);
                rri.setResource_id(rid);
                rri.setCreate_time(new Timestamp(System.currentTimeMillis()));
                rri.setUpdate_time(new Timestamp(System.currentTimeMillis()));
                rri.setProduct_code(product_code);
                rris.add(rri);
            }
            resourceTreeMapper.deleteByRoleId(role_id);
            resourceTreeMapper.updateUserResource(rris);
            //返回统一信息
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "更新成功", null);
        }catch (Exception e){
            String error = "类:" + Thread.currentThread().getStackTrace()[1].getClassName() + " 函数:" + Thread.currentThread().getStackTrace()[1].getMethodName() + " 异常: {}";
            logger.error(error, e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "更新失败", e);
        }
    }

    /**
     * 根据role_code 获取角色
     * @param product_code 产品代码
     * @param ak  ak
     * @param sk  sk
     * @param role_code 角色code
     * @return
     */
    @RequestMapping(value = "get_role_by_product", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<RoleInfo> get_role_by_product(String product_code,String ak, String sk, String role_code) {

        try{
            //检查ak,sk
            check_aksk(product_code, ak, sk);

            RoleInfo roleInfo=new RoleInfo();
            roleInfo.setCode(role_code);
            roleInfo.setProduct_code(product_code);
            roleInfo.setEnable(Const.TRUR);

            roleInfo = roleDao.selectOne(roleInfo);
            //返回统一信息
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "查询成功", roleInfo);
        }catch (Exception e){
            String error = "类:" + Thread.currentThread().getStackTrace()[1].getClassName() + " 函数:" + Thread.currentThread().getStackTrace()[1].getMethodName() + " 异常: {}";
            logger.error(error, e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "查询失败", e);
        }
    }

    /**
     * 获取产品线下所有角色
     * @param product_code 产品代码
     * @param ak  ak
     * @param sk  sk
     * @return
     */
    @RequestMapping(value = "get_role_list_by_product", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<List<RoleInfo>> get_role_by_product(String product_code,String ak, String sk) {

        try{
            //检查ak,sk
            check_aksk(product_code, ak, sk);

            RoleInfo roleInfo=new RoleInfo();
            roleInfo.setProduct_code(product_code);
            roleInfo.setEnable(Const.TRUR);

            List<RoleInfo> roleInfos = roleDao.select(roleInfo);

            //返回统一信息
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "查询成功", roleInfos);
        }catch (Exception e){
            String error = "类:" + Thread.currentThread().getStackTrace()[1].getClassName() + " 函数:" + Thread.currentThread().getStackTrace()[1].getMethodName() + " 异常: {}";
            logger.error(error, e);
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "查询失败", e);
        }
    }

    /**
     * 获取角色下的用户列表
     * @param product_code 产品代码
     * @param role_code 角色code
     * @param ak ak
     * @param sk sk
     * @return
     */
    @RequestMapping(value = "get_user_list_by_product_role", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<List<PermissionUserInfo>> get_user_list_by_product_role(String product_code,String role_code,String ak, String sk) {

        try{
            //检查ak,sk
            check_aksk(product_code, ak, sk);

            //根据code 获取id
            RoleInfo roleInfo=new RoleInfo();
            roleInfo.setCode(role_code);
            roleInfo.setProduct_code(product_code);
            roleInfo.setEnable(Const.TRUR);

            roleInfo = roleDao.selectOne(roleInfo);

            Example example=new Example(PermissionUserInfo.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("product_code", product_code);
            criteria.andCondition("find_in_set('"+roleInfo.getId()+"', roles)");
            criteria.andEqualTo("enable", Const.TRUR);

            List<PermissionUserInfo> permissionUserInfos = permissionMapper.selectByExample(example);

            //返回统一信息
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "查询成功", permissionUserInfos);
        }catch (Exception e){
            String error = "类:" + Thread.currentThread().getStackTrace()[1].getClassName() + " 函数:" + Thread.currentThread().getStackTrace()[1].getMethodName() + " 异常: {}";
            logger.error(error, e);
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "查询失败", e);
        }
    }


    /**
     * 新增资源
     * @param product_code 产品代码
     * @param ak  ak
     * @param sk  sk
     * @param resource_tree_info
     * @return
     */
    @RequestMapping(value = "add_resource_by_product", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo add_resource_by_product(String product_code,String ak, String sk, ResourceTreeInfo resource_tree_info) {

        try{
            check_aksk(product_code, ak, sk);
            resource_tree_info.setProduct_code(product_code);
            int result = resourceTreeMapper.insertSelective(resource_tree_info);

            //返回统一信息
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "新增成功", null);
        }catch (Exception e){
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "新增失败", e);
        }
    }

    /**
     * 批量增加资源
     * @param product_code 产品代码
     * @param ak  ak
     * @param sk  sk
     * @param resource_tree_info
     * @return
     */
    @RequestMapping(value = "add_batch_resource_by_product", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public ReturnInfo add_batch_resource_by_product(String product_code,String ak, String sk, @RequestBody List<ResourceTreeInfo> resource_tree_info) {

        try{
            check_aksk(product_code, ak, sk);
            for (ResourceTreeInfo rti:resource_tree_info){
                rti.setProduct_code(product_code);
                int result = resourceTreeMapper.insertSelective(rti);
            }

            //返回统一信息
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "新增成功", null);
        }catch (Exception e){
            String error = "类:" + Thread.currentThread().getStackTrace()[1].getClassName() + " 函数:" + Thread.currentThread().getStackTrace()[1].getMethodName() + " 异常: {}";
            logger.error(error, e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "新增失败", e);
        }
    }


    /**
     * 通过用户账户 获取资源信息
     * @param user_account 用户账号
     * @param product_code 产品代码
     * @param ak  ak
     * @param sk  sk
     * @return
     */
    @RequestMapping(value = "resources_by_user", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<List<UserResourceInfo2>> resources_by_user(String user_account, String product_code,String ak, String sk) {

        try{
            check_aksk(product_code, ak, sk);
            //验证用户和产品是否匹配 todo 此处需要实现,在前端增加 用户申请产品,通过后记录用户和产品对应关系

            //通过product_code 用户绑定角色,角色绑定资源
            List<UserResourceInfo2> uris = resourceTreeMapper.selectResourceByUserAccount(user_account, product_code);
            if(uris != null){
                uris.sort(Comparator.comparing(UserResourceInfo2::getOrderN));
            }

            //返回统一信息
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "查询成功", uris);
        }catch (Exception e){
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "查询失败", e);
        }
    }

    /**
     * 通过角色code获取资源
     * @param role_code 角色code
     * @param product_code 产品代码
     * @param ak  ak
     * @param sk  sk
     * @return
     */
    @RequestMapping(value = "resources_by_role", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo resources_by_role(String role_code, String product_code,String ak, String sk) {

        try{
            check_aksk(product_code, ak, sk);
            //验证用户和产品是否匹配 todo 此处需要实现,在前端增加 用户申请产品,通过后记录用户和产品对应关系

            //通过product_code 用户绑定角色,角色绑定资源
            List<UserResourceInfo2> uris = resourceTreeMapper.selectResourceByRoleCode(role_code, product_code);
            if(uris != null){
                uris.sort(Comparator.comparing(UserResourceInfo2::getOrderN));
            }

            //返回统一信息
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "查询成功", uris);
        }catch (Exception e){
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "查询失败", e);
        }
    }

    /**
     * 新增数据标识
     * @param product_code 产品代码
     * @param ak  ak
     * @param sk  sk
     * @param dataTagInfo
     * @return
     */
    @RequestMapping(value = "add_data_tag_by_product", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo add_data_tag_by_product(String product_code,String ak, String sk, DataTagInfo dataTagInfo) {

        try{
            check_aksk(product_code, ak, sk);
            //验证用户和产品是否匹配 todo 此处需要实现,在前端增加 用户申请产品,通过后记录用户和产品对应关系

            //校验tag_code是否重复
            DataTagInfo dti=new DataTagInfo();
            dti.setTag_code(dataTagInfo.getTag_code());
            dti.setProduct_code(product_code);
            dti = dataTagMapper.selectOne(dti);
            if(dti != null){
                throw new Exception("数据标识code已存在");
            }

            dataTagMapper.insertSelective(dataTagInfo);
            //返回统一信息
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "查询成功", null);
        }catch (Exception e){
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "查询失败", e);
        }
    }

    /**
     * 新增数据标识
     * @param product_code 产品代码
     * @param ak  ak
     * @param sk  sk
     * @param dataTagInfo
     * @return
     */
    @RequestMapping(value = "update_data_tag_by_product", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo update_data_tag_by_product(String product_code,String ak, String sk, DataTagInfo dataTagInfo) {

        try{
            check_aksk(product_code, ak, sk);
            //验证用户和产品是否匹配 todo 此处需要实现,在前端增加 用户申请产品,通过后记录用户和产品对应关系

            //校验tag_code是否重复
            DataTagInfo dti=new DataTagInfo();
            dti = dataTagMapper.selectByPrimaryKey(dataTagInfo.getId());
            if(dti != null && !dti.getProduct_code().equalsIgnoreCase(product_code)){
                throw new Exception("数据标识所属产品不可更改");
            }

            dataTagMapper.updateByPrimaryKeySelective(dataTagInfo);
            //返回统一信息
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "更新成功", null);
        }catch (Exception e){
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "更新失败", e);
        }
    }

    /**
     * 新增数据组标识
     * @param product_code 产品代码
     * @param ak  ak
     * @param sk  sk
     * @param dataTagGroupInfo
     * @return
     */
    @RequestMapping(value = "add_data_tag_group_by_product", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo add_data_tag_group_by_product(String product_code,String ak, String sk, DataTagGroupInfo dataTagGroupInfo) {

        try{
            check_aksk(product_code, ak, sk);
            //验证用户和产品是否匹配 todo 此处需要实现,在前端增加 用户申请产品,通过后记录用户和产品对应关系

            //校验tag_code是否重复
            DataTagGroupInfo dtgi=new DataTagGroupInfo();
            dtgi.setTag_group_code(dataTagGroupInfo.getTag_group_code());
            dtgi.setProduct_code(product_code);
            dtgi = dataTagGroupMapper.selectOne(dtgi);
            if(dtgi != null){
                throw new Exception("数据组标识code已存在");
            }

            dataTagGroupMapper.insertSelective(dataTagGroupInfo);
            //返回统一信息
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "新增成功", null);
        }catch (Exception e){
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "新增失败", e);
        }
    }

    /**
     * 新增数据标识
     * @param product_code 产品代码
     * @param ak  ak
     * @param sk  sk
     * @param dataTagGroupInfo
     * @return
     */
    @RequestMapping(value = "update_data_tag_group_by_product", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<DataTagGroupInfo> update_data_tag_group_by_product(String product_code,String ak, String sk, DataTagGroupInfo dataTagGroupInfo) {

        try{
            check_aksk(product_code, ak, sk);
            //验证用户和产品是否匹配 todo 此处需要实现,在前端增加 用户申请产品,通过后记录用户和产品对应关系

            //校验tag_code是否重复
            DataTagGroupInfo dtgi=new DataTagGroupInfo();
            dtgi = dataTagGroupMapper.selectByPrimaryKey(dataTagGroupInfo.getId());
            if(dtgi != null && !dtgi.getProduct_code().equalsIgnoreCase(product_code)){
                throw new Exception("数据组标识所属产品不可更改");
            }

            dataTagGroupMapper.updateByPrimaryKeySelective(dataTagGroupInfo);
            //返回统一信息
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "更新成功", null);
        }catch (Exception e){
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "更新失败", e);
        }
    }


    /**
     * 获取产品线下所有维度
     * @param product_code 产品代码
     * @param ak  ak
     * @param sk  sk
     * @return
     */
    @RequestMapping(value = "get_dimension_list_by_product", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<List<PermissionDimensionInfo>> get_dimension_list_by_product(String product_code,String ak, String sk) {

        try{
            //检查ak,sk
            check_aksk(product_code, ak, sk);

            PermissionDimensionInfo permissionDimensionInfo=new PermissionDimensionInfo();
            permissionDimensionInfo.setProduct_code(product_code);
            permissionDimensionInfo.setIs_delete(Const.NOT_DELETE);

            List<PermissionDimensionInfo> permissionDimensionInfos = permissionDimensionMapper.select(permissionDimensionInfo);

            //返回统一信息
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "查询成功", permissionDimensionInfos);
        }catch (Exception e){
            String error = "类:" + Thread.currentThread().getStackTrace()[1].getClassName() + " 函数:" + Thread.currentThread().getStackTrace()[1].getMethodName() + " 异常: {}";
            logger.error(error, e);
            return ReturnInfo.buildError( "查询失败", e);
        }
    }


    /**
     * 获取产品线下所有维度
     * @param product_code 产品代码
     * @param ak  ak
     * @param sk  sk
     * @return
     */
    @RequestMapping(value = "get_dimension_value_list_by_product", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<List<PermissionDimensionValueInfo>> get_dimension_value_list_by_product(String product_code,String ak, String sk) {

        try{
            //检查ak,sk
            check_aksk(product_code, ak, sk);

            PermissionDimensionValueInfo permissionDimensionValueInfo=new PermissionDimensionValueInfo();
            permissionDimensionValueInfo.setProduct_code(product_code);
            permissionDimensionValueInfo.setIs_delete(Const.NOT_DELETE);

            List<PermissionDimensionValueInfo> permissionDimensionInfos = permissionDimensionValueMapper.select(permissionDimensionValueInfo);

            //返回统一信息
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "查询成功", permissionDimensionInfos);
        }catch (Exception e){
            String error = "类:" + Thread.currentThread().getStackTrace()[1].getClassName() + " 函数:" + Thread.currentThread().getStackTrace()[1].getMethodName() + " 异常: {}";
            logger.error(error, e);
            return ReturnInfo.buildError( "查询失败", e);
        }
    }


    /**
     * 获取用户在产品线下所有维度
     * @param product_code 产品代码
     * @param ak  ak
     * @param sk  sk
     * @return
     */
    @RequestMapping(value = "get_user_dimension_list_by_product", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<List<PermissionDimensionInfo>> get_user_dimension_list_by_product(String user_account,String product_code,String ak, String sk) {

        try{
            //检查ak,sk
            check_aksk(product_code, ak, sk);

            PermissionDimensionInfo permissionDimensionInfo=new PermissionDimensionInfo();
            permissionDimensionInfo.setProduct_code(product_code);
            permissionDimensionInfo.setIs_delete(Const.NOT_DELETE);

            List<PermissionDimensionInfo> permissionDimensionInfos = permissionUserDimensionValueMapper.selectDimByUser(product_code, user_account);

            //返回统一信息
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "查询成功", permissionDimensionInfos);
        }catch (Exception e){
            String error = "类:" + Thread.currentThread().getStackTrace()[1].getClassName() + " 函数:" + Thread.currentThread().getStackTrace()[1].getMethodName() + " 异常: {}";
            logger.error(error, e);
            return ReturnInfo.buildError( "查询失败", e);
        }
    }

    /**
     * 获取用户在产品线下所有维度值
     * @param product_code 产品代码
     * @param ak  ak
     * @param sk  sk
     * @return
     */
    @RequestMapping(value = "get_user_dimension_value_list_by_product", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<List<PermissionUserDimensionValueInfo>> get_user_dimension_value_list_by_product(String user_account, String product_code,String ak, String sk) {

        try{
            //检查ak,sk
            check_aksk(product_code, ak, sk);

            PermissionUserDimensionValueInfo permissionUserDimensionValueInfo=new PermissionUserDimensionValueInfo();
            permissionUserDimensionValueInfo.setProduct_code(product_code);
            permissionUserDimensionValueInfo.setUser_account(user_account);
            permissionUserDimensionValueInfo.setIs_delete(Const.NOT_DELETE);

            List<PermissionUserDimensionValueInfo> permissionUserDimensionValueInfos = permissionUserDimensionValueMapper.select(permissionUserDimensionValueInfo);

            //返回统一信息
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "查询成功", permissionUserDimensionValueInfos);
        }catch (Exception e){
            String error = "类:" + Thread.currentThread().getStackTrace()[1].getClassName() + " 函数:" + Thread.currentThread().getStackTrace()[1].getMethodName() + " 异常: {}";
            logger.error(error, e);
            return ReturnInfo.buildError( "查询失败", e);
        }
    }

    /**
     * 获取用户在产品线下所有维度
     * @param product_code 产品代码
     * @param ak  ak
     * @param sk  sk
     * @return
     */
    @RequestMapping(value = "get_usergroup_dimension_list_by_product", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<List<PermissionDimensionInfo>> get_usergroup_dimension_list_by_product(String group_code,String product_code,String ak, String sk) {

        try{
            //检查ak,sk
            check_aksk(product_code, ak, sk);

            PermissionDimensionInfo permissionDimensionInfo=new PermissionDimensionInfo();
            permissionDimensionInfo.setProduct_code(product_code);
            permissionDimensionInfo.setIs_delete(Const.NOT_DELETE);

            List<PermissionDimensionInfo> permissionDimensionInfos = permissionUserGroupDimensionValueMapper.selectDimByGroup(product_code, group_code);

            //返回统一信息
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "查询成功", permissionDimensionInfos);
        }catch (Exception e){
            String error = "类:" + Thread.currentThread().getStackTrace()[1].getClassName() + " 函数:" + Thread.currentThread().getStackTrace()[1].getMethodName() + " 异常: {}";
            logger.error(error, e);
            return ReturnInfo.buildError( "查询失败", e);
        }
    }

    /**
     * 获取用户在产品线下所有维度值
     * @param product_code 产品代码
     * @param ak  ak
     * @param sk  sk
     * @return
     */
    @RequestMapping(value = "get_usergroup_dimension_value_list_by_product", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<List<PermissionUserGroupDimensionValueInfo>> get_usergroup_dimension_value_list_by_product(String group_code, String product_code,String ak, String sk) {

        try{
            //检查ak,sk
            check_aksk(product_code, ak, sk);

            PermissionUserGroupDimensionValueInfo permissionUserGroupDimensionValueInfo=new PermissionUserGroupDimensionValueInfo();
            permissionUserGroupDimensionValueInfo.setProduct_code(product_code);
            permissionUserGroupDimensionValueInfo.setGroup_code(group_code);
            permissionUserGroupDimensionValueInfo.setIs_delete(Const.NOT_DELETE);

            List<PermissionUserGroupDimensionValueInfo> permissionUserGroupDimensionValueInfos = permissionUserGroupDimensionValueMapper.select(permissionUserGroupDimensionValueInfo);

            //返回统一信息
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "查询成功", permissionUserGroupDimensionValueInfos);
        }catch (Exception e){
            String error = "类:" + Thread.currentThread().getStackTrace()[1].getClassName() + " 函数:" + Thread.currentThread().getStackTrace()[1].getMethodName() + " 异常: {}";
            logger.error(error, e);
            return ReturnInfo.buildError( "查询失败", e);
        }
    }

    private void check_exist_role(String product_code, String code) throws Exception {

        RoleInfo roleInfo=new RoleInfo();
        roleInfo.setProduct_code(product_code);
        roleInfo.setCode(code);
        roleInfo = roleDao.selectOne(roleInfo);
        if(roleInfo != null){
            throw new Exception("角色code已经存在");
        }

    }

    private void check_role(RoleInfo roleInfo) throws Exception {
        if(StringUtils.isEmpty(roleInfo.getProduct_code())){
            throw new Exception("产品为空");
        }
        if(StringUtils.isEmpty(roleInfo.getCode())){
            throw new Exception("角色code为空");
        }

        if(StringUtils.isEmpty(roleInfo.getName())){
            throw new Exception("角色名称为空");
        }
    }


    private void check_exist_user(String product_code, String user_account) throws Exception {

        PermissionUserInfo permissionUserInfo=new PermissionUserInfo();
        permissionUserInfo.setProduct_code(product_code);
        permissionUserInfo.setUser_account(user_account);
        List<PermissionUserInfo> permissionUserInfos = permissionMapper.select(permissionUserInfo);
        if(permissionUserInfos!= null && permissionUserInfos.size()>0){
            throw new Exception("用户账号已经存在");
        }

    }

    private void check_user(PermissionUserInfo permissionUserInfo) throws Exception {
        if(permissionUserInfo == null){
            throw new Exception("用户信息为空");
        }

        if(StringUtils.isEmpty(permissionUserInfo.getUser_account())){
            throw new Exception("用户账户为空");
        }
        if(StringUtils.isEmpty(permissionUserInfo.getUser_name())){
            throw new Exception("用户名为空");
        }

    }


    /**
     * ak,sk 结构md5_秒级时间戳
     * @param product_code
     * @param ak
     * @param sk
     * @throws Exception
     */
    private void check_aksk(String product_code,String ak, String sk) throws Exception {
        if(StringUtils.isEmpty(product_code)){
            throw new Exception("产品为空");
        }
        if(StringUtils.isEmpty(ak)){
            throw new Exception("AK为空");
        }
        if(StringUtils.isEmpty(sk)){
            throw new Exception("SK为空");
        }

        //解密ak,sk ,根据时间
//        String akdec = Encrypt.AESdecrypt(ak);
//        String[] aks = akdec.split("_");
//        if(aks.length!=2 || (System.currentTimeMillis()/1000 - Integer.parseInt(aks[1])) <= 300 ){
//            //throw new Exception("AK异常");
//        }

//        String skdec = Encrypt.AESdecrypt(ak);
//        String[] sks = skdec.split("_");
//        if(sks.length!=2 || (System.currentTimeMillis()/1000 - Integer.parseInt(sks[1])) <= 300  ){
//           // throw new Exception("SK异常");
//        }

        //验证ak,sk
        ProductTagInfo productTagInfo=new ProductTagInfo();
        productTagInfo.setProduct_code(product_code);
        productTagInfo.setAk(ak);
        productTagInfo.setSk(sk);
        productTagInfo.setStatus(Const.PRODUCT_ENABLE);
        ProductTagInfo pti = productTagMapper.selectOne(productTagInfo);
        if(pti == null){
            throw new Exception("无效的ak/sk,请确认产品与ak/sk是否匹配");
        }
    }


    private String aes(String password,String password_key,String iv){
        AES aes = new AES(Mode.CBC, Padding.PKCS5Padding, password_key.getBytes(), iv.getBytes());

        // 加密并进行Base转码
        String encrypt = aes.decryptStr(password);
        return encrypt;
    }
    /**
     * 验证token 是否有效
     * @param token
     * @return
     */
    private boolean valid(String token) {
        try {
            Session session = sessionDao.readSession(token);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

}
