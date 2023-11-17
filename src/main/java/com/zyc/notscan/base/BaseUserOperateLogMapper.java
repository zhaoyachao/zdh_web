package com.zyc.notscan.base;

import com.zyc.notscan.BaseMapper;

public interface BaseUserOperateLogMapper<T> extends BaseMapper<T> {
    @Override
    default String getTable(){
        return "user_operate_log";
    }
}