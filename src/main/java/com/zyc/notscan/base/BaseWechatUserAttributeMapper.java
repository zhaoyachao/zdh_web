package com.zyc.notscan.base;

import com.zyc.notscan.BaseMapper;

public interface BaseWechatUserAttributeMapper<T> extends BaseMapper<T> {
    @Override
    default String getTable(){
        return "wechat_user_attribute";
    }
}