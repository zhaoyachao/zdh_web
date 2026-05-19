package com.zyc.notscan.base;

import com.zyc.notscan.BaseMapper;

public interface BasePermissionUserDimensionValueMapper<T> extends BaseMapper<T> {
    @Override
    default String getTable(){
        return "permission_user_dimension_value_info";
    }
}