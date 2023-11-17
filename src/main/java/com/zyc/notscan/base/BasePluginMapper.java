package com.zyc.notscan.base;

import com.zyc.notscan.BaseMapper;

public interface BasePluginMapper<T> extends BaseMapper<T> {
    @Override
    default String getTable(){
        return "plugin_info";
    }
}