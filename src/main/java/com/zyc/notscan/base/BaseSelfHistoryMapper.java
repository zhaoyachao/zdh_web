package com.zyc.notscan.base;

import com.zyc.notscan.BaseMapper;

public interface BaseSelfHistoryMapper<T> extends BaseMapper<T> {
    @Override
    default String getTable(){
        return "self_history";
    }
}