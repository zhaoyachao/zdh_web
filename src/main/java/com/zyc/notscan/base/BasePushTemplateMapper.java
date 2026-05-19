package com.zyc.notscan.base;

import com.zyc.notscan.BaseMapper;

public interface BasePushTemplateMapper<T> extends BaseMapper<T> {
    @Override
    default String getTable(){
        return "push_template_info";
    }
}