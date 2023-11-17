package com.zyc.notscan.base;

import com.zyc.notscan.BaseMapper;

public interface BaseQualityMapper<T> extends BaseMapper<T> {
    @Override
    default String getTable(){
        return "quality";
    }
}