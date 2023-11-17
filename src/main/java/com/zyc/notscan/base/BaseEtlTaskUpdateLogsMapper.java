package com.zyc.notscan.base;

import com.zyc.notscan.BaseMapper;

/**
 * ClassName: BaseEtlTaskUpdateLogsMapper
 * @author zyc-admin
 * @date 2017年12月26日  
 * @Description: TODO  
 */
public interface BaseEtlTaskUpdateLogsMapper<T> extends BaseMapper<T> {
    @Override
    default String getTable(){
        return "etl_task_update_logs";
    }
}
