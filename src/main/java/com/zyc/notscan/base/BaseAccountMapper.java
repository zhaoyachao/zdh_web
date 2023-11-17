package com.zyc.notscan.base;

import com.zyc.notscan.BaseMapper;

public interface BaseAccountMapper<T> extends BaseMapper<T> {
    @Override
    default String getTable(){
        return "account_info";
    }
}