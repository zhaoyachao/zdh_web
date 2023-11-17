package com.zyc.zdh.api;

import com.zyc.zdh.controller.PermissionApiController;
import com.zyc.zdh.entity.*;
import com.zyc.zdh.shiro.SessionDao;
import org.apache.shiro.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

/**
 * api权限服务
 *
 * ak,sk都是加密后的,接收到ak,sk需要提前解密验证
 * @author zyc-admin
 * @date 2018年2月5日
 * @Description: api包下的服务 不需要通过shiro验证拦截，需要自定义的token验证, 通过此api 提供外部平台权限验证
 */
@Controller("permissionApi")
@RequestMapping("api")
public class PermissionApi {

    public Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SessionDao sessionDao;
    @Autowired
    private PermissionApiController permissionApiController;



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
            return permissionApiController.apply_product_by_user(user_account, product_code, ak, sk);
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
            return permissionApiController.add_user_by_product(product_code, ak, sk, permissionUserInfo);
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
            return permissionApiController.update_user_by_product(product_code, ak, sk, permissionUserInfo);
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
            return permissionApiController.enable_user_by_product(product_code, ak, sk, user_account, enable);
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
            return permissionApiController.update_batch_user_by_product(product_code, ak, sk, user_account, enable);
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
            return permissionApiController.auth(product_code, ak, sk, user_account, password, auth_type,
                     in_auths,  out_auths);
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
    @RequestMapping(value = "get_user_by_product_password", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<PermissionUserInfo> get_user_by_product_password(String product_code,String ak, String sk, String user_account,String password) {

        try{
            return permissionApiController.get_user_by_product_password(product_code, ak, sk, user_account, password);
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
            return permissionApiController.get_user_by_product(product_code, ak, sk, user_account);
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
            return permissionApiController.get_user_list_by_product_users(product_code, ak, sk, user_account);
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
            return permissionApiController.get_user_by_product(product_code, ak, sk);
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
            return permissionApiController.add_user_group_by_product(product_code, ak, sk, user_group);
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
            return permissionApiController.add_role_by_product(product_code, ak, sk, role_info);
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
            return permissionApiController.enable_role_by_product( product_code, ak, sk, role_info);
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
    public ReturnInfo enable_role_by_product(String product_code,String ak, String sk, String role_id, String[] resource_ids) {

        try{
            return permissionApiController.enable_role_by_product( product_code, ak,  sk,  role_id, resource_ids);
        }catch (Exception e){
            String error = "类:" + Thread.currentThread().getStackTrace()[1].getClassName() + " 函数:" + Thread.currentThread().getStackTrace()[1].getMethodName() + " 异常: {}";
            logger.error(error, e);

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
            return permissionApiController.get_role_by_product(product_code, ak, sk, role_code);
        }catch (Exception e){
            String error = "类:" + Thread.currentThread().getStackTrace()[1].getClassName() + " 函数:" + Thread.currentThread().getStackTrace()[1].getMethodName() + " 异常: {}";
            logger.error(error, e);

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
            return permissionApiController.get_role_by_product(product_code, ak, sk);
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
            return permissionApiController.get_user_list_by_product_role(product_code, role_code, ak, sk);
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
            return permissionApiController.add_resource_by_product(product_code, ak, sk, resource_tree_info);
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
    public ReturnInfo add_batch_resource_by_product(String product_code,String ak, String sk, @RequestBody List<ResourceTreeInfo> resource_tree_info) {

        try{
            return permissionApiController.add_batch_resource_by_product(product_code, ak, sk, resource_tree_info);
        }catch (Exception e){
            String error = "类:" + Thread.currentThread().getStackTrace()[1].getClassName() + " 函数:" + Thread.currentThread().getStackTrace()[1].getMethodName() + " 异常: {}";
            logger.error(error, e);
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
            return permissionApiController.resources_by_user(user_account, product_code, ak, sk);
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
            return permissionApiController.resources_by_role(role_code, product_code, ak, sk);
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
            return permissionApiController.add_data_tag_by_product(product_code, ak, sk, dataTagInfo);
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
            return permissionApiController.update_data_tag_by_product(product_code, ak, sk, dataTagInfo);
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
            return permissionApiController.add_data_tag_group_by_product(product_code, ak, sk, dataTagGroupInfo);
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
            return permissionApiController.update_data_tag_group_by_product(product_code, ak, sk, dataTagGroupInfo);
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
            return permissionApiController.get_dimension_list_by_product(product_code, ak, sk);
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
            return permissionApiController.get_dimension_value_list_by_product(product_code, ak, sk);
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
            return permissionApiController.get_user_dimension_list_by_product(user_account, product_code, ak, sk);
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
            return permissionApiController.get_user_dimension_value_list_by_product(user_account, product_code, ak, sk);
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
            return permissionApiController.get_usergroup_dimension_list_by_product(group_code, product_code, ak, sk);
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
            return permissionApiController.get_usergroup_dimension_value_list_by_product(group_code, product_code, ak, sk);
        }catch (Exception e){
            String error = "类:" + Thread.currentThread().getStackTrace()[1].getClassName() + " 函数:" + Thread.currentThread().getStackTrace()[1].getMethodName() + " 异常: {}";
            logger.error(error, e);
            return ReturnInfo.buildError( "查询失败", e);
        }
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
