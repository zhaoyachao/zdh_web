package com.zyc.notscan.base;

import com.zyc.notscan.BaseMapper;

public interface BaseEtlTaskUnstructureMapper<T> extends BaseMapper<T> {
    @Override
    default String getTable(){
        return "etl_task_unstructure_info";
    }
}