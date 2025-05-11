package com.zyc.notscan.base;

import com.zyc.notscan.BaseMapper;

public interface BasePushChannelPoolMapper<T> extends BaseMapper<T> {
    @Override
    default String getTable(){
        return "push_channel_pool_info";
    }
}