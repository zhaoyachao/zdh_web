package com.zyc.zdh.controller;


import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.zyc.zdh.entity.RagResponse;
import com.zyc.zdh.entity.ReturnInfo;
import com.zyc.zdh.util.ConfigUtil;
import com.zyc.zdh.util.HttpUtil;
import com.zyc.zdh.util.JsonUtil;
import com.zyc.zdh.util.LogUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

/**
 * 云控制台服务
 */
@Controller
public class CloudController extends BaseController {

    /**
     * 项目信息列表首页
     * @return
     */
    @RequestMapping(value = "/cloud_console", method = RequestMethod.GET)
    public String cloud_console() {

        return "cloud_console";
    }

    /**
     * 云问答
     * @param question 问题
     * @return
     */
    @SentinelResource(value = "cloud_question", blockHandler = "handleReturn")
    @RequestMapping(value = "/cloud_question", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<RagResponse> cloud_question(String question) {
        try{
            Map<String, Object> params = new HashMap<>();
            params.put("query", question);
            params.put("top_k", 5);


            String ask = HttpUtil.builder()
                    .retryCount(0)
                    .socketTimeout(1000*60)
                    .connectionTimeout(1000*60)
                    .connectionRequestTimeout(1000*60)
                    .postJSON(ConfigUtil.getValue(ConfigUtil.ZDH_RAG_URL), JsonUtil.formatJsonString(params));

            return ReturnInfo.buildSuccess(JsonUtil.toJavaBean(ask, RagResponse.class));
        }catch(Exception e){
            LogUtil.error(this.getClass(), e);
            return ReturnInfo.buildError("服务异常,重试或者联系管理员", e);
        }
    }
}
