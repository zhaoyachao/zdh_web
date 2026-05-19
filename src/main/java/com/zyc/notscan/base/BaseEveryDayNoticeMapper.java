package com.zyc.notscan.base;

import com.zyc.notscan.BaseMapper;

/**
 * ClassName: BaseEveryDayNoticeMapper
 * @author zyc-admin
 * @date 2017年12月26日  
 * @Description: TODO  
 */
public interface BaseEveryDayNoticeMapper<T> extends BaseMapper<T>{
    @Override
    default String getTable(){
        return "every_day_notice";
    }
}
