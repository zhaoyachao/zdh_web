package com.zyc.zdh.push;

/**
 * 消息统一推送接口
 */
public interface MessagePush {

    public Object send(MessageParam messageParam) throws Exception;
}
