package com.zyc.notscan.base;

import com.zyc.notscan.BaseMapper;

public interface BasePermissionBigdataMapper<T> extends BaseMapper<T> {
    @Override
    default String getTable(){
        return "permission_bigdata_info";
    }
}