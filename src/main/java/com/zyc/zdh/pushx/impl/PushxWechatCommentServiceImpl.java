package com.zyc.zdh.pushx.impl;

import com.zyc.zdh.pushx.PushxWechatCommentService;
import com.zyc.zdh.pushx.entity.WechatCommentRequest;
import com.zyc.zdh.pushx.entity.WechatCommentResponse;
import com.zyc.zdh.util.ConfigUtil;
import com.zyc.zdh.util.HttpUtil;
import com.zyc.zdh.util.JsonUtil;
import com.zyc.zdh.util.LogUtil;
import com.zyc.zdh.util.SignUtil;
import org.springframework.stereotype.Service;

@Service
public class PushxWechatCommentServiceImpl implements PushxWechatCommentService {

    private static final String COMMENT_LIST = "/api/v1/pushx/wechat/comment/list";
    private static final String COMMENT_REPLY_ADD = "/api/v1/pushx/wechat/comment/replyadd";
    private static final String COMMENT_REPLY_DELETE = "/api/v1/pushx/wechat/comment/replydelete";
    private static final String COMMENT_DELETE = "/api/v1/pushx/wechat/comment/delete";
    private static final String COMMENT_SYNC = "/api/v1/pushx/wechat/comment/sync";

    @Override
    public WechatCommentResponse listComment(WechatCommentRequest request) {
        return doRequest(COMMENT_LIST, request, "list");
    }

    @Override
    public WechatCommentResponse replyAdd(WechatCommentRequest request) {
        return doRequest(COMMENT_REPLY_ADD, request, "replyadd");
    }

    @Override
    public WechatCommentResponse commentDelete(WechatCommentRequest request) {
        return doRequest(COMMENT_DELETE, request, "delete");
    }

    @Override
    public WechatCommentResponse replyDelete(WechatCommentRequest request) {
        return doRequest(COMMENT_REPLY_DELETE, request, "replydelete");
    }

    @Override
    public WechatCommentResponse commentSync(WechatCommentRequest request) {
        return doRequest(COMMENT_SYNC, request, "sync");
    }

    private WechatCommentResponse doRequest(String path, WechatCommentRequest request, String action) {
        try {
            String sign = SignUtil.generatSign(
                    JsonUtil.toJavaMap(JsonUtil.formatJsonString(request)),
                    ConfigUtil.getValue(ConfigUtil.ZDH_PUSHX_SERVICE_KEY)
            );
            request.setSign(sign);

            String json = JsonUtil.formatJsonString(request);
            String response = HttpUtil.builder().retryCount(0).postJSON(ConfigUtil.getValue(ConfigUtil.ZDH_PUSHX_BASE_URL) + path, json);
            LogUtil.info(this.getClass(), "pushx wechat comment {} response: {}", action, response);
            return JsonUtil.toJavaBean(response, WechatCommentResponse.class);
        } catch (Exception e) {
            LogUtil.error(this.getClass(), "微信评论接口调用异常", e);
        }
        return null;
    }
}
