package com.zyc.notscan.base;

import com.zyc.notscan.BaseMapper;

public interface BaseWechatUserTagMapper<T> extends BaseMapper<T> {
    @Override
    default String getTable(){
        return "wechat_user_tag_info";
    }
}