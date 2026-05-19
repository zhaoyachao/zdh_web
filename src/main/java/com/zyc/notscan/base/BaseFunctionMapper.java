package com.zyc.notscan.base;

import com.zyc.notscan.BaseMapper;

public interface BaseFunctionMapper<T> extends BaseMapper<T> {
    @Override
    default String getTable(){
        return "function_info";
    }
}