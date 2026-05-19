package com.zyc.zdh.pushx;

import com.zyc.zdh.pushx.entity.WechatUserRemarkResponse;

public interface PushxWechatUserService {
    public WechatUserRemarkResponse updateRemark(String wechat_channel, String openid, String remark);
}