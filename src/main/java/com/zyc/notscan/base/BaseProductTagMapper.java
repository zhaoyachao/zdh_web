package com.zyc.notscan.base;

import com.zyc.notscan.BaseMapper;

public interface BaseProductTagMapper<T> extends BaseMapper<T> {
    @Override
    default String getTable(){
        return "product_tag_info";
    }
}