package com.zyc.zdh.pushx.impl;

import com.zyc.zdh.pushx.PushxWechatUserService;
import com.zyc.zdh.pushx.entity.WechatUserRemarkRequest;
import com.zyc.zdh.pushx.entity.WechatUserRemarkResponse;
import com.zyc.zdh.util.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Service
public class PushxWechatUserServiceImpl implements PushxWechatUserService {

    private static final String USER_REMARKUPDATE = "/api/v1/pushx/wechat/user/remarkupdate";
    @Override
    public WechatUserRemarkResponse updateRemark(String wechat_channel, String openid, String remark) {
        try {
            WechatUserRemarkRequest wechatUserRemarkRequest = new WechatUserRemarkRequest();
            wechatUserRemarkRequest.setChannel(wechat_channel);
            wechatUserRemarkRequest.setOpenid(openid);
            wechatUserRemarkRequest.setRemark(remark);

            if (StringUtils.isEmpty(wechatUserRemarkRequest.getRemark())) {
                throw new Exception("备注不能为空");
            }

            String sign = SignUtil.generatSign(JsonUtil.toJavaMap(JsonUtil.formatJsonString(wechatUserRemarkRequest)), ConfigUtil.getValue(ConfigUtil.ZDH_PUSHX_SERVICE_KEY));
            wechatUserRemarkRequest.setSign(sign);

            String json = JsonUtil.formatJsonString(wechatUserRemarkRequest);

            String response = HttpUtil.builder().retryCount(1).postJSON(ConfigUtil.getValue(ConfigUtil.ZDH_PUSHX_BASE_URL) + USER_REMARKUPDATE, json);
            LogUtil.info(this.getClass(), "pushx tag updateRemark response: {}", response);
            WechatUserRemarkResponse remarkResponse = JsonUtil.toJavaBean(response, WechatUserRemarkResponse.class);
            return remarkResponse;
        } catch (Exception e) {
            LogUtil.error(this.getClass(), "更新备注异常", e);
        }
        return null;
    }
}
