package com.zyc.zdh.controller;

import com.zyc.zdh.entity.*;
import com.zyc.zdh.service.ZdhPermissionApiService;
import com.zyc.zdh.util.LogUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;


/**
 * 权限api 统一业务逻辑处理
 */
@Controller("permissionApiController")
public class PermissionApiController {

    @Autowired
    private ZdhPermissionApiService zdhPermissionApiService;



    /**
     * 申请产品 获取ak,sk, 暂未实现
     * @param user_account 用户账号
     * @param product_code 产品代码
     * @param ak  ak
     * @param sk  sk
     * @return
     */
    @RequestMapping(value = "apply_product_by_user", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo apply_product_by_user(String product_code,String ak, String sk, String user_account) {
        try{
            return zdhPermissionApiService.apply_product_by_user(product_code, ak, sk, user_account);
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
    @RequestMapping(value = "add_user_by_product", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo add_user_by_product(String product_code,String ak, String sk, PermissionUserInfo permissionUserInfo) {

        try{
            return zdhPermissionApiService.add_user_by_product(product_code, ak, sk, permissionUserInfo);
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
    @RequestMapping(value = "update_user_by_product", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo update_user_by_product(String product_code,String ak, String sk, PermissionUserInfo permissionUserInfo) {

        try{
            //返回统一信息
            return zdhPermissionApiService.update_user_by_product(product_code, ak, sk, permissionUserInfo);
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
    @RequestMapping(value = "enable_user_by_product", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo enable_user_by_product(String product_code,String ak, String sk, String user_account, String enable) {

        try{
            //返回统一信息
            return zdhPermissionApiService.enable_user_by_product(product_code, ak, sk, user_account, enable);
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
    @RequestMapping(value = "update_batch_user_by_product", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo update_batch_user_by_product(String product_code,String ak, String sk, String[] user_account, String enable) {

        try{
            return zdhPermissionApiService.update_batch_user_by_product(product_code, ak, sk, user_account, enable);
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
    @RequestMapping(value = "auth", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo auth(String product_code,String ak, String sk, String user_account,String password, String auth_type,
                           Map<String,List<String>> in_auths, Map<String,List<String>> out_auths) {

        try{
            return zdhPermissionApiService.auth(product_code, ak, sk, user_account, password, auth_type, in_auths, out_auths);
        }catch (Exception e){
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "查询失败", e);
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
    @RequestMapping(value = "get_user_by_product_password", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<PermissionUserInfo> get_user_by_product_password(String product_code,String ak, String sk, String user_account,String password) {

        try{
            return zdhPermissionApiService.get_user_by_product_password(product_code, ak, sk, user_account, password);
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
    @RequestMapping(value = "get_user_by_product", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<PermissionUserInfo> get_user_by_product(String product_code,String ak, String sk, String user_account) {

        try{
            return zdhPermissionApiService.get_user_by_product(product_code, ak, sk, user_account);
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
    @RequestMapping(value = "get_user_by_product_user", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<List<PermissionUserInfo>> get_user_list_by_product_users(String product_code,String ak, String sk, String[] user_account) {

        try{
            return zdhPermissionApiService.get_user_list_by_product_users(product_code, ak, sk, user_account);
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
    @RequestMapping(value = "get_user_list_by_product", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<List<PermissionUserInfo>> get_user_by_product(String product_code,String ak, String sk) {

        try{
            return zdhPermissionApiService.get_user_by_product(product_code, ak, sk);
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
    @RequestMapping(value = "add_user_group_by_product", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo add_user_group_by_product(String product_code,String ak, String sk, String user_group) {

        try{
            return zdhPermissionApiService.add_user_group_by_product(product_code, ak, sk, user_group);
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
    @RequestMapping(value = "add_role_by_product", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo add_role_by_product(String product_code,String ak, String sk, RoleInfo role_info) {

        try{
            return zdhPermissionApiService.add_role_by_product(product_code, ak, sk, role_info);
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
    @RequestMapping(value = "enable_role_by_product", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo enable_role_by_product(String product_code,String ak, String sk, String role_code, String enable) {

        try{
            //返回统一信息
            return zdhPermissionApiService.enable_role_by_product(product_code, ak, sk, role_code, enable);
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
    @RequestMapping(value = "add_resource_in_role_by_product", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public ReturnInfo enable_role_by_product(String product_code,String ak, String sk, String role_code, String[] resource_ids) {

        try{
            return zdhPermissionApiService.enable_role_by_product(product_code, ak, sk, role_code, resource_ids);
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
    @RequestMapping(value = "get_role_by_product", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<RoleInfo> get_role_by_product(String product_code,String ak, String sk, String role_code) {

        try{
            return zdhPermissionApiService.get_role_by_product(product_code, ak, sk, role_code);
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
    @RequestMapping(value = "get_role_list_by_product", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<List<RoleInfo>> get_role_list_by_product(String product_code,String ak, String sk) {

        try{
            return zdhPermissionApiService.get_role_list_by_product(product_code, ak, sk);
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
    @RequestMapping(value = "get_user_list_by_product_role", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<List<PermissionUserInfo>> get_user_list_by_product_role(String product_code,String ak, String sk,String role_code) {

        try{
            return zdhPermissionApiService.get_user_list_by_product_role(product_code, ak, sk, role_code);
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
    @RequestMapping(value = "add_resource_by_product", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo add_resource_by_product(String product_code,String ak, String sk, ResourceTreeInfo resource_tree_info) {

        try{
            return zdhPermissionApiService.add_resource_by_product(product_code, ak, sk, resource_tree_info);
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
    @RequestMapping(value = "add_batch_resource_by_product", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public ReturnInfo add_batch_resource_by_product(String product_code,String ak, String sk, @RequestBody List<ResourceTreeInfo> resource_tree_info) {

        try{
            return zdhPermissionApiService.add_batch_resource_by_product(product_code, ak, sk, resource_tree_info);
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
    @RequestMapping(value = "resources_by_user", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<List<UserResourceInfo2>> resources_by_user(String product_code,String ak, String sk, String user_account) {

        try{
            return zdhPermissionApiService.resources_by_user(product_code, ak, sk, user_account);
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
    @RequestMapping(value = "resources_by_role", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo resources_by_role(String product_code,String ak, String sk, String role_code) {

        try{
            return zdhPermissionApiService.resources_by_user(product_code, ak, sk, role_code);
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
    @RequestMapping(value = "get_dimension_list_by_product", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<List<PermissionDimensionInfo>> get_dimension_list_by_product(String product_code,String ak, String sk) {

        try{
            return zdhPermissionApiService.get_dimension_list_by_product(product_code, ak, sk);
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
    @RequestMapping(value = "get_dimension_value_list_by_product", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<List<PermissionDimensionValueInfo>> get_dimension_value_list_by_product(String product_code,String ak, String sk) {

        try{
            return zdhPermissionApiService.get_dimension_value_list_by_product(product_code, ak, sk);
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
    @RequestMapping(value = "get_user_dimension_list_by_product", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<List<PermissionDimensionInfo>> get_user_dimension_list_by_product(String product_code,String ak, String sk, String user_account) {

        try{
            return zdhPermissionApiService.get_user_dimension_list_by_product(product_code, ak, sk, user_account);
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
    @RequestMapping(value = "get_user_dimension_value_list_by_product", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<List<PermissionUserDimensionValueInfo>> get_user_dimension_value_list_by_product(String product_code,String ak, String sk, String user_account) {

        try{
            return zdhPermissionApiService.get_user_dimension_value_list_by_product(product_code, ak, sk, user_account);
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
    @RequestMapping(value = "get_usergroup_dimension_list_by_product", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<List<PermissionDimensionInfo>> get_usergroup_dimension_list_by_product(String product_code,String ak, String sk, String group_code) {

        try{
            return zdhPermissionApiService.get_usergroup_dimension_list_by_product(product_code, ak, sk, group_code);
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
    @RequestMapping(value = "get_usergroup_dimension_value_list_by_product", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<List<PermissionUserGroupDimensionValueInfo>> get_usergroup_dimension_value_list_by_product(String product_code,String ak, String sk, String group_code) {

        try{
            return zdhPermissionApiService.get_usergroup_dimension_value_list_by_product(product_code, ak, sk, group_code);
        }catch (Exception e){
            LogUtil.error(this.getClass(), e);
            return ReturnInfo.buildError( "查询失败", e);
        }
    }
}
