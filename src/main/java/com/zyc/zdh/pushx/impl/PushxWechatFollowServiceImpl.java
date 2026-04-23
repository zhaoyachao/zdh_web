package com.zyc.zdh.pushx.impl;

import com.zyc.zdh.pushx.PushxWechatFollowService;
import com.zyc.zdh.pushx.entity.WechatFollowAddRequest;
import com.zyc.zdh.pushx.entity.WechatFollowAddResponse;
import com.zyc.zdh.util.*;
import org.springframework.stereotype.Service;

@Service
public class PushxWechatFollowServiceImpl implements PushxWechatFollowService {

    private static final String FOLLOW_ADD = "/api/v1/pushx/wechat/follow/add";
    @Override
    public WechatFollowAddResponse add(String wechat_channel, String openid) {
        try {
            WechatFollowAddRequest wechatFollowAddRequest = new WechatFollowAddRequest();
            wechatFollowAddRequest.setChannel(wechat_channel);
            wechatFollowAddRequest.setOpenid(openid);

            String sign = SignUtil.generatSign(JsonUtil.toJavaMap(JsonUtil.formatJsonString(wechatFollowAddRequest)), ConfigUtil.getValue(ConfigUtil.ZDH_PUSHX_SERVICE_KEY));
            wechatFollowAddRequest.setSign(sign);

            String json = JsonUtil.formatJsonString(wechatFollowAddRequest);

            String response = HttpUtil.builder().retryCount(1).postJSON(ConfigUtil.getValue(ConfigUtil.ZDH_PUSHX_BASE_URL) + FOLLOW_ADD, json);
            LogUtil.info(this.getClass(), "pushx tag followadd response: {}", response);
            WechatFollowAddResponse remarkResponse = JsonUtil.toJavaBean(response, WechatFollowAddResponse.class);
            return remarkResponse;
        } catch (Exception e) {
            LogUtil.error(this.getClass(), "更新备注异常", e);
        }
        return null;
    }
}
