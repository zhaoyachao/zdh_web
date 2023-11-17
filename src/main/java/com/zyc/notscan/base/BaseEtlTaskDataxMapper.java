package com.zyc.notscan.base;

import com.zyc.notscan.BaseMapper;

public interface BaseEtlTaskDataxMapper<T> extends BaseMapper<T> {
    @Override
    default String getTable(){
        return "etl_task_datax_info";
    }
}