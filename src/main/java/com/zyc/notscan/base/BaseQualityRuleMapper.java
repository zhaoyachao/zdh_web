package com.zyc.notscan.base;

import com.zyc.notscan.BaseMapper;

public interface BaseQualityRuleMapper<T> extends BaseMapper<T> {
    @Override
    default String getTable(){
        return "quality_rule_info";
    }
}