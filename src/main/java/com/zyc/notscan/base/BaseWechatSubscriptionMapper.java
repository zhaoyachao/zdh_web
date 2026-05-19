package com.zyc.notscan.base;

import com.zyc.notscan.BaseMapper;

public interface BaseWechatSubscriptionMapper<T> extends BaseMapper<T> {
    @Override
    default String getTable(){
        return "wechat_subscription_info";
    }
}