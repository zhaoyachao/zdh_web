package com.zyc.notscan.secondary;

import com.zyc.notscan.BaseMapper;

public interface SecondaryBaseTestMapper<T> extends BaseMapper<T> {
    @Override
    default String getTable(){
        return "test_info";
    }
}