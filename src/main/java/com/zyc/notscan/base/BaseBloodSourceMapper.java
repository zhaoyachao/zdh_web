package com.zyc.notscan.base;

import com.zyc.notscan.BaseMapper;

public interface BaseBloodSourceMapper<T> extends BaseMapper<T> {
    @Override
    default String getTable(){
        return "blood_source_info";
    }
}