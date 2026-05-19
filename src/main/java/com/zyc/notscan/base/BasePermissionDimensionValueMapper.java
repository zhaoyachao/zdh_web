package com.zyc.notscan.base;

import com.zyc.notscan.BaseMapper;

public interface BasePermissionDimensionValueMapper<T> extends BaseMapper<T> {
    @Override
    default String getTable(){
        return "permission_dimension_value_info";
    }
}