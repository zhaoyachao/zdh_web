package com.zyc.notscan.base;

import com.zyc.notscan.BaseMapper;

/**
 * ClassName: BaseSurveyMapper
 * @author zyc-admin
 * @date 2017年12月26日  
 * @Description: TODO  
 */
public interface BaseSurveyMapper<T> extends BaseMapper<T> {

    @Override
    default String getTable(){
        return "survey_info";
    }
}
