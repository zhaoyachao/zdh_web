package com.zyc.notscan.base;

import com.zyc.notscan.BaseMapper;

/**
 * ClassName: BaseZdhHaInfoMapper
 * @author zyc-admin
 * @date 2017年12月26日  
 * @Description: TODO  
 */
public interface BaseZdhHaInfoMapper<T> extends BaseMapper<T> {
    @Override
    default String getTable(){
        return "zdh_ha_info";
    }
}
