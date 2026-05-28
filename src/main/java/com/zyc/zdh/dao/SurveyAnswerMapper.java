package com.zyc.zdh.dao;

import com.zyc.notscan.base.BaseSurveyAnswerMapper;
import com.zyc.zdh.entity.SurveyAnswerInfo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

public interface SurveyAnswerMapper extends BaseSurveyAnswerMapper<SurveyAnswerInfo> {

    @Insert("INSERT INTO survey_answer_info (survey_id, question_id, question_type, answer_value, answer_user, ip_address, create_time) " +
            "VALUES (#{survey_id}, #{question_id}, #{question_type}, #{answer_value}, #{answer_user}, #{ip_address}, #{create_time})")
    int insert(SurveyAnswerInfo answer);

    @Select("SELECT COUNT(*) FROM survey_answer_info WHERE survey_id=#{surveyId}")
    int countBySurveyId(String surveyId);
}
