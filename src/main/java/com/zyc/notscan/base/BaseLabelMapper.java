package com.zyc.notscan.base;

import com.zyc.notscan.BaseMapper;

public interface BaseLabelMapper<T> extends BaseMapper<T> {
    @Override
    default String getTable(){
        return "label_info";
    }
}