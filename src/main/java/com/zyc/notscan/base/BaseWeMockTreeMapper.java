package com.zyc.notscan.base;

import com.zyc.notscan.BaseMapper;

public interface BaseWeMockTreeMapper<T> extends BaseMapper<T> {
    @Override
    default String getTable(){
        return "we_mock_tree_info";
    }
}