package com.zyc.zdh.dao;

import com.zyc.notscan.base.BaseSurveyQuestionMapper;
import com.zyc.zdh.entity.SurveyQuestionInfo;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface SurveyQuestionMapper extends BaseSurveyQuestionMapper<SurveyQuestionInfo> {

    @Select("SELECT * FROM survey_question_info WHERE survey_id=#{surveyId} ORDER BY sort_order ASC")
    List<SurveyQuestionInfo> selectQuestionsBySurveyId(String surveyId);

    @Delete("DELETE FROM survey_question_info WHERE survey_id=#{surveyId}")
    int deleteBySurveyId(String surveyId);
}
