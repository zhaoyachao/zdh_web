package com.zyc.notscan.base;

import com.zyc.notscan.BaseMapper;

public interface BaseQualityTaskMapper<T> extends BaseMapper<T> {
    @Override
    default String getTable(){
        return "quality_task_info";
    }
}