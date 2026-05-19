package com.zyc.zdh.pushx.entity;

public class WechatTagResponse extends PushxBaseResponse {
    private TagData data;

    public TagData getData() {
        return data;
    }

    public void setData(TagData data) {
        this.data = data;
    }

    public static class TagData {
        private Tag tag;
        public Tag getTag() {
            return tag;
        }
        public void setTag(Tag tag) {
            this.tag = tag;
        }
    }

    public static class Tag {
        private Integer id;
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