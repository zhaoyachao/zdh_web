package com.zyc.zdh.pushx.impl;

import com.zyc.zdh.entity.WechatTagInfo;
import com.zyc.zdh.pushx.PushxWechatTagService;
import com.zyc.zdh.pushx.entity.WechatTagRequest;
import com.zyc.zdh.pushx.entity.WechatTagResponse;
import com.zyc.zdh.util.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Service
public class PushxWechatTagServiceImpl implements PushxWechatTagService {

    private static final String TAG_CREATE = "/api/v1/pushx/wechat/tag/create";
    private static final String TAG_UPDATE = "/api/v1/pushx/wechat/tag/update";
    private static final String TAG_DELETE = "/api/v1/pushx/wechat/tag/delete";

    @Override
    public WechatTagResponse createTag(WechatTagInfo wechatTagInfo) {
        try {
            WechatTagRequest wechatTagRequest = buildTagRequest(wechatTagInfo);

            String sign = SignUtil.generatSign(JsonUtil.toJavaMap(JsonUtil.formatJsonString(wechatTagRequest)), ConfigUtil.getValue(ConfigUtil.ZDH_PUSHX_SERVICE_KEY));
            wechatTagRequest.setSign(sign);

            String json = JsonUtil.formatJsonString(wechatTagRequest);

            String response = HttpUtil.builder().retryCount(1).postJSON(ConfigUtil.getValue(ConfigUtil.ZDH_PUSHX_BASE_URL) + TAG_CREATE, json);

            LogUtil.info(this.getClass(), "pushx tag create response: {}", response);
            WechatTagResponse wechatTagResponse = JsonUtil.toJavaBean(response, WechatTagResponse.class);
            return wechatTagResponse;
        } catch (Exception e) {
            LogUtil.error(this.getClass(), "创建标签异常", e);
        }
        return null;
    }

    @Override
    public WechatTagResponse updateTag(WechatTagInfo wechatTagInfo) {
        try {
            WechatTagRequest wechatTagRequest = buildTagRequest(wechatTagInfo);
            if (StringUtils.isEmpty(wechatTagInfo.getTid())) {
                throw new Exception("标签ID不能为空");
            }

            String sign = SignUtil.generatSign(JsonUtil.toJavaMap(JsonUtil.formatJsonString(wechatTagRequest)), ConfigUtil.getValue(ConfigUtil.ZDH_PUSHX_SERVICE_KEY));
            wechatTagRequest.setSign(sign);

            String json = JsonUtil.formatJsonString(wechatTagRequest);

            String response = HttpUtil.builder().retryCount(1).postJSON(ConfigUtil.getValue(ConfigUtil.ZDH_PUSHX_BASE_URL) + TAG_UPDATE, json);
            LogUtil.info(this.getClass(), "pushx tag update response: {}", response);
            WechatTagResponse wechatTagResponse = JsonUtil.toJavaBean(response, WechatTagResponse.class);
            return wechatTagResponse;
        } catch (Exception e) {
            LogUtil.error(this.getClass(), "更新标签异常", e);
        }
        return null;
    }

    @Override
    public WechatTagResponse deleteTag(WechatTagInfo wechatTagInfo) {
        try {
            WechatTagRequest wechatTagRequest = new WechatTagRequest();
            wechatTagRequest.setChannel(wechatTagInfo.getWechat_app());

            WechatTagRequest.WechatTag tag = new WechatTagRequest.WechatTag();
            if (StringUtils.isNotEmpty(wechatTagInfo.getTid())) {
                tag.setId(Integer.parseInt(wechatTagInfo.getTid()));
            }
            tag.setName(wechatTagInfo.getTname());
            wechatTagRequest.setTag(tag);

            String sign = SignUtil.generatSign(JsonUtil.toJavaMap(JsonUtil.formatJsonString(wechatTagRequest)), ConfigUtil.getValue(ConfigUtil.ZDH_PUSHX_SERVICE_KEY));
            wechatTagRequest.setSign(sign);

            String json = JsonUtil.formatJsonString(wechatTagRequest);

            String response = HttpUtil.builder().retryCount(1).postJSON(ConfigUtil.getValue(ConfigUtil.ZDH_PUSHX_BASE_URL) + TAG_DELETE, json);
            LogUtil.info(this.getClass(), "pushx tag delete response: {}", response);
            WechatTagResponse wechatTagResponse = JsonUtil.toJavaBean(response, WechatTagResponse.class);
            return wechatTagResponse;

        } catch (Exception e) {
            LogUtil.error(this.getClass(), "删除标签异常", e);
        }
        return null;
    }

    private WechatTagRequest buildTagRequest(WechatTagInfo wechatTagInfo) {
        WechatTagRequest wechatTagRequest = new WechatTagRequest();
        wechatTagRequest.setChannel(wechatTagInfo.getWechat_app());

        WechatTagRequest.WechatTag tag = new WechatTagRequest.WechatTag();
        if (StringUtils.isNotEmpty(wechatTagInfo.getTid())) {
            tag.setId(Integer.parseInt(wechatTagInfo.getTid()));
        }
        tag.setName(wechatTagInfo.getTname());
        wechatTagRequest.setTag(tag);

        return wechatTagRequest;
    }
}