package com.zyc.zdh.pushx;

import com.zyc.zdh.pushx.entity.WechatCommentRequest;
import com.zyc.zdh.pushx.entity.WechatCommentResponse;

public interface PushxWechatCommentService {

    WechatCommentResponse listComment(WechatCommentRequest request);

    WechatCommentResponse replyAdd(WechatCommentRequest request);

    WechatCommentResponse commentDelete(WechatCommentRequest request);

    WechatCommentResponse replyDelete(WechatCommentRequest request);

    WechatCommentResponse commentSync(WechatCommentRequest request);
}
