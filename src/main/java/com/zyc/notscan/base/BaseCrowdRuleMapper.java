package com.zyc.notscan.base;

import com.zyc.notscan.BaseMapper;

public interface BaseCrowdRuleMapper<T> extends BaseMapper<T> {
    @Override
    default String getTable(){
        return "crowd_rule_info";
    }
}