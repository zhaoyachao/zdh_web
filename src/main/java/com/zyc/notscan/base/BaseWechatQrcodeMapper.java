package com.zyc.notscan.base;

import com.zyc.notscan.BaseMapper;

public interface BaseWechatQrcodeMapper<T> extends BaseMapper<T> {
    @Override
    default String getTable(){
        return "wechat_qrcode_info";
    }
}