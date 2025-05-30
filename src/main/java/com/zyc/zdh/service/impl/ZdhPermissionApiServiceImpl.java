package com.zyc.zdh.service.impl;

import cn.hutool.core.util.ReUtil;
import cn.hutool.crypto.Mode;
import cn.hutool.crypto.Padding;
import cn.hutool.crypto.symmetric.AES;
import com.google.common.collect.Lists;
import com.zyc.zdh.dao.*;
import com.zyc.zdh.entity.*;
import com.zyc.zdh.service.ZdhPermissionApiService;
import com.zyc.zdh.shiro.SessionDao;
import com.zyc.zdh.util.ConfigUtil;
import com.zyc.zdh.util.Const;
import com.zyc.zdh.util.LogUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.RequestBody;
import tk.mybatis.mapper.entity.Example;

import java.sql.Timestamp;
import java.util.*;

@Service("zdhPermissionApiService")
public class ZdhPermissionApiServiceImpl implements ZdhPermissionApiService {

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
    private PermissionDimensionMapper permissionDimensionMapper;
    @Autowired
    private PermissionDimensionValueMapper permissionDimensionValueMapper;
    @Autowired
    private PermissionUserDimensionValueMapper permissionUserDimensionValueMapper;
    @Autowired
    private PermissionUserGroupDimensionValueMapper permissionUserGroupDimensionValueMapper;

    /**
     * 申请产品 获取ak,sk, 暂未实现
     * @param user_account 用户账号
     * @param product_code 产品代码
     * @param ak  ak
     * @param sk  sk
     * @return
     */
    @Override
    public ReturnInfo apply_product_by_user(String product_code, String ak, String sk, String user_account) {

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
    @Override
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
    @Override
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
    @Override
    public ReturnInfo enable_user_by_product(String product_code,String ak, String sk, String user_account, String enable) {

        try{
            //检查ak,sk
            check_aksk(product_code, ak, sk);

            check_enable(enable);

            check_user_account(user_account);

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
    @Override
    public ReturnInfo update_batch_user_by_product(String product_code,String ak, String sk, String[] user_account, String enable) {

        try{
            //检查ak,sk
            check_aksk(product_code, ak, sk);

            check_enable(enable);
            for (String u: user_account){
                check_user_account(u);
            }
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
    @Override
    public ReturnInfo auth(String product_code, String ak, String sk, String user_account, String password, String auth_type,
                           Map<String, List<String>> in_auths, Map<String,List<String>> out_auths) {

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
    @Override
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
    @Override
    public ReturnInfo<PermissionUserInfo> get_user_by_product(String product_code,String ak, String sk, String user_account) {

        try{
            //检查ak,sk
            check_aksk(product_code, ak, sk);

            check_user_account(user_account);

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
    @Override
    public ReturnInfo<List<PermissionUserInfo>> get_user_list_by_product_users(String product_code,String ak, String sk, String[] user_account) {

        try{
            //检查ak,sk
            check_aksk(product_code, ak, sk);

            for(String u: user_account){
                check_user_account(u);
            }

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
    @Override
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

            //安全考虑,屏蔽密码
            for (PermissionUserInfo permissionUserInfo: permissionUserInfos){
                permissionUserInfo.setUser_password(null);
            }

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
    @Override
    public ReturnInfo add_user_group_by_product(String product_code,String ak, String sk, String user_group) {

        try{
            //检查ak,sk
            check_aksk(product_code, ak, sk);

            check_user_group(user_group);

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
    @Override
    public ReturnInfo add_role_by_product(String product_code,String ak, String sk, RoleInfo role_info) {

        try{
            //检查ak,sk
            check_aksk(product_code, ak, sk);

            //检查角色信息
            check_role(role_info);

            check_str(role_info.getCode(), "角色code");

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
     * @param role_code
     * @param enable true/false
     * @return
     */
    @Override
    public ReturnInfo enable_role_by_product(String product_code,String ak, String sk, String role_code, String enable) {

        try{
            //检查ak,sk
            check_aksk(product_code, ak, sk);

            check_enable(enable);

            check_str(role_code, "角色code");
            //检查角色信息

            RoleInfo roleInfo = check_exist_role(product_code, role_code);
            roleInfo.setEnable(enable);
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
     * @param role_code 角色code
     * @param resource_ids 资源ID列表
     * @return
     */
    @Override
    @Transactional(propagation= Propagation.NESTED)
    public ReturnInfo enable_role_by_product(String product_code,String ak, String sk, String role_code, String[] resource_ids) {

        try{
            //检查ak,sk
            check_aksk(product_code, ak, sk);

            List<RoleResourceInfo> rris = new ArrayList<>();
            for (String rid : resource_ids) {
                RoleResourceInfo rri = new RoleResourceInfo();
                rri.setRole_code(role_code);
                rri.setResource_id(rid);
                rri.setCreate_time(new Timestamp(System.currentTimeMillis()));
                rri.setUpdate_time(new Timestamp(System.currentTimeMillis()));
                rri.setProduct_code(product_code);
                rris.add(rri);
            }
            resourceTreeMapper.deleteByRoleCode(role_code, product_code);
            resourceTreeMapper.updateUserResource(rris);
            //返回统一信息
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "更新成功", null);
        }catch (Exception e){
            LogUtil.error(this.getClass(), e);
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
    @Override
    public ReturnInfo<RoleInfo> get_role_by_product(String product_code,String ak, String sk, String role_code) {

        try{
            //检查ak,sk
            check_aksk(product_code, ak, sk);

            check_str(role_code, "角色code");

            RoleInfo roleInfo=new RoleInfo();
            roleInfo.setCode(role_code);
            roleInfo.setProduct_code(product_code);
            roleInfo.setEnable(Const.TRUR);

            roleInfo = roleDao.selectOne(roleInfo);
            //返回统一信息
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "查询成功", roleInfo);
        }catch (Exception e){
            LogUtil.error(this.getClass(), e);
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
    @Override
    public ReturnInfo<List<RoleInfo>> get_role_list_by_product(String product_code,String ak, String sk) {

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
            LogUtil.error(this.getClass(), e);
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
    @Override
    public ReturnInfo<List<PermissionUserInfo>> get_user_list_by_product_role(String product_code,String ak, String sk,String role_code) {

        try{
            //检查ak,sk
            check_aksk(product_code, ak, sk);

            check_str(role_code, "角色code");

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
            LogUtil.error(this.getClass(), e);
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
    @Override
    public ReturnInfo add_resource_by_product(String product_code,String ak, String sk, ResourceTreeInfo resource_tree_info) {

        try{
            check_aksk(product_code, ak, sk);
            resource_tree_info.setProduct_code(product_code);

            //检查资源信息 todo 待补充

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
    @Override
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
            LogUtil.error(this.getClass(), e);
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
    @Override
    public ReturnInfo<List<UserResourceInfo2>> resources_by_user(String product_code,String ak, String sk, String user_account) {

        try{
            check_aksk(product_code, ak, sk);
            //验证用户和产品是否匹配 todo 此处需要实现,在前端增加 用户申请产品,通过后记录用户和产品对应关系

            check_user_account(user_account);

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
     * @param product_code 产品代码
     * @param ak  ak
     * @param sk  sk
     * @param role_code 角色code
     * @return
     */
    @Override
    public ReturnInfo resources_by_role(String product_code,String ak, String sk, String role_code) {

        try{
            check_aksk(product_code, ak, sk);
            //验证用户和产品是否匹配 todo 此处需要实现,在前端增加 用户申请产品,通过后记录用户和产品对应关系

            check_str(role_code, "角色code");

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
     * 获取产品线下所有维度code
     * @param product_code 产品代码
     * @param ak  ak
     * @param sk  sk
     * @return
     */
    @Override
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
            LogUtil.error(this.getClass(), e);
            return ReturnInfo.buildError( "查询失败", e);
        }
    }


    /**
     * 获取产品线下所有维度值
     * @param product_code 产品代码
     * @param ak  ak
     * @param sk  sk
     * @return
     */
    @Override
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
            LogUtil.error(this.getClass(), e);
            return ReturnInfo.buildError( "查询失败", e);
        }
    }


    /**
     * 获取用户在产品线下绑定的维度信息
     * @param product_code 产品代码
     * @param ak  ak
     * @param sk  sk
     * @return
     */
    @Override
    public ReturnInfo<List<PermissionDimensionInfo>> get_user_dimension_list_by_product(String product_code,String ak, String sk, String user_account) {

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
            LogUtil.error(this.getClass(), e);
            return ReturnInfo.buildError( "查询失败", e);
        }
    }

    /**
     * 获取用户在产品线下所有维度值
     * @param product_code 产品代码
     * @param ak  ak
     * @param sk  sk
     * @param user_account
     * @return
     */
    @Override
    public ReturnInfo<List<PermissionUserDimensionValueInfo>> get_user_dimension_value_list_by_product(String product_code,String ak, String sk, String user_account) {

        try{
            //检查ak,sk
            check_aksk(product_code, ak, sk);

            check_user_account(user_account);

            PermissionUserDimensionValueInfo permissionUserDimensionValueInfo=new PermissionUserDimensionValueInfo();
            permissionUserDimensionValueInfo.setProduct_code(product_code);
            permissionUserDimensionValueInfo.setUser_account(user_account);
            permissionUserDimensionValueInfo.setIs_delete(Const.NOT_DELETE);

            List<PermissionUserDimensionValueInfo> permissionUserDimensionValueInfos = permissionUserDimensionValueMapper.select(permissionUserDimensionValueInfo);

            //返回统一信息
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "查询成功", permissionUserDimensionValueInfos);
        }catch (Exception e){
            LogUtil.error(this.getClass(), e);
            return ReturnInfo.buildError( "查询失败", e);
        }
    }

    /**
     * 获取用户组在产品线下绑定所有维度信息
     * @param product_code 产品代码
     * @param ak  ak
     * @param sk  sk
     * @param group_code 用户组code
     * @return
     */
    @Override
    public ReturnInfo<List<PermissionDimensionInfo>> get_usergroup_dimension_list_by_product(String product_code,String ak, String sk, String group_code) {

        try{
            //检查ak,sk
            check_aksk(product_code, ak, sk);
            check_user_group(group_code);

            PermissionDimensionInfo permissionDimensionInfo=new PermissionDimensionInfo();
            permissionDimensionInfo.setProduct_code(product_code);
            permissionDimensionInfo.setIs_delete(Const.NOT_DELETE);

            List<PermissionDimensionInfo> permissionDimensionInfos = permissionUserGroupDimensionValueMapper.selectDimByGroup(product_code, group_code);

            //返回统一信息
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "查询成功", permissionDimensionInfos);
        }catch (Exception e){
            LogUtil.error(this.getClass(), e);
            return ReturnInfo.buildError( "查询失败", e);
        }
    }

    /**
     * 获取用户组在产品线下所有维度值信息
     * @param product_code 产品代码
     * @param ak  ak
     * @param sk  sk
     * @param group_code 用户组code
     * @return
     */
    @Override
    public ReturnInfo<List<PermissionUserGroupDimensionValueInfo>> get_usergroup_dimension_value_list_by_product(String product_code,String ak, String sk, String group_code) {

        try{
            //检查ak,sk
            check_aksk(product_code, ak, sk);
            check_user_group(group_code);

            PermissionUserGroupDimensionValueInfo permissionUserGroupDimensionValueInfo=new PermissionUserGroupDimensionValueInfo();
            permissionUserGroupDimensionValueInfo.setProduct_code(product_code);
            permissionUserGroupDimensionValueInfo.setGroup_code(group_code);
            permissionUserGroupDimensionValueInfo.setIs_delete(Const.NOT_DELETE);

            List<PermissionUserGroupDimensionValueInfo> permissionUserGroupDimensionValueInfos = permissionUserGroupDimensionValueMapper.select(permissionUserGroupDimensionValueInfo);

            //返回统一信息
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "查询成功", permissionUserGroupDimensionValueInfos);
        }catch (Exception e){
            LogUtil.error(this.getClass(), e);
            return ReturnInfo.buildError( "查询失败", e);
        }
    }

    private RoleInfo check_exist_role(String product_code, String code) throws Exception {

        RoleInfo roleInfo=new RoleInfo();
        roleInfo.setProduct_code(product_code);
        roleInfo.setCode(code);
        roleInfo = roleDao.selectOne(roleInfo);
        if(roleInfo != null){
            throw new Exception("角色code已经存在");
        }

        return roleInfo;

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

        check_product(product_code);
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

    /**
     * 检查启用/禁用是否合法
     * @param enable
     */
    private void check_enable(String enable){
        if(!Lists.newArrayList(Const.TRUR, Const.FALSE).contains(enable)){
            new Exception("启用/禁用参数不合法");
        }
    }

    /**
     * 检查账号是否合法
     * @param user_acocunt
     */
    private void check_user_account(String user_acocunt){
        if(!ReUtil.isMatch("^[a-zA-Z0-9_]{4,18}$", user_acocunt)){
            new Exception("账号参数不合法");
        }
    }

    /**
     * 检查用户组是否合法
     * @param user_group
     */
    private void check_user_group(String user_group){
        if(!ReUtil.isMatch("^[a-zA-Z0-9_]{2,18}$", user_group)){
            new Exception("用户组参数不合法");
        }
    }

    /**
     * 检查产品是否合法
     * @param product_code
     */
    private void check_product(String product_code){
        if(!ReUtil.isMatch("^[a-zA-Z0-9_]{2,18}$", product_code)){
            new Exception("产品参数不合法");
        }
    }

    /**
     * 通用字符串参数检查
     * @param str
     * @param str_name
     */
    private void check_str(String str, String str_name){
        check_str(str, str_name, "^[a-zA-Z0-9_]{2,18}$");
    }

    /**
     * 通用字符串参数检查
     * @param str
     * @param str_name
     * @param regex
     */
    private void check_str(String str, String str_name, String regex){
        if(!ReUtil.isMatch(regex, str)){
            new Exception(str_name+"参数不合法");
        }
    }

}
