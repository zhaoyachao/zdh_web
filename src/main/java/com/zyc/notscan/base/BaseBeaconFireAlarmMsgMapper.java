package com.zyc.notscan.base;

import com.zyc.notscan.BaseMapper;

public interface BaseBeaconFireAlarmMsgMapper<T> extends BaseMapper<T> {
    @Override
    default String getTable(){
        return "beacon_fire_alarm_msg_info";
    }
}