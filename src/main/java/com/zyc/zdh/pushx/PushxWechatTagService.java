package com.zyc.zdh.pushx;

import com.zyc.zdh.entity.WechatTagInfo;
import com.zyc.zdh.entity.WechatUserTagInfo;
import com.zyc.zdh.pushx.entity.WechatTagResponse;

import java.util.List;

public interface PushxWechatTagService {
    public WechatTagResponse createTag(WechatTagInfo wechatTagInfo);
    public WechatTagResponse updateTag(WechatTagInfo wechatTagInfo);
    public WechatTagResponse deleteTag(WechatTagInfo wechatTagInfo);
    public WechatTagResponse batchtagging(WechatUserTagInfo wechatUserTagInfo, List<String> openids);
    public WechatTagResponse batchuntagging(WechatUserTagInfo wechatUserTagInfo, List<String> openids);

}