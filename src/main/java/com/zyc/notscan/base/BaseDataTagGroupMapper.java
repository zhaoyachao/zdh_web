package com.zyc.notscan.base;

import com.zyc.notscan.BaseMapper;

public interface BaseDataTagGroupMapper<T> extends BaseMapper<T> {
    @Override
    default String getTable(){
        return "data_tag_group_info";
    }
}