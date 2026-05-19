package com.zyc.notscan.base;

import com.zyc.notscan.BaseMapper;

public interface BaseAlarmSmsMapper<T> extends BaseMapper<T> {
    @Override
    default String getTable(){
        return "alarm_sms_info";
    }
}