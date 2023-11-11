package com.zyc.zdh.push.impl;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.profile.DefaultProfile;
import com.zyc.zdh.push.MessageParam;
import com.zyc.zdh.push.MessagePush;

/**
 * 阿里云短信推送-基础实现
 * 当前推送性能较低
 */
public class AliMessagePush implements MessagePush {
    @Override
    public Object send(MessageParam messageParam) throws Exception {
        AliMessageParam aliMessageParam = (AliMessageParam)messageParam;
        DefaultProfile profile = DefaultProfile.getProfile(aliMessageParam.getReginId(), aliMessageParam.getAk(),aliMessageParam.getSk());
        IAcsClient client = new DefaultAcsClient(profile);

        // 创建API请求并设置参数
        SendSmsRequest request = new SendSmsRequest();
        request.setPhoneNumbers(aliMessageParam.getPhoneNumbers());
        request.setSignName(aliMessageParam.getSignName());
        request.setTemplateCode(aliMessageParam.getTemplateCode());
        request.setTemplateParam(aliMessageParam.getTemplateParam());
        request.setOutId(aliMessageParam.getOutId());
        try {
            SendSmsResponse response = client.getAcsResponse(request);

            return response;
        } catch (ServerException e) {
            e.printStackTrace();
            throw e;
        } catch (ClientException e) {
            // 打印错误码
            System.out.println("ErrCode:" + e.getErrCode());
            System.out.println("ErrMsg:" + e.getErrMsg());
            System.out.println("RequestId:" + e.getRequestId());
            throw e;
        }
    }
}
