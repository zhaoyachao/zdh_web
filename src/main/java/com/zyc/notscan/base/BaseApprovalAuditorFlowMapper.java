package com.zyc.notscan.base;

import com.zyc.notscan.BaseMapper;

public interface BaseApprovalAuditorFlowMapper<T> extends BaseMapper<T> {
    @Override
    default String getTable(){
        return "approval_auditor_Flow_info";
    }
}