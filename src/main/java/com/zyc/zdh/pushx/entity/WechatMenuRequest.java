package com.zyc.zdh.pushx.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class WechatMenuRequest {
    private String channel;
    private String menu_type;
    private String menu_id;
    private WechatMenuMatchRule match_rule;
    @JsonProperty("button")
    private List<WechatMenuButton> button;
    private String sign;

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getMenu_type() {
        return menu_type;
    }

    public void setMenu_type(String menu_type) {
        this.menu_type = menu_type;
    }

    public String getMenu_id() {
        return menu_id;
    }

    public void setMenu_id(String menu_id) {
        this.menu_id = menu_id;
    }

    public WechatMenuMatchRule getMatch_rule() {
        return match_rule;
    }

    public void setMatch_rule(WechatMenuMatchRule match_rule) {
        this.match_rule = match_rule;
    }

    public List<WechatMenuButton> getButton() {
        return button;
    }

    public void setButton(List<WechatMenuButton> button) {
        this.button = button;
    }

    public String getSign() {
        return sign;
    }
    public void setSign(String sign) {
        this.sign = sign;
    }

    public static class WechatMenuButton {

        /**
         * 菜单标题，不超过16个字节（子菜单不超过60个字节）
         */
        private String name;

        /**
         * 菜单的响应动作类型
         * click/view/scancode_push/scancode_waitmsg/pic_sysphoto/
         * pic_photo_or_album/pic_weixin/location_select/media_id/
         * view_limited/miniprogram
         */
        private String type;

        /**
         * click等点击类型必须
         * 菜单KEY值，用于消息接口推送，不超过128字节
         */
        private String key;

        /**
         * view/miniprogram类型必须
         * 网页链接，用户点击菜单可打开链接，不超过1024字节
         */
        private String url;

        /**
         * media_id和view_limited类型必须
         * 调用新增永久素材接口返回的合法media_id
         */
        @JsonProperty("media_id")
        private String media_id;

        /**
         * private String article_id;
         */
        private String article_id;

        /**
         * miniprogram类型必须
         * 小程序的appid（仅认证公众号可配置）
         */
        @JsonProperty("appid")
        private String appid;

        /**
         * miniprogram类型必须
         * 小程序的页面路径
         */
        @JsonProperty("pagepath")
        private String pagepath;

        /**
         * 二级菜单数组，个数应为1~5个
         */
        @JsonProperty("sub_button")
        private List<WechatMenuButton> sub_button;

        // Getter和Setter方法
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getMedia_id() {
            return media_id;
        }

        public void setMedia_id(String media_id) {
            this.media_id = media_id;
        }

        public String getArticle_id() {
            return article_id;
        }

        public void setArticle_id(String article_id) {
            this.article_id = article_id;
        }

        public String getAppid() {
            return appid;
        }

        public void setAppid(String appid) {
            this.appid = appid;
        }

        public String getPagepath() {
            return pagepath;
        }

        public void setPagepath(String pagepath) {
            this.pagepath = pagepath;
        }

        public List<WechatMenuButton> getSub_button() {
            return sub_button;
        }

        public void setSub_button(List<WechatMenuButton> sub_button) {
            this.sub_button = sub_button;
        }
    }
}
