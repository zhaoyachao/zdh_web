package com.zyc.notscan.base;

import com.zyc.notscan.BaseMapper;


public interface BaseTaskDao<T> extends BaseMapper<T> {
    @Override
    default String getTable(){
        return "task_info";
    }
}