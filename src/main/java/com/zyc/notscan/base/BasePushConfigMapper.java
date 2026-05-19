package com.zyc.notscan.base;

import com.zyc.notscan.BaseMapper;

public interface BasePushConfigMapper<T> extends BaseMapper<T> {
    @Override
    default String getTable(){
        return "push_config_info";
    }
}