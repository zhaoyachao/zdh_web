package com.zyc.notscan.base;

import com.zyc.notscan.BaseMapper;

public interface BaseServerTaskInstanceMappeer<T> extends BaseMapper<T> {
    @Override
    default String getTable(){
        return "server_task_instance";
    }
}