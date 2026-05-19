package com.zyc.notscan.base;

import com.zyc.notscan.BaseMapper;

public interface BaseServerTaskMappeer<T> extends BaseMapper<T> {
    @Override
    default String getTable(){
        return "server_task_info";
    }
}