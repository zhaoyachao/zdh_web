package com.zyc.notscan.base;

import com.zyc.notscan.BaseMapper;

/**
 * ClassName: BaseApplyMapper
 * @author zyc-admin
 * @date 2017年12月26日  
 * @Description: TODO  
 */
public interface BaseApplyMapper<T> extends BaseMapper<T> {
    @Override
    default String getTable(){
        return "apply_info";
    }
}
