package com.zyc.zdh.pushx.entity;

public class WechatMediaRequest{
    /**
     * 服务号
     */
    private String wechat_channel;

    /**
     * 规则名称
     */
    private String media_name;

    /**
     * 素材说明
     */
    private String media_desc;

    /**
     * 素材分类,1:永久,2:临时
     */
    private String media_category;

    /**
     * 素材类型,image, voice, video, thumb
     */
    private String media_type;

    /**
     * 素材字符串,base64加密,素材不超过1M大小
     */
    private String media_str;

    /**
     * 素材id
     */
    private String media_id;

    /**
     * 视频标题
     */
    private String title;

    /**
     * 素材链接
     */
    private String url;

    /**
     * 视频标题简介
     */
    private String introduction;

    private String sign;

    public String getWechat_channel() {
        return wechat_channel;
    }

    public void setWechat_channel(String wechat_channel) {
        this.wechat_channel = wechat_channel;
    }

    public String getMedia_name() {
        return media_name;
    }

    public void setMedia_name(String media_name) {
        this.media_name = media_name;
    }

    public String getMedia_desc() {
        return media_desc;
    }

    public void setMedia_desc(String media_desc) {
        this.media_desc = media_desc;
    }

    public String getMedia_category() {
        return media_category;
    }

    public void setMedia_category(String media_category) {
        this.media_category = media_category;
    }

    public String getMedia_type() {
        return media_type;
    }

    public void setMedia_type(String media_type) {
        this.media_type = media_type;
    }

    public String getMedia_str() {
        return media_str;
    }

    public void setMedia_str(String media_str) {
        this.media_str = media_str;
    }

    public String getMedia_id() {
        return media_id;
    }

    public void setMedia_id(String media_id) {
        this.media_id = media_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
