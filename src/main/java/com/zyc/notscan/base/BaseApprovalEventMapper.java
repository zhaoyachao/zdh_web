package com.zyc.notscan.base;

import com.zyc.notscan.BaseMapper;

/**
 * ClassName: BaseApprovalEventMapper
 * @author zyc-admin
 * @date 2021年10月12日
 * @Description: TODO  
 */
public interface BaseApprovalEventMapper<T> extends BaseMapper<T> {
    @Override
    default String getTable(){
        return "approval_event_info";
    }
}
