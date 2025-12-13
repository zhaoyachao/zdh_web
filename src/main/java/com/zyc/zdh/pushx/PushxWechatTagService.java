package com.zyc.zdh.pushx;

import com.zyc.zdh.entity.WechatTagInfo;
import com.zyc.zdh.pushx.entity.WechatTagResponse;

public interface PushxWechatTagService {
    public WechatTagResponse createTag(WechatTagInfo wechatTagInfo);
    public WechatTagResponse updateTag(WechatTagInfo wechatTagInfo);
    public WechatTagResponse deleteTag(WechatTagInfo wechatTagInfo);
}