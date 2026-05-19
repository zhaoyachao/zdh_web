package com.zyc.notscan.base;

import com.zyc.notscan.BaseMapper;

public interface BaseWeMockDataMapper<T> extends BaseMapper<T> {
    @Override
    default String getTable(){
        return "we_mock_data_info";
    }
}