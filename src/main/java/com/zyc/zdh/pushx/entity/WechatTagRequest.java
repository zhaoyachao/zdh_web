package com.zyc.zdh.pushx.entity;

public class WechatTagRequest {
    private String channel;
    private WechatTag tag;
    private String sign;

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public WechatTag getTag() {
        return tag;
    }

    public void setTag(WechatTag tag) {
        this.tag = tag;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public static class WechatTag {
        /**
         * 标签id，创建时不需要，删除时需要
         */
        private Integer id;

        /**
         * 标签名（30个字符以内）
         */
        private String name;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}