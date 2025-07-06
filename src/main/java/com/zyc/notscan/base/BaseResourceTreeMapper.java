package com.zyc.notscan.base;

import com.zyc.notscan.BaseMapper;


public interface BaseResourceTreeMapper<T> extends BaseMapper<T> {
    @Override
    default String getTable(){
        return "role_resource_info";
    }
}