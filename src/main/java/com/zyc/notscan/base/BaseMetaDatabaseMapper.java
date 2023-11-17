package com.zyc.notscan.base;

import com.zyc.notscan.BaseMapper;

/**
 * ClassName: BaseMetaDatabaseMapper
 * @author zyc-admin
 * @date 2017年12月26日  
 * @Description: TODO  
 */
public interface BaseMetaDatabaseMapper<T> extends BaseMapper<T> {
    @Override
    default String getTable(){
        return "meta_database_info";
    }
}
