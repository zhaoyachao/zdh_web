package com.zyc.notscan.base;

import com.zyc.notscan.BaseMapper;

public interface BaseProjectMapper<T> extends BaseMapper<T> {
    @Override
    default String getTable(){
        return "project_info";
    }
}