package com.zyc.notscan.base;

import com.zyc.notscan.BaseMapper;


public interface BaseQuartzJobMapper<T> extends BaseMapper<T> {
    @Override
    default String getTable(){
        return "quartz_job_info";
    }
}