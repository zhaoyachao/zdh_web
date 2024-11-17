package com.zyc.notscan.base;

import com.zyc.notscan.BaseMapper;

public interface BaseDataCodeMapper<T> extends BaseMapper<T> {
    @Override
    default String getTable(){
        return "data_code_info";
    }
}