package com.zyc.zdh.pushx;

import com.zyc.zdh.pushx.entity.WechatTemplateResponse;


public interface PushxWechatTemplateService {
    public WechatTemplateResponse getWechatTemplate(String channel, String template_id, String template_type) throws Exception ;
}
