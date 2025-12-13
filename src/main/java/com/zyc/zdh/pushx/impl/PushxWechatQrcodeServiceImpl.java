package com.zyc.zdh.pushx.impl;

import com.zyc.zdh.entity.WechatQrcodeInfo;
import com.zyc.zdh.pushx.PushxWechatQrcodeService;
import com.zyc.zdh.pushx.entity.WechatQrcodeRequest;
import com.zyc.zdh.pushx.entity.WechatQrcodeResponse;
import com.zyc.zdh.util.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Service
public class PushxWechatQrcodeServiceImpl implements PushxWechatQrcodeService {

    private static final String QRCODE_CREATE = "/api/v1/pushx/wechat/qrcode/create";

    @Override
    public WechatQrcodeResponse createQrcode(WechatQrcodeInfo wechatQrcodeInfo) {
        try {
            // 构建请求体
            WechatQrcodeRequest qrcodeRequest = buildQrcodeRequest(wechatQrcodeInfo);

            // 生成签名
            String sign = SignUtil.generatSign(JsonUtil.toJavaMap(JsonUtil.formatJsonString(qrcodeRequest)), ConfigUtil.getValue(ConfigUtil.ZDH_PUSHX_SERVICE_KEY));
            qrcodeRequest.setSign(sign);

            // 发送HTTP请求
            String json = JsonUtil.formatJsonString(qrcodeRequest);
            String response = HttpUtil.postJSON(ConfigUtil.getValue(ConfigUtil.ZDH_PUSHX_BASE_URL) + QRCODE_CREATE, json);

            // 记录日志并返回结果
            LogUtil.info(this.getClass(), "pushx qrcode create response: {}", response);
            WechatQrcodeResponse qrcodeResponse = JsonUtil.toJavaBean(response, WechatQrcodeResponse.class);
            return qrcodeResponse;
        } catch (Exception e) {
            LogUtil.error(this.getClass(), "创建二维码异常", e);
        }
        return null;
    }

    private WechatQrcodeRequest buildQrcodeRequest(WechatQrcodeInfo wechatQrcodeInfo) {
        WechatQrcodeRequest qrcodeRequest = new WechatQrcodeRequest();
        qrcodeRequest.setChannel(wechatQrcodeInfo.getWechat_app());
        if(!StringUtils.isEmpty(wechatQrcodeInfo.getExpire_seconds())){
            qrcodeRequest.setExpire_seconds(Integer.valueOf(wechatQrcodeInfo.getExpire_seconds()));
        }
        qrcodeRequest.setScene_str(wechatQrcodeInfo.getAction_info());
        qrcodeRequest.setAction_name(wechatQrcodeInfo.getAction_name());
        return qrcodeRequest;
    }
}