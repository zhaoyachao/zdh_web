package com.zyc.notscan.base;

import com.zyc.notscan.BaseMapper;

/**
 * ClassName: BaseIssueDataMapper
 * @author zyc-admin
 * @date 2017年12月26日  
 * @Description: TODO  
 */
public interface BaseIssueDataMapper<T> extends BaseMapper<T> {
    @Override
    default String getTable(){
        return "issue_data_info";
    }
}
