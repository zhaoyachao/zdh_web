package com.zyc.notscan.base;

import com.zyc.notscan.BaseMapper;

public interface BaseStrategyGroupLogMapper<T> extends BaseMapper<T> {
    @Override
    default String getTable(){
        return "strategy_group_log";
    }
}