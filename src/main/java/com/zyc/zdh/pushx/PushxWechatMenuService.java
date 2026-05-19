package com.zyc.zdh.pushx;

import com.zyc.zdh.entity.WechatMenuInfo;
import com.zyc.zdh.pushx.entity.WechatMenuResponse;

public interface PushxWechatMenuService {
    public WechatMenuResponse createMenu(WechatMenuInfo wechatMenuInfo);
    public WechatMenuResponse deleteMenu(WechatMenuInfo wechatMenuInfo);
}
