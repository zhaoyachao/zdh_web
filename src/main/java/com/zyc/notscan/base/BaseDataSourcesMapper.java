package com.zyc.notscan.base;

import com.zyc.notscan.BaseMapper;

public interface BaseDataSourcesMapper<T> extends BaseMapper<T> {
    @Override
    default String getTable(){
        return "data_sources_info";
    }
}