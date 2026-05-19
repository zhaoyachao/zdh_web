package com.zyc.notscan.base;

import com.zyc.notscan.BaseMapper;

public interface BaseParamMapper<T> extends BaseMapper<T> {
    @Override
    default String getTable(){
        return "param_info";
    }
}