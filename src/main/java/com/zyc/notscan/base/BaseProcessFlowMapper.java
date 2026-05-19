package com.zyc.notscan.base;

import com.zyc.notscan.BaseMapper;

/**
 * ClassName: BaseProcessFlowMapper
 * @author zyc-admin
 * @date 2021年10月19日
 * @Description: TODO  
 */
public interface BaseProcessFlowMapper<T> extends BaseMapper<T> {
    @Override
    default String getTable(){
        return "process_flow_info";
    }
}
