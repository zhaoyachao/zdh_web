package com.zyc.zdh.entity;

import java.util.List;

public class SurveySaveRequest {
    private SurveyInfo survey;
    private List<SurveyQuestionInfo> questions;

    public SurveyInfo getSurvey() {
        return survey;
    }

    public void setSurvey(SurveyInfo survey) {
        this.survey = survey;
    }

    public List<SurveyQuestionInfo> getQuestions() {
        return questions;
    }

    public void setQuestions(List<SurveyQuestionInfo> questions) {
        this.questions = questions;
    }
}
