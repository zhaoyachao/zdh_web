package com.zyc.notscan.base;

import com.zyc.notscan.BaseMapper;

public interface BaseCrowdFileMapper<T> extends BaseMapper<T> {
    @Override
    default String getTable(){
        return "crowd_file_info";
    }
}