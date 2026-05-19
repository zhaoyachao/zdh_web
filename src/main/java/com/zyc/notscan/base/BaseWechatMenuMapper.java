package com.zyc.notscan.base;

import com.zyc.notscan.BaseMapper;

public interface BaseWechatMenuMapper<T> extends BaseMapper<T> {
    @Override
    default String getTable(){
        return "wechat_menu_info";
    }
}