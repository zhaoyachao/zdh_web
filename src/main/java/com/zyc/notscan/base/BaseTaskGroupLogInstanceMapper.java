package com.zyc.notscan.base;

import com.zyc.notscan.BaseMapper;


public interface BaseTaskGroupLogInstanceMapper<T> extends BaseMapper<T> {
    @Override
    default String getTable(){
        return "task_group_log_instance";
    }
}