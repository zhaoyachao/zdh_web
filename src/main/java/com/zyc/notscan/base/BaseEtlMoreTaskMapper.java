package com.zyc.notscan.base;

import com.zyc.notscan.BaseMapper;

/**
 * ClassName: BaseEtlMoreTaskMapper
 * @author zyc-admin
 * @date 2017年12月26日  
 * @Description: TODO  
 */
public interface BaseEtlMoreTaskMapper<T> extends BaseMapper<T> {
    @Override
    default String getTable(){
        return "etl_more_task_info";
    }
}
