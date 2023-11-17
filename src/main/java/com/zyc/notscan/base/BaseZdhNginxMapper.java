package com.zyc.notscan.base;

import com.zyc.notscan.BaseMapper;

/**
 * ClassName: BaseZdhNginxMapper
 * @author zyc-admin
 * @date 2017年12月26日  
 * @Description: TODO  
 */
public interface BaseZdhNginxMapper<T> extends BaseMapper<T> {
    @Override
    default String getTable(){
        return "zdh_nginx";
    }
}
