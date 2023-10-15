package com.zyc.zdh.service;

import com.zyc.zdh.entity.PermissionDimensionValueInfo;
import com.zyc.zdh.entity.PermissionUserDimensionValueInfo;

import java.util.List;

public interface ZdhPermissionService {

    public List<PermissionUserDimensionValueInfo> get_dim_value_by_user_account(String user_account, String dim_code) throws Exception;

    public List<PermissionDimensionValueInfo> get_dim_value_list(String dim_code) throws Exception;

    public List<PermissionDimensionValueInfo> get_dim_value_list_by_user(String user_account, String dim_code) throws Exception;

    public List<PermissionDimensionValueInfo> get_dim_product(String user_account) throws Exception;

    public List<PermissionDimensionValueInfo> get_dim_group(String user_account) throws Exception;

    public List<String> dim_value2code(List<PermissionDimensionValueInfo> permissionDimensionValueInfos) throws Exception;
}
