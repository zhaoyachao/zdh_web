package com.zyc.notscan.base;

import com.zyc.notscan.BaseMapper;

public interface BaseFilterMapper<T> extends BaseMapper<T> {
    @Override
    default String getTable(){
        return "filter_info";
    }
}