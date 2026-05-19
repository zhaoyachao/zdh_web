package com.zyc.notscan.base;

import com.zyc.notscan.BaseMapper;

public interface BaseQuartzExecutorMapper<T> extends BaseMapper<T> {
    @Override
    default String getTable(){
        return "quartz_executor_info";
    }
}