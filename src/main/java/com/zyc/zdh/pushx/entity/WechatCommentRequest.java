package com.zyc.zdh.pushx.entity;

public class WechatCommentRequest {

    private String wechat_channel;
    private String article_id;
    private String msg_data_id;
    private String comment_id;
    private String content;
    private String begin_create_time;
    private String sign;

    public String getWechat_channel() {
        return wechat_channel;
    }

    public void setWechat_channel(String wechat_channel) {
        this.wechat_channel = wechat_channel;
    }

    public String getArticle_id() {
        return article_id;
    }

    public void setArticle_id(String article_id) {
        this.article_id = article_id;
    }

    public String getMsg_data_id() {
        return msg_data_id;
    }

    public void setMsg_data_id(String msg_data_id) {
        this.msg_data_id = msg_data_id;
    }

    public String getComment_id() {
        return comment_id;
    }

    public void setComment_id(String comment_id) {
        this.comment_id = comment_id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getBegin_create_time() {
        return begin_create_time;
    }

    public void setBegin_create_time(String begin_create_time) {
        this.begin_create_time = begin_create_time;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
