package com.zyc.zdh.pushx;

import com.zyc.zdh.pushx.entity.WechatAuthGetResponse;

public interface PushxWechatAuthService {
    public WechatAuthGetResponse genAuthUrl(String wechat_channel);
}