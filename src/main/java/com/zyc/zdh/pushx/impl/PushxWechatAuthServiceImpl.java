package com.zyc.zdh.pushx.impl;

import com.zyc.zdh.pushx.PushxWechatAuthService;
import com.zyc.zdh.pushx.entity.WechatAuthGetRequest;
import com.zyc.zdh.pushx.entity.WechatAuthGetResponse;
import com.zyc.zdh.util.*;
import org.springframework.stereotype.Service;

@Service
public class PushxWechatAuthServiceImpl implements PushxWechatAuthService {

    private static final String FOLLOW_ADD = "/api/v1/pushx/wechatthird/authurl/get";
    @Override
    public WechatAuthGetResponse genAuthUrl(String wechat_channel) {
        try {
            WechatAuthGetRequest wechatAuthGetRequest = new WechatAuthGetRequest();
            wechatAuthGetRequest.setChannel(wechat_channel);

            String sign = SignUtil.generatSign(JsonUtil.toJavaMap(JsonUtil.formatJsonString(wechatAuthGetRequest)), ConfigUtil.getValue(ConfigUtil.ZDH_PUSHX_SERVICE_KEY));
            wechatAuthGetRequest.setSign(sign);

            String json = JsonUtil.formatJsonString(wechatAuthGetRequest);

            String response = HttpUtil.builder().retryCount(1).postJSON(ConfigUtil.getValue(ConfigUtil.ZDH_PUSHX_BASE_URL) + FOLLOW_ADD, json);
            LogUtil.info(this.getClass(), "pushx wechatthird genAuthUrl response: {}", response);
            WechatAuthGetResponse remarkResponse = JsonUtil.toJavaBean(response, WechatAuthGetResponse.class);
            return remarkResponse;
        } catch (Exception e) {
            LogUtil.error(this.getClass(), "获取授权链接异常", e);
        }
        return null;
    }
}
