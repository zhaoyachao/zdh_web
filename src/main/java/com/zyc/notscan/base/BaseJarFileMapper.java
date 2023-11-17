package com.zyc.notscan.base;

import com.zyc.notscan.BaseMapper;


public interface BaseJarFileMapper<T> extends BaseMapper<T> {
    @Override
    default String getTable(){
        return "jar_file_info";
    }
}