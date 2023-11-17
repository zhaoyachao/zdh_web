package com.zyc.notscan.base;

import com.zyc.notscan.BaseMapper;

public interface BaseQrtzSchedulerStateMapper<T> extends BaseMapper<T> {
    @Override
    default String getTable(){
        return "qrtz_scheduler_state";
    }
}