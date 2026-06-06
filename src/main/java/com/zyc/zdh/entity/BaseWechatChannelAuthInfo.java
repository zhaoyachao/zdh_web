package com.zyc.zdh.entity;

import javax.persistence.Transient;
import java.util.Map;

/**
 * 统一数据权限信息
 * 需要做权限的列表都需要继承此实例
 */
public class BaseWechatChannelAuthInfo {

    private String wechat_channel;

    @Transient
    private String product_code;

    @Transient
    private Auth auth;

    public String getWechat_channel() {
        return wechat_channel;
    }

    public void setWechat_channel(String wechat_channel) {
        this.wechat_channel = wechat_channel;
    }
    public String getProduct_code() {
        return product_code;
    }

    public void setProduct_code(String product_code) {
        this.product_code = product_code;
    }

    public Auth getAuth() {
        return auth;
    }

    public void setAuth(Auth auth) {
        this.auth = auth;
    }

    public static class Auth{

        private String is_manager;

        private Map<String, String> actions;

        public String getIs_manager() {
            return is_manager;
        }

        public void setIs_manager(String is_manager) {
            this.is_manager = is_manager;
        }

        public Map<String, String> getActions() {
            return actions;
        }

        public void setActions(Map<String, String> actions) {
            this.actions = actions;
        }
    }
}
