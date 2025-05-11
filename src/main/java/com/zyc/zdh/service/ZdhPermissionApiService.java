package com.zyc.zdh.service;

import com.zyc.zdh.entity.*;

import java.util.List;
import java.util.Map;


public interface ZdhPermissionApiService {

    /**
     * 申请产品 获取ak,sk, 暂未实现
     * @param user_account 用户账号
     * @param product_code 产品代码
     * @param ak  ak
     * @param sk  sk
     * @return
     */
    public ReturnInfo apply_product_by_user(String product_code, String ak, String sk, String user_account);

    /**
     * 新增用户
     * @param product_code 产品代码
     * @param ak  ak
     * @param sk  sk
     * @param permissionUserInfo
     * @return
     */
    public ReturnInfo add_user_by_product(String product_code,String ak, String sk, PermissionUserInfo permissionUserInfo) ;

    /**
     * 更新用户信息
     * @param product_code 产品代码
     * @param ak  ak
     * @param sk  sk
     * @param permissionUserInfo
     * @return
     */
    public ReturnInfo update_user_by_product(String product_code,String ak, String sk, PermissionUserInfo permissionUserInfo) ;


    /**
     * 启用/禁用用户
     * @param product_code 产品代码
     * @param ak  ak
     * @param sk  sk
     * @param user_account 用户账号
     * @param enable 是否启用true/false
     * @return
     */
    public ReturnInfo enable_user_by_product(String product_code,String ak, String sk, String user_account, String enable);

    /**
     * 批量启用/禁用用户
     * @param product_code 产品代码
     * @param ak  ak
     * @param sk  sk
     * @param user_account 用户账号
     * @param enable 是否启用true/false
     * @return
     */
    public ReturnInfo update_batch_user_by_product(String product_code,String ak, String sk, String[] user_account, String enable) ;


    /**
     * 根据用户名密码获取用户信息
     * @param product_code 产品代码
     * @param ak  ak
     * @param sk  sk
     * @param user_account 用户账号
     * @return
     */
    public ReturnInfo auth(String product_code, String ak, String sk, String user_account, String password, String auth_type,
                           Map<String, List<String>> in_auths, Map<String,List<String>> out_auths) ;

    /**
     * 根据用户名密码获取用户信息
     * @param product_code 产品代码
     * @param ak  ak
     * @param sk  sk
     * @param user_account 用户账号
     * @return
     */
    public ReturnInfo<PermissionUserInfo> get_user_by_product_password(String product_code,String ak, String sk, String user_account,String password) ;


    /**
     * 获取用户信息
     * @param product_code 产品代码
     * @param ak  ak
     * @param sk  sk
     * @param user_account 用户账号
     * @return
     */
    public ReturnInfo<PermissionUserInfo> get_user_by_product(String product_code,String ak, String sk, String user_account) ;


    /**
     * 批量获取用户信息
     * @param product_code 产品代码
     * @param ak  ak
     * @param sk  sk
     * @param user_account 用户账号
     * @return
     */
    public ReturnInfo<List<PermissionUserInfo>> get_user_list_by_product_users(String product_code,String ak, String sk, String[] user_account);

    /**
     * 获取产品下所有用户
     * @param product_code 产品代码
     * @param ak  ak
     * @param sk  sk
     * @return
     */
    public ReturnInfo<List<PermissionUserInfo>> get_user_by_product(String product_code,String ak, String sk) ;


    /**
     * 新增用户组
     * @param product_code 产品代码
     * @param ak  ak
     * @param sk  sk
     * @param user_group 用户组code
     * @return
     */
    public ReturnInfo add_user_group_by_product(String product_code,String ak, String sk, String user_group) ;


    /**
     * 增加角色
     * @param product_code 产品代码
     * @param ak  ak
     * @param sk  sk
     * @param role_info
     * @return
     */
    public ReturnInfo add_role_by_product(String product_code,String ak, String sk, RoleInfo role_info);

    /**
     * 禁用/启用 角色
     * @param product_code 产品代码
     * @param ak  ak
     * @param sk  sk
     * @param role_code
     * @param enable true/false
     * @return
     */
    public ReturnInfo enable_role_by_product(String product_code,String ak, String sk, String role_code, String enable);

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
    public ReturnInfo enable_role_by_product(String product_code,String ak, String sk, String role_code, String[] resource_ids);

    /**
     * 根据role_code 获取角色
     * @param product_code 产品代码
     * @param ak  ak
     * @param sk  sk
     * @param role_code 角色code
     * @return
     */
    public ReturnInfo<RoleInfo> get_role_by_product(String product_code,String ak, String sk, String role_code) ;

    /**
     * 获取产品线下所有角色
     * @param product_code 产品代码
     * @param ak  ak
     * @param sk  sk
     * @return
     */
    public ReturnInfo<List<RoleInfo>> get_role_list_by_product(String product_code,String ak, String sk);

    /**
     * 获取角色下的用户列表
     * @param product_code 产品代码
     * @param role_code 角色code
     * @param ak ak
     * @param sk sk
     * @return
     */
    public ReturnInfo<List<PermissionUserInfo>> get_user_list_by_product_role(String product_code,String ak, String sk,String role_code) ;


    /**
     * 新增资源
     * @param product_code 产品代码
     * @param ak  ak
     * @param sk  sk
     * @param resource_tree_info
     * @return
     */
    public ReturnInfo add_resource_by_product(String product_code,String ak, String sk, ResourceTreeInfo resource_tree_info);

    /**
     * 批量增加资源
     * @param product_code 产品代码
     * @param ak  ak
     * @param sk  sk
     * @param resource_tree_info
     * @return
     */
    public ReturnInfo add_batch_resource_by_product(String product_code,String ak, String sk, List<ResourceTreeInfo> resource_tree_info) ;


    /**
     * 通过用户账户 获取资源信息
     * @param user_account 用户账号
     * @param product_code 产品代码
     * @param ak  ak
     * @param sk  sk
     * @return
     */
    public ReturnInfo<List<UserResourceInfo2>> resources_by_user(String product_code,String ak, String sk, String user_account) ;

    /**
     * 通过角色code获取资源
     * @param product_code 产品代码
     * @param ak  ak
     * @param sk  sk
     * @param role_code 角色code
     * @return
     */
    public ReturnInfo resources_by_role(String product_code,String ak, String sk, String role_code) ;


    /**
     * 获取产品线下所有维度code
     * @param product_code 产品代码
     * @param ak  ak
     * @param sk  sk
     * @return
     */
    public ReturnInfo<List<PermissionDimensionInfo>> get_dimension_list_by_product(String product_code,String ak, String sk) ;


    /**
     * 获取产品线下所有维度值
     * @param product_code 产品代码
     * @param ak  ak
     * @param sk  sk
     * @return
     */
    public ReturnInfo<List<PermissionDimensionValueInfo>> get_dimension_value_list_by_product(String product_code,String ak, String sk) ;


    /**
     * 获取用户在产品线下绑定的维度信息
     * @param product_code 产品代码
     * @param ak  ak
     * @param sk  sk
     * @return
     */
    public ReturnInfo<List<PermissionDimensionInfo>> get_user_dimension_list_by_product(String product_code,String ak, String sk, String user_account) ;

    /**
     * 获取用户在产品线下所有维度值
     * @param product_code 产品代码
     * @param ak  ak
     * @param sk  sk
     * @param user_account
     * @return
     */
    public ReturnInfo<List<PermissionUserDimensionValueInfo>> get_user_dimension_value_list_by_product(String product_code,String ak, String sk, String user_account) ;

    /**
     * 获取用户组在产品线下绑定所有维度信息
     * @param product_code 产品代码
     * @param ak  ak
     * @param sk  sk
     * @param group_code 用户组code
     * @return
     */
    public ReturnInfo<List<PermissionDimensionInfo>> get_usergroup_dimension_list_by_product(String product_code,String ak, String sk, String group_code) ;

    /**
     * 获取用户组在产品线下所有维度值信息
     * @param product_code 产品代码
     * @param ak  ak
     * @param sk  sk
     * @param group_code 用户组code
     * @return
     */
    public ReturnInfo<List<PermissionUserGroupDimensionValueInfo>> get_usergroup_dimension_value_list_by_product(String product_code,String ak, String sk, String group_code);

}
