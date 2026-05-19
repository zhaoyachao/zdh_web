package com.zyc.notscan.base;

import com.zyc.notscan.BaseMapper;

public interface BaseDataTagMapper<T> extends BaseMapper<T> {
    @Override
    default String getTable(){
        return "data_tag_info";
    }
}