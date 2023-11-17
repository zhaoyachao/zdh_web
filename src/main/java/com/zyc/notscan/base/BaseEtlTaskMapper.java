package com.zyc.notscan.base;

import com.zyc.notscan.BaseMapper;

/**
 * ClassName: BaseEtlTaskJdbcMapper
 * @author zyc-admin
 * @date 2017年12月26日  
 * @Description: TODO  
 */
public interface BaseEtlTaskMapper<T> extends BaseMapper<T> {

    @Override
    default String getTable(){
        return "etl_task_info";
    }
}
