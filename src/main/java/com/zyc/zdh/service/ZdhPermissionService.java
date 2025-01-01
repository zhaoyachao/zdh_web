package com.zyc.zdh.service;

import com.zyc.zdh.entity.PermissionDimensionValueInfo;
import com.zyc.zdh.entity.PermissionUserDimensionValueInfo;
import com.zyc.zdh.entity.UserAndGroupPermissionDimensionValueInfo;
import com.zyc.zdh.service.impl.ZdhPermissionServiceImpl;

import java.util.List;
import java.util.Map;

public interface ZdhPermissionService {

    public Map<String, Map<String,String>> get_dim_value_attr_by_user_account(String product_code, String user_account, String user_group, String dim_code) throws Exception;

    public List<PermissionDimensionValueInfo> get_dim_value_list(String product_code, String dim_code) throws Exception;

    public List<PermissionDimensionValueInfo> get_dim_value_list_by_user(String product_code,String user_account, String user_group, String dim_code) throws Exception;

    public List<PermissionDimensionValueInfo> get_dim_product(String product_code,String user_account, String user_group) throws Exception;

    public List<PermissionDimensionValueInfo> get_dim_group(String product_code,String user_account, String user_group) throws Exception;

    public List<String> dim_value2code(List<PermissionDimensionValueInfo> permissionDimensionValueInfos) throws Exception;

    public String get_user_group_by_user(String product_code, String user_account) throws Exception;

    /**
     * 根据账号和用户组获取绑定维度信息
     * @param user_account
     * @param user_group
     * @return
     * @throws Exception
     */
    public UserAndGroupPermissionDimensionValueInfo get_dim_permission(String product_code, String user_account, String user_group) throws Exception;
}
