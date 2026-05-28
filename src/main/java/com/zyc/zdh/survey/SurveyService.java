package com.zyc.zdh.survey;

import com.zyc.zdh.entity.ReturnInfo;

import java.util.Map;

public interface SurveyService {
    public ReturnInfo<Map<String, Object>> survey_detail(String id);
    public ReturnInfo<String> survey_submit(String request);
}
