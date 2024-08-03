package com.zyc.notscan.base;

import com.zyc.notscan.BaseMapper;

public interface BasePushAppMapper<T> extends BaseMapper<T> {
    @Override
    default String getTable(){
        return "push_app_info";
    }
}