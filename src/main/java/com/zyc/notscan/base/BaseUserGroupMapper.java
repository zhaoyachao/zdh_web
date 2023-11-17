package com.zyc.notscan.base;

import com.zyc.notscan.BaseMapper;

/**
 * ClassName: BaseUserGroupMapper
 * @author zyc-admin
 * @date 2021年09月19日
 * @Description: TODO  
 */
public interface BaseUserGroupMapper<T> extends BaseMapper<T> {
    @Override
    default String getTable(){
        return "user_group_info";
    }
}
