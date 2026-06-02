package com.zyc.zdh.pushx.entity;

public class WechatAuthGetRequest {
    private String channel;
    private String sign;

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}