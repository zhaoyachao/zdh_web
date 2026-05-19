package com.zyc.notscan.base;

import com.zyc.notscan.BaseMapper;

public interface BaseTouchConfigMapper<T> extends BaseMapper<T> {
    @Override
    default String getTable(){
        return "touch_config_info";
    }
}