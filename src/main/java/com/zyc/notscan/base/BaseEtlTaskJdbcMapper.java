package com.zyc.notscan.base;

import com.zyc.notscan.BaseMapper;

/**
 * ClassName: BaseEtlTaskJdbcMapper
 * @author zyc-admin
 * @date 2021年11月27日
 * @Description: TODO  
 */
public interface BaseEtlTaskJdbcMapper<T> extends BaseMapper<T> {
    @Override
    default String getTable(){
        return "etl_task_jdbc_info";
    }
}
