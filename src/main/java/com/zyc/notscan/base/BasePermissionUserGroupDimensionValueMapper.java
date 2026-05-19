package com.zyc.notscan.base;

import com.zyc.notscan.BaseMapper;

public interface BasePermissionUserGroupDimensionValueMapper<T> extends BaseMapper<T> {
    @Override
    default String getTable(){
        return "permission_usergroup_dimension_value_info";
    }
}