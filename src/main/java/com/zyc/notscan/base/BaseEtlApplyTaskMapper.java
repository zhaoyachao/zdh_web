package com.zyc.notscan.base;

import com.zyc.notscan.BaseMapper;

/**
 * ClassName: BaseEtlApplyTaskMapper
 * @author zyc-admin
 * @date 2017年12月26日  
 * @Description: TODO  
 */
public interface BaseEtlApplyTaskMapper<T> extends BaseMapper<T> {
    @Override
    default String getTable(){
        return "etl_apply_task_info";
    }
}
