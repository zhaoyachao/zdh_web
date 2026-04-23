package com.zyc.zdh.pushx;

import com.zyc.zdh.pushx.entity.WechatFollowAddResponse;

public interface PushxWechatFollowService {
    public WechatFollowAddResponse add(String wechat_channel, String openid);
}