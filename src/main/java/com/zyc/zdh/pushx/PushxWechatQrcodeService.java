package com.zyc.zdh.pushx;

import com.zyc.zdh.entity.WechatQrcodeInfo;
import com.zyc.zdh.pushx.entity.WechatQrcodeResponse;

public interface PushxWechatQrcodeService {
    WechatQrcodeResponse createQrcode(WechatQrcodeInfo wechatQrcodeInfo);
    WechatQrcodeResponse qrcodeJumpAdd(WechatQrcodeInfo wechatQrcodeInfo);
    WechatQrcodeResponse qrcodeJumpPublish(WechatQrcodeInfo wechatQrcodeInfo);
}
