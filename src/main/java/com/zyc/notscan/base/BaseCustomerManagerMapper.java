package com.zyc.notscan.base;

import com.zyc.notscan.BaseMapper;

public interface BaseCustomerManagerMapper<T> extends BaseMapper<T> {
    @Override
    default String getTable(){
        return "customer_manager_info";
    }
}