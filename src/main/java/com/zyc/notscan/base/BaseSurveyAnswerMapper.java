package com.zyc.notscan.base;

import com.zyc.notscan.BaseMapper;

/**
 * ClassName: BaseSurveyAnswerMapper
 * @author zyc-admin
 * @date 2017年12月26日  
 * @Description: TODO  
 */
public interface BaseSurveyAnswerMapper<T> extends BaseMapper<T> {

    @Override
    default String getTable(){
        return "survey_answer_info";
    }
}
