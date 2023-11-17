package com.zyc.notscan.base;

import com.zyc.notscan.BaseMapper;

/**
 * ClassName: BaseEtlDroolsTaskMapper
 * @author zyc-admin
 * @date 2017年12月26日  
 * @Description: TODO  
 */
public interface BaseEtlDroolsTaskMapper<T> extends BaseMapper<T> {
    @Override
    default String getTable(){
        return "etl_drools_task_info";
    }
}
