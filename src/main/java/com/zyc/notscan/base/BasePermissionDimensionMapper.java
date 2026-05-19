package com.zyc.notscan.base;

import com.zyc.notscan.BaseMapper;

public interface BasePermissionDimensionMapper<T> extends BaseMapper<T> {
    @Override
    default String getTable(){
        return "permission_dimension_info";
    }
}