package com.zyc.zdh.pushx.impl;

import com.zyc.zdh.pushx.PushxWechatTemplateService;
import com.zyc.zdh.pushx.entity.WechatTemplateRequest;
import com.zyc.zdh.pushx.entity.WechatTemplateResponse;
import com.zyc.zdh.util.*;
import org.springframework.stereotype.Service;

@Service
public class PushxWechatTemplateServiceImpl implements PushxWechatTemplateService {

    private static final String TEMPLATE_LIST = "/api/v1/pushx/wechat/template/list";

    @Override
    public WechatTemplateResponse getWechatTemplate(String channel, String template_id, String template_type) throws Exception {
        try{
            WechatTemplateRequest wechatTemplateRequest = new WechatTemplateRequest();
            wechatTemplateRequest.setChannel(channel);
            wechatTemplateRequest.setTemplate_id(template_id);
            wechatTemplateRequest.setTemplate_type(template_type);
            String sign = SignUtil.generatSign(JsonUtil.toJavaMap(JsonUtil.formatJsonString(wechatTemplateRequest)), ConfigUtil.getValue(ConfigUtil.ZDH_PUSHX_SERVICE_KEY));

            wechatTemplateRequest.setSign(sign);
            String json = JsonUtil.formatJsonString(wechatTemplateRequest);
            String response = HttpUtil.builder().retryCount(1).postJSON(ConfigUtil.getValue(ConfigUtil.ZDH_PUSHX_BASE_URL)+TEMPLATE_LIST, json);

            LogUtil.info(this.getClass(), "pushx template response: {}", response);
            WechatTemplateResponse wechatTemplateResponse = JsonUtil.toJavaBean(response, WechatTemplateResponse.class);
            return wechatTemplateResponse;
        }catch (Exception e){
            throw new Exception("调用pushx获取微信公众号模板信息失败");
        }
    }
}
