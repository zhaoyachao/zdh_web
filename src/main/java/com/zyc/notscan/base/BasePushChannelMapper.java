package com.zyc.notscan.base;

import com.zyc.notscan.BaseMapper;

public interface BasePushChannelMapper<T> extends BaseMapper<T> {
    @Override
    default String getTable(){
        return "push_channel_info";
    }
}