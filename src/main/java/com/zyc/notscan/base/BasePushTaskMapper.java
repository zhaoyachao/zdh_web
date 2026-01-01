package com.zyc.notscan.base;

import com.zyc.notscan.BaseMapper;

public interface BasePushTaskMapper<T> extends BaseMapper<T> {
    @Override
    default String getTable(){
        return "push_task_log";
    }
}