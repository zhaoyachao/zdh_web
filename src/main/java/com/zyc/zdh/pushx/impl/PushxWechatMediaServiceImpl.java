package com.zyc.zdh.pushx.impl;

import com.zyc.zdh.entity.WechatMediaInfo;
import com.zyc.zdh.entity.WechatQrcodeInfo;
import com.zyc.zdh.pushx.PushxWechatMediaService;
import com.zyc.zdh.pushx.PushxWechatQrcodeService;
import com.zyc.zdh.pushx.entity.WechatMediaRequest;
import com.zyc.zdh.pushx.entity.WechatMediaResponse;
import com.zyc.zdh.pushx.entity.WechatQrcodeRequest;
import com.zyc.zdh.pushx.entity.WechatQrcodeResponse;
import com.zyc.zdh.util.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Service
public class PushxWechatMediaServiceImpl implements PushxWechatMediaService {

    private static final String MEDIA_CREATE = "/api/v1/pushx/wechat/media/create";
    private static final String MEDIA_DELETE = "/api/v1/pushx/wechat/media/delete";



    private WechatMediaRequest buildMediaRequest(WechatMediaInfo wechatMediaInfo) {
        WechatMediaRequest mediaRequest = new WechatMediaRequest();
        mediaRequest.setWechat_channel(wechatMediaInfo.getWechat_channel());
        mediaRequest.setMedia_name(wechatMediaInfo.getMedia_name());
        mediaRequest.setMedia_desc(wechatMediaInfo.getMedia_desc());
        mediaRequest.setMedia_category(wechatMediaInfo.getMedia_category());
        mediaRequest.setMedia_type(wechatMediaInfo.getMedia_type());
        mediaRequest.setMedia_id(wechatMediaInfo.getMedia_id());
        mediaRequest.setTitle(wechatMediaInfo.getTitle());
        mediaRequest.setUrl(wechatMediaInfo.getUrl());
        mediaRequest.setIntroduction(wechatMediaInfo.getIntroduction());
        mediaRequest.setMedia_str(wechatMediaInfo.getMedia_str());

        return mediaRequest;
    }

    @Override
    public WechatMediaResponse addWechatMedia(WechatMediaInfo wechatMediaInfo) {
        try {
            // 构建请求体
            WechatMediaRequest wechatMediaRequest = buildMediaRequest(wechatMediaInfo);

            // 生成签名
            String sign = SignUtil.generatSign(JsonUtil.toJavaMap(JsonUtil.formatJsonString(wechatMediaRequest)), ConfigUtil.getValue(ConfigUtil.ZDH_PUSHX_SERVICE_KEY));
            wechatMediaRequest.setSign(sign);

            // 发送HTTP请求
            String json = JsonUtil.formatJsonString(wechatMediaRequest);
            String response = HttpUtil.builder().retryCount(0).postJSON(ConfigUtil.getValue(ConfigUtil.ZDH_PUSHX_BASE_URL) + MEDIA_CREATE, json);

            // 记录日志并返回结果
            LogUtil.info(this.getClass(), "pushx media create response: {}", response);
            WechatMediaResponse mediaResponse = JsonUtil.toJavaBean(response, WechatMediaResponse.class);
            return mediaResponse;
        } catch (Exception e) {
            LogUtil.error(this.getClass(), "创建素材异常", e);
        }
        return null;
    }

    @Override
    public WechatMediaResponse deleteWechatMedia(WechatMediaInfo wechatMediaInfo) {
        try {
            // 构建请求体
            WechatMediaRequest wechatMediaRequest = buildMediaRequest(wechatMediaInfo);

            // 生成签名
            String sign = SignUtil.generatSign(JsonUtil.toJavaMap(JsonUtil.formatJsonString(wechatMediaRequest)), ConfigUtil.getValue(ConfigUtil.ZDH_PUSHX_SERVICE_KEY));
            wechatMediaRequest.setSign(sign);

            // 发送HTTP请求
            String json = JsonUtil.formatJsonString(wechatMediaRequest);
            String response = HttpUtil.builder().retryCount(0).postJSON(ConfigUtil.getValue(ConfigUtil.ZDH_PUSHX_BASE_URL) + MEDIA_DELETE, json);

            // 记录日志并返回结果
            LogUtil.info(this.getClass(), "pushx media delete response: {}", response);
            WechatMediaResponse mediaResponse = JsonUtil.toJavaBean(response, WechatMediaResponse.class);
            return mediaResponse;
        } catch (Exception e) {
            LogUtil.error(this.getClass(), "删除素材异常", e);
        }
        return null;
    }
}