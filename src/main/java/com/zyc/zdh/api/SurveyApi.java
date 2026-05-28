package com.zyc.zdh.api;

import com.zyc.zdh.entity.ReturnInfo;
import com.zyc.zdh.survey.SurveyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

/**
 * api登录服务
 *
 * @author zyc-admin
 * @date 2018年2月5日
 * @Description: cloud.api包下的服务 不需要通过shiro验证拦截，需要自定义的token验证
 */
@Controller("surveyApi")
@RequestMapping("api")
public class SurveyApi {

    @Autowired
    private SurveyService surveyService;

    /**
     *
     * @return
     */
    @RequestMapping("survey_preview")
    public String survey_preview() {
        return "survey/survey_preview";
    }

    @RequestMapping("survey_detail")
    @ResponseBody
    public ReturnInfo<Map<String, Object>> survey_detail(String id) {
        return surveyService.survey_detail(id);
    }

    @RequestMapping(value = "survey_submit", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<String> survey_submit(String request) {
        return surveyService.survey_submit(request);
    }

}
