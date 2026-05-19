package com.zyc.notscan.base;

import com.zyc.notscan.BaseMapper;

public interface BaseEnumMapper<T> extends BaseMapper<T> {
    @Override
    default String getTable(){
        return "enum_info";
    }
}