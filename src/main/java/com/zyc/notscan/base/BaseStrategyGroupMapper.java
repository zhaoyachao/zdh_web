package com.zyc.notscan.base;

import com.zyc.notscan.BaseMapper;

public interface BaseStrategyGroupMapper<T> extends BaseMapper<T> {
    @Override
    default String getTable(){
        return "strategy_group_info";
    }
}