package com.zyc.notscan.base;

import com.zyc.notscan.BaseMapper;

public interface BaseEtlTaskBatchMapper<T> extends BaseMapper<T> {
    @Override
    default String getTable(){
        return "etl_task_batch_info";
    }
}