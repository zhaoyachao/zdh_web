package com.zyc.zdh.pushx.impl;

import com.zyc.zdh.entity.WechatDraftInfo;
import com.zyc.zdh.pushx.PushxWechatDraftService;
import com.zyc.zdh.pushx.entity.WechatDraftRequest;
import com.zyc.zdh.pushx.entity.WechatDraftResponse;
import com.zyc.zdh.util.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PushxWechatDraftServiceImpl implements PushxWechatDraftService {

    private static final String DRAFT_CREATE = "/api/v1/pushx/wechat/draft/create";
    private static final String DRAFT_DELETE = "/api/v1/pushx/wechat/draft/delete";

    private WechatDraftRequest buildMediaRequest(WechatDraftInfo wechatDraftInfo) {
        WechatDraftRequest request = new WechatDraftRequest();
        request.setWechat_channel(wechatDraftInfo.getWechat_channel());
        request.setMedia_id(wechatDraftInfo.getMedia_id());
        WechatDraftRequest.Article article = new WechatDraftRequest.Article();
        article.setArticle_type(wechatDraftInfo.getArticle_type());
        article.setTitle(wechatDraftInfo.getTitle());
        article.setAuthor(wechatDraftInfo.getAuthor());
        article.setDigest(wechatDraftInfo.getDigest());
        article.setContent(wechatDraftInfo.getContent());
        article.setContent_source_url(wechatDraftInfo.getContent_source_url());
        article.setThumb_media_id(wechatDraftInfo.getThumb_media_id());
        article.setNeed_open_comment(Integer.valueOf(wechatDraftInfo.getNeed_open_comment()));
        article.setOnly_fans_can_comment(Integer.valueOf(wechatDraftInfo.getOnly_fans_can_comment()));
        article.setPic_crop_235_1(wechatDraftInfo.getPic_crop_235_1());
        article.setPic_crop_1_1(wechatDraftInfo.getPic_crop_1_1());

        List<WechatDraftRequest.Article> articles = new ArrayList<WechatDraftRequest.Article>();
        articles.add(article);

        request.setArticles(articles);

        return request;
    }

    @Override
    public WechatDraftResponse addDraft(WechatDraftInfo wechatDraftInfo) {
        try {
            // 构建请求体
            WechatDraftRequest wechatDraftRequest = buildMediaRequest(wechatDraftInfo);

            // 生成签名
            String sign = SignUtil.generatSign(JsonUtil.toJavaMap(JsonUtil.formatJsonString(wechatDraftRequest)), ConfigUtil.getValue(ConfigUtil.ZDH_PUSHX_SERVICE_KEY));
            wechatDraftRequest.setSign(sign);

            // 发送HTTP请求
            String json = JsonUtil.formatJsonString(wechatDraftRequest);
            String response = HttpUtil.builder().retryCount(0).postJSON(ConfigUtil.getValue(ConfigUtil.ZDH_PUSHX_BASE_URL) + DRAFT_CREATE, json);

            // 记录日志并返回结果
            LogUtil.info(this.getClass(), "pushx draft create response: {}", response);
            WechatDraftResponse baseWechatResponse = JsonUtil.toJavaBean(response, WechatDraftResponse.class);
            return baseWechatResponse;
        } catch (Exception e) {
            LogUtil.error(this.getClass(), "创建草稿异常", e);
        }
        return null;
    }

    @Override
    public WechatDraftResponse deleteDraft(WechatDraftInfo wechatDraftInfo) {
        try {
            // 构建请求体
            WechatDraftRequest wechatDraftRequest = buildMediaRequest(wechatDraftInfo);

            // 生成签名
            String sign = SignUtil.generatSign(JsonUtil.toJavaMap(JsonUtil.formatJsonString(wechatDraftRequest)), ConfigUtil.getValue(ConfigUtil.ZDH_PUSHX_SERVICE_KEY));
            wechatDraftRequest.setSign(sign);

            // 发送HTTP请求
            String json = JsonUtil.formatJsonString(wechatDraftRequest);
            String response = HttpUtil.builder().retryCount(0).postJSON(ConfigUtil.getValue(ConfigUtil.ZDH_PUSHX_BASE_URL) + DRAFT_DELETE, json);

            // 记录日志并返回结果
            LogUtil.info(this.getClass(), "pushx draft delete response: {}", response);
            WechatDraftResponse baseWechatResponse = JsonUtil.toJavaBean(response, WechatDraftResponse.class);
            return baseWechatResponse;
        } catch (Exception e) {
            LogUtil.error(this.getClass(), "删除草稿异常", e);
        }
        return null;
    }
}