package com.zyc.zdh.pushx.entity;

public class WechatQrcodeRequest {
    private String channel;
    private String action_name;
    private Integer expire_seconds;
    private String scene_str;

    private Integer is_edit;

    private String url;

    private String appid;

    private String path;

    private Integer open_version;

    private String debug_url;

    private Integer permit_sub_rule;

    private String sign;

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getAction_name() {
        return action_name;
    }

    public void setAction_name(String action_name) {
        this.action_name = action_name;
    }

    public Integer getExpire_seconds() {
        return expire_seconds;
    }

    public void setExpire_seconds(Integer expire_seconds) {
        this.expire_seconds = expire_seconds;
    }

    public String getScene_str() {
        return scene_str;
    }

    public void setScene_str(String scene_str) {
        this.scene_str = scene_str;
    }

    public Integer getIs_edit() {
        return is_edit;
    }

    public void setIs_edit(Integer is_edit) {
        this.is_edit = is_edit;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Integer getOpen_version() {
        return open_version;
    }

    public void setOpen_version(Integer open_version) {
        this.open_version = open_version;
    }

    public String getDebug_url() {
        return debug_url;
    }

    public void setDebug_url(String debug_url) {
        this.debug_url = debug_url;
    }

    public Integer getPermit_sub_rule() {
        return permit_sub_rule;
    }

    public void setPermit_sub_rule(Integer permit_sub_rule) {
        this.permit_sub_rule = permit_sub_rule;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}