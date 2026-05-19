package com.zyc.notscan.base;

import com.zyc.notscan.BaseMapper;

public interface BaseEtlTaskUnstructureLogMapper<T> extends BaseMapper<T> {
    @Override
    default String getTable(){
        return "etl_task_unstructure_log_info";
    }
}