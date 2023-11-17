package com.zyc.notscan.base;

import com.zyc.notscan.BaseMapper;

public interface BaseRiskEventMapper<T> extends BaseMapper<T> {
    @Override
    default String getTable(){
        return "risk_event_info";
    }
}