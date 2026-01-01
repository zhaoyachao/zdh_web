package com.zyc.notscan.base;

import com.zyc.notscan.BaseMapper;

public interface BaseWechatRuleMapper<T> extends BaseMapper<T> {
    @Override
    default String getTable(){
        return "wechat_rule_info";
    }
}