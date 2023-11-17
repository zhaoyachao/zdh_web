package com.zyc.notscan.base;

import com.zyc.notscan.BaseMapper;

/**
 * ClassName: BaseJarTaskMapper
 * @author zyc-admin
 * @date 2017年12月26日  
 * @Description: TODO  
 */
public interface BaseJarTaskMapper<T> extends BaseMapper<T> {
    @Override
    default String getTable(){
        return "jar_task_info";
    }
}
