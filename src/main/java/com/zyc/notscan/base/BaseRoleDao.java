package com.zyc.notscan.base;

import com.zyc.notscan.BaseMapper;

public interface BaseRoleDao<T> extends BaseMapper<T> {
    @Override
    default String getTable(){
        return "role_info";
    }
}
