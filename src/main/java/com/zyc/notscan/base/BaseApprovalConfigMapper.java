package com.zyc.notscan.base;

import com.zyc.notscan.BaseMapper;

/**
 * ClassName: BaseApprovalConfigMapper
 * @author zyc-admin
 * @date 2017年12月26日  
 * @Description: TODO  
 */
public interface BaseApprovalConfigMapper<T> extends BaseMapper<T> {
    @Override
    default String getTable(){
        return "approval_config_info";
    }
}
