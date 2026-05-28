package com.zyc.zdh.survey.impl;

import com.zyc.zdh.dao.SurveyAnswerMapper;
import com.zyc.zdh.dao.SurveyMapper;
import com.zyc.zdh.dao.SurveyQuestionMapper;
import com.zyc.zdh.entity.*;
import com.zyc.zdh.job.SnowflakeIdWorker;
import com.zyc.zdh.survey.SurveyService;
import com.zyc.zdh.util.JsonUtil;
import com.zyc.zdh.util.LogUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

@Service
public class SurveyServiceImpl implements SurveyService {

    @Autowired
    private SurveyMapper surveyMapper;
    @Autowired
    private SurveyQuestionMapper surveyQuestionMapper;
    @Autowired
    private SurveyAnswerMapper surveyAnswerMapper;
    @Override
    public ReturnInfo<Map<String, Object>> survey_detail(String id) {
        try {
            SurveyInfo survey = surveyMapper.selectByPrimaryKey(id);
            if (survey == null) {
                return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "问卷不存在", null);
            }

            List<SurveyQuestionInfo> questions = surveyQuestionMapper.selectQuestionsBySurveyId(id);

            Map<String, Object> result = JsonUtil.createEmptyMap();
            result.put("survey", survey);
            result.put("questions", questions);

            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "查询成功", result);
        } catch (Exception e) {
            LogUtil.error(this.getClass(), e);
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "查询失败", e);
        }
    }

    @Transactional
    @Override
    public ReturnInfo<String> survey_submit(String request) {
        try {
            Map<String, Object> requestData = JsonUtil.toJavaMap(request);

            String surveyId = (String) requestData.get("surveyId");
            String answerUser = (String) requestData.get("answerUser");
            String ipAddress = (String) requestData.get("ipAddress");
            Map<String, Object> answers = (Map<String, Object>) requestData.get("answers");

            if (surveyId == null || surveyId.isEmpty()) {
                return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "问卷ID不能为空", null);
            }

            SurveyInfo survey = surveyMapper.selectByPrimaryKey(surveyId);
            if (survey == null) {
                return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "问卷不存在", null);
            }

            List<SurveyQuestionInfo> questions = surveyQuestionMapper.selectQuestionsBySurveyId(surveyId);

            Timestamp now = new Timestamp(System.currentTimeMillis());
            int savedCount = 0;

            for (SurveyQuestionInfo question : questions) {
                String questionId = question.getId().toString();

                if (answers != null && answers.containsKey(questionId)) {
                    Map<String, Object> answerObj = (Map<String, Object>) answers.get(questionId);
                    String answerValue = null;
                    String answerText = null;

                    if (answerObj != null) {
                        answerValue = (String) answerObj.get("value");
                        answerText = (String) answerObj.get("text");
                    } else {
                        answerValue = (String) answers.get(questionId);
                    }

                    SurveyAnswerInfo answer = new SurveyAnswerInfo();
                    answer.setId(SnowflakeIdWorker.getInstance().nextId());
                    answer.setSurvey_id(surveyId);
                    answer.setQuestion_id(questionId);
                    answer.setQuestion_type(question.getQuestion_type());
                    answer.setAnswer_value(answerValue);
                    answer.setAnswer_text(answerText);
                    answer.setAnswer_user(answerUser != null ? answerUser : "匿名用户");
                    answer.setIp_address(ipAddress != null ? ipAddress : "未知");
                    answer.setCreate_time(now);

                    surveyAnswerMapper.insertSelective(answer);
                    savedCount++;
                } else {
                    LogUtil.info(this.getClass(), "问题 " + question.getQuestion_title() + " 未填写答案");
                }
            }

            LogUtil.info(this.getClass(), "问卷提交 - 问卷ID: " + surveyId + ", 答题人: " + (answerUser != null ? answerUser : "匿名用户") + ", 提交答案数: " + savedCount);

            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "提交成功，共保存" + savedCount + "条答案", null);
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            LogUtil.error(this.getClass(), e);
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "提交失败: " + e.getMessage(), e);
        }
    }

}
