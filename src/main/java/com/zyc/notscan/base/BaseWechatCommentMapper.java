package com.zyc.notscan.base;

import com.zyc.notscan.BaseMapper;

public interface BaseWechatCommentMapper<T> extends BaseMapper<T> {
    @Override
    default String getTable() {
        return "wechat_comment_info";
    }
}
