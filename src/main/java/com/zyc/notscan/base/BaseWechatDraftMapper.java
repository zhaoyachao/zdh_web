package com.zyc.notscan.base;

import com.zyc.notscan.BaseMapper;

public interface BaseWechatDraftMapper<T> extends BaseMapper<T> {
    @Override
    default String getTable(){
        return "wechat_draft_info";
    }
}