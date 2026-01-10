package com.zyc.zdh.pushx;

import com.zyc.zdh.entity.WechatDraftInfo;
import com.zyc.zdh.pushx.entity.WechatDraftResponse;

public interface PushxWechatDraftService {

    public WechatDraftResponse addDraft(WechatDraftInfo wechatDraftInfo);

    public WechatDraftResponse deleteDraft(WechatDraftInfo wechatDraftInfo);
}
