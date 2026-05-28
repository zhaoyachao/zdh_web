package com.zyc.zdh.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.zyc.zdh.dao.SurveyAnswerMapper;
import com.zyc.zdh.dao.SurveyMapper;
import com.zyc.zdh.dao.SurveyQuestionMapper;
import com.zyc.zdh.entity.*;
import com.zyc.zdh.job.SnowflakeIdWorker;
import com.zyc.zdh.survey.SurveyService;
import com.zyc.zdh.util.JsonUtil;
import com.zyc.zdh.util.LogUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import tk.mybatis.mapper.entity.Example;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Controller
public class SurveyController extends BaseController {

    @Autowired
    private SurveyMapper surveyMapper;

    @Autowired
    private SurveyQuestionMapper surveyQuestionMapper;

    @Autowired
    private SurveyAnswerMapper surveyAnswerMapper;

    @Autowired
    private SurveyService surveyService;

    @RequestMapping("survey_index")
    public String survey_index() {
        return "survey/survey_index";
    }

    @RequestMapping("survey_design")
    public String survey_design() {
        return "survey/survey_design";
    }

    @RequestMapping("survey_preview")
    public String survey_preview() {
        LogUtil.info(this.getClass(), "访问问卷预览页面");
        return "survey/survey_preview";
    }

    @RequestMapping("survey_statistics_index")
    public String survey_statistics_index() {
        return "survey/survey_statistics_index";
    }

    @SentinelResource(value = "survey_list", blockHandler = "handleReturn")
    @RequestMapping(value = "/survey_list", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<List<SurveyInfo>> survey_list() {
        try {
            List<SurveyInfo> surveys = surveyMapper.selectAllSurveys();
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "查询成功", surveys);
        } catch (Exception e) {
            LogUtil.error(this.getClass(), e);
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "查询失败", e);
        }
    }

    @SentinelResource(value = "survey_list_by_page", blockHandler = "handleReturn")
    @RequestMapping(value = "/survey_list_by_page", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<PageResult<List<SurveyInfo>>> survey_list_by_page(String context, String product_code, Integer limit, Integer offset) {
        try {
            if (limit == null || limit <= 0) {
                limit = 10;
            }
            if (offset == null || offset < 0) {
                offset = 0;
            }

            Example example = new Example(SurveyInfo.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("is_delete", "0");

            if (!StringUtils.isEmpty(product_code)) {
                criteria.andEqualTo("product_code", product_code);
            }

            if (!StringUtils.isEmpty(context)) {
                Example.Criteria criteria2 = example.createCriteria();
                criteria2.orLike("survey_title", "%" + context + "%");
                example.and(criteria2);
            }

            example.setOrderByClause("create_time DESC");

            RowBounds rowBounds = new RowBounds(offset, limit);
            int total = surveyMapper.selectCountByExample(example);

            List<SurveyInfo> surveys = surveyMapper.selectByExampleAndRowBounds(example, rowBounds);

            PageResult<List<SurveyInfo>> pageResult = new PageResult<>();
            pageResult.setTotal(total);
            pageResult.setRows(surveys);

            return ReturnInfo.buildSuccess(pageResult);
        } catch (Exception e) {
            LogUtil.error(this.getClass(), e);
            return ReturnInfo.buildError("问卷列表分页查询失败", e);
        }
    }

    @SentinelResource(value = "survey_detail", blockHandler = "handleReturn")
    @RequestMapping(value = "/survey_detail", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<Map<String, Object>> survey_detail(String id) {
        try {
            return surveyService.survey_detail(id);
        } catch (Exception e) {
            LogUtil.error(this.getClass(), e);
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "查询失败", e);
        }
    }

    @SentinelResource(value = "survey_add", blockHandler = "handleReturn")
    @Transactional
    @RequestMapping(value = "/survey_add", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<String> survey_add(String request) {
        try {
            SurveySaveRequest requestData = JsonUtil.toJavaBean(request, SurveySaveRequest.class);
            SurveyInfo surveyData = requestData.getSurvey();
            List<SurveyQuestionInfo> questionsData = requestData.getQuestions();

            String surveyId = surveyData.getId();
            Timestamp now = new Timestamp(System.currentTimeMillis());

            if (surveyId == null || surveyId.isEmpty() || "undefined".equals(surveyId)) {
                surveyId = String.valueOf(SnowflakeIdWorker.getInstance().nextId());
                surveyData.setId(surveyId);
                surveyData.setCreate_time(now);
            } else {
                surveyData.setCreate_time(now);
            }
            surveyData.setOwner(getOwner());
            surveyData.setUpdate_time(now);
            surveyData.setIs_delete("0");

            SurveyInfo existSurvey = surveyMapper.selectByPrimaryKey(surveyId);
            if (existSurvey != null) {
                surveyMapper.updateByPrimaryKeySelective(surveyData);
            } else {
                surveyMapper.insertSelective(surveyData);
            }

            surveyQuestionMapper.deleteBySurveyId(surveyId);

            for (int i = 0; i < questionsData.size(); i++) {
                SurveyQuestionInfo questionData = questionsData.get(i);
                String questionId = questionData.getId();
                if (questionId == null || questionId.isEmpty() || "undefined".equals(questionId)) {
                    questionId = String.valueOf(SnowflakeIdWorker.getInstance().nextId());
                    questionData.setId(questionId);
                }
                questionData.setCreate_time(now);
                questionData.setOwner(getOwner());
                questionData.setUpdate_time(now);
                questionData.setSurvey_id(surveyId);
                questionData.setSort_order(i);

                surveyQuestionMapper.insertSelective(questionData);
            }

            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "保存成功", surveyId);
        } catch (Exception e) {
            LogUtil.error(this.getClass(), e);
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "保存失败: " + e.getMessage(), e);
        }
    }

    @SentinelResource(value = "survey_delete", blockHandler = "handleReturn")
    @RequestMapping(value = "/survey_delete", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<String> survey_delete(String ids) {
        try {
            String[] idArray = ids.split(",");
            surveyMapper.deleteBatchByIds(idArray);
            for (String id : idArray) {
                surveyQuestionMapper.deleteBySurveyId(id.trim());
            }
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "删除成功", null);
        } catch (Exception e) {
            LogUtil.error(this.getClass(), e);
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "删除失败", e);
        }
    }

    @SentinelResource(value = "survey_get_page", blockHandler = "handleReturn")
    @RequestMapping(value = "/survey_get_page", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<Map<String, Object>> survey_get_page(String id) {
        try {
            Map<String, Object> result = JsonUtil.createEmptyMap();

            SurveyInfo survey = surveyMapper.selectByPrimaryKey(id);
            if (survey == null) {
                return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "问卷不存在", null);
            }

            List<SurveyQuestionInfo> questions = surveyQuestionMapper.selectQuestionsBySurveyId(id);

            result.put("surveyTitle", survey.getSurvey_title());
            result.put("surveyDesc", survey.getSurvey_desc());

            List<Map<String, Object>> questionsArray = new ArrayList<>();
            if (questions != null && !questions.isEmpty()) {
                for (SurveyQuestionInfo q : questions) {
                    Map<String, Object> questionObj = JsonUtil.createEmptyMap();
                    questionObj.put("id", q.getId());
                    questionObj.put("question_type", q.getQuestion_type());
                    questionObj.put("question_title", q.getQuestion_title());
                    questionObj.put("question_options", q.getQuestion_options());
                    questionObj.put("question_required", q.getQuestion_required());
                    questionsArray.add(questionObj);
                }
            }
            result.put("questions", questionsArray);

            LogUtil.info(this.getClass(), "问卷预览 - ID: " + id + ", 问题数量: " + questionsArray.size());

            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "获取成功", result);
        } catch (Exception e) {
            LogUtil.error(this.getClass(), e);
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "获取失败", e);
        }
    }

    @SentinelResource(value = "survey_submit", blockHandler = "handleReturn")
    @RequestMapping(value = "/survey_submit", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<String> survey_submit(String request) {
        return surveyService.survey_submit(request);
    }

    @SentinelResource(value = "survey_answer_list", blockHandler = "handleReturn")
    @RequestMapping(value = "/survey_answer_list", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<List<SurveyAnswerInfo>> survey_answer_list(String survey_id) {
        try {
            List<SurveyAnswerInfo> answers = null;
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "查询成功", answers);
        } catch (Exception e) {
            LogUtil.error(this.getClass(), e);
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "查询失败", e);
        }
    }

    @SentinelResource(value = "survey_statistics", blockHandler = "handleReturn")
    @RequestMapping(value = "/survey_statistics", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<Map<String, Object>> survey_statistics(String survey_id) {
        try {
            if (survey_id == null || survey_id.isEmpty()) {
                return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "问卷ID不能为空", null);
            }

            SurveyInfo survey = surveyMapper.selectByPrimaryKey(survey_id);
            if (survey == null) {
                return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "问卷不存在", null);
            }

            List<SurveyQuestionInfo> questions = surveyQuestionMapper.selectQuestionsBySurveyId(survey_id);

            SurveyAnswerInfo answerExample = new SurveyAnswerInfo();
            answerExample.setSurvey_id(survey_id);
            List<SurveyAnswerInfo> allAnswers = surveyAnswerMapper.select(answerExample);

            java.util.Set<String> uniqueUserSet = new java.util.HashSet<String>();
            for (SurveyAnswerInfo answer : allAnswers) {
                if (answer.getAnswer_user() != null) {
                    uniqueUserSet.add(answer.getAnswer_user());
                }
            }

            Map<String, Object> statistics = JsonUtil.createEmptyMap();
            statistics.put("surveyId", survey_id);
            statistics.put("surveyTitle", survey.getSurvey_title());

            int maxResponseCount = 0;
            List<Object> questionStats = new ArrayList<>();

            for (SurveyQuestionInfo question : questions) {
                Map<String, Object> qStat = JsonUtil.createEmptyMap();
                qStat.put("questionId", question.getId().toString());
                qStat.put("questionType", question.getQuestion_type());
                qStat.put("questionTitle", question.getQuestion_title());
                qStat.put("isRequired", question.getQuestion_required());

                List<SurveyAnswerInfo> questionAnswers = new java.util.ArrayList<SurveyAnswerInfo>();
                for (SurveyAnswerInfo answer : allAnswers) {
                    if (question.getId().toString().equals(answer.getQuestion_id())) {
                        questionAnswers.add(answer);
                    }
                }

                int currentResponseCount = questionAnswers.size();
                qStat.put("responseCount", currentResponseCount);

                if (currentResponseCount > maxResponseCount) {
                    maxResponseCount = currentResponseCount;
                }

                String type = question.getQuestion_type();

                if ("single".equals(type) || "multi".equals(type) || "select".equals(type)) {
                    java.util.Map<String, Long> optionCounts = new java.util.LinkedHashMap<>();

                    try {
                        List<Map<String, Object>> options = JsonUtil.toJavaListMap(question.getQuestion_options());
                        if (options != null) {
                            for (int i = 0; i < options.size(); i++) {
                                Map<String, Object> opt = options.get(i);
                                optionCounts.put(opt.get("label").toString(), 0L);
                            }
                        }
                    } catch (Exception e) {}

                    for (SurveyAnswerInfo answer : questionAnswers) {
                        if (answer.getAnswer_text() != null && !answer.getAnswer_text().isEmpty()) {
                            String[] texts = answer.getAnswer_text().split(",");
                            for (String text : texts) {
                                text = text.trim();
                                if (optionCounts.containsKey(text)) {
                                    optionCounts.put(text, optionCounts.get(text) + 1);
                                } else {
                                    optionCounts.put(text, optionCounts.getOrDefault(text, 0L) + 1);
                                }
                            }
                        }
                    }

                    List<Map<String, Object>> optionData = new ArrayList<>();
                    for (java.util.Map.Entry<String, Long> entry : optionCounts.entrySet()) {
                        Map<String, Object> item = JsonUtil.createEmptyMap();
                        item.put("label", entry.getKey());
                        item.put("value", entry.getValue());
                        optionData.add(item);
                    }
                    qStat.put("optionStats", optionData);

                } else if ("score".equals(type)) {
                    java.util.Map<Integer, Long> scoreCounts = new java.util.LinkedHashMap<>();
                    for (int i = 1; i <= 5; i++) {
                        scoreCounts.put(i, 0L);
                    }
                    
                    double totalScore = 0;
                    int scoreCount = 0;
                    
                    for (SurveyAnswerInfo answer : questionAnswers) {
                        if (answer.getAnswer_value() != null) {
                            try {
                                int score = Integer.parseInt(answer.getAnswer_value());
                                scoreCounts.put(score, scoreCounts.getOrDefault(score, 0L) + 1);
                                totalScore += score;
                                scoreCount++;
                            } catch (NumberFormatException e) {}
                        }
                    }
                    
                    qStat.put("scoreDistribution", scoreCounts);
                    qStat.put("averageScore", scoreCount > 0 ? Math.round(totalScore / scoreCount * 100.0) / 100.0 : 0);

                } else if ("number".equals(type)) {
                    java.util.List<Double> numbers = new java.util.ArrayList<>();
                    for (SurveyAnswerInfo answer : questionAnswers) {
                        if (answer.getAnswer_value() != null && !answer.getAnswer_value().isEmpty()) {
                            try {
                                numbers.add(Double.parseDouble(answer.getAnswer_value()));
                            } catch (NumberFormatException e) {}
                        }
                    }
                    
                    if (!numbers.isEmpty()) {
                        double sum = numbers.stream().mapToDouble(Double::doubleValue).sum();
                        double avg = sum / numbers.size();
                        double min = numbers.stream().mapToDouble(Double::doubleValue).min().getAsDouble();
                        double max = numbers.stream().mapToDouble(Double::doubleValue).max().getAsDouble();
                        
                        qStat.put("average", Math.round(avg * 100.0) / 100.0);
                        qStat.put("min", min);
                        qStat.put("max", max);
                        qStat.put("sum", Math.round(sum * 100.0) / 100.0);
                    }

                } else if ("matrix_single".equals(type)) {
                    try {
                        Map<String, Object> matrixOptions = JsonUtil.toJavaMap(question.getQuestion_options());
                        List<String> rows = (List<String>) matrixOptions.get("rows");
                        List<String> cols = (List<String>) matrixOptions.get("columns");

                        List<Map<String, Object>> matrixData = new ArrayList<>();

                        for (String row : rows) {
                            java.util.Map<String, Long> colCounts = new java.util.LinkedHashMap<>();
                            for (String col : cols) {
                                colCounts.put(col, 0L);
                            }

                            for (SurveyAnswerInfo answer : questionAnswers) {
                                if (answer.getAnswer_text() != null) {
                                    try {
                                        Map<String, Object> textJson = JsonUtil.toJavaMap(answer.getAnswer_text());
                                        for (java.util.Map.Entry<String, Object> entry : textJson.entrySet()) {
                                            String val = entry.getValue().toString();
                                            if (val.contains(row)) {
                                                for (String col : cols) {
                                                    if (val.contains(col)) {
                                                        colCounts.put(col, colCounts.get(col) + 1);
                                                    }
                                                }
                                            }
                                        }
                                    } catch (Exception e) {}
                                }
                            }

                            Map<String, Object> rowData = JsonUtil.createEmptyMap();
                            rowData.put("rowLabel", row);
                            rowData.put("colStats", colCounts);
                            matrixData.add(rowData);
                        }

                        qStat.put("matrixData", matrixData);
                        qStat.put("columns", cols);
                    } catch (Exception e) {
                        LogUtil.error(this.getClass(), "解析矩阵数据失败: " + e.getMessage());
                    }

                } else {
                    qStat.put("textType", true);
                }

                questionStats.add(qStat);
            }

            statistics.put("totalResponses", maxResponseCount);
            statistics.put("uniqueUsers", uniqueUserSet.size());
            statistics.put("questions", questionStats);

            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "统计成功", statistics);
        } catch (Exception e) {
            LogUtil.error(this.getClass(), e);
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "统计失败: " + e.getMessage(), e);
        }
    }
}
