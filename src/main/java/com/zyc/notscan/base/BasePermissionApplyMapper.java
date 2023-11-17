package com.zyc.notscan.base;

import com.zyc.notscan.BaseMapper;

public interface BasePermissionApplyMapper<T> extends BaseMapper<T> {
    @Override
    default String getTable(){
        return "permission_apply_info";
    }
}