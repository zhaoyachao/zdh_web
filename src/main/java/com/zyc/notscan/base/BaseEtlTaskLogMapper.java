package com.zyc.notscan.base;

import com.zyc.notscan.BaseMapper;

public interface BaseEtlTaskLogMapper<T> extends BaseMapper<T> {
    @Override
    default String getTable(){
        return "etl_task_log_info";
    }
}