package com.zyc.zdh.pushx;

import com.zyc.zdh.entity.WechatMediaInfo;
import com.zyc.zdh.pushx.entity.WechatMediaResponse;

public interface PushxWechatMediaService {

    public WechatMediaResponse addWechatMedia(WechatMediaInfo wechatMediaInfo);

    public WechatMediaResponse deleteWechatMedia(WechatMediaInfo wechatMediaInfo);
}
