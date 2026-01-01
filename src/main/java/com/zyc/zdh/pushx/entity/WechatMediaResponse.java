package com.zyc.zdh.pushx.entity;

public class WechatMediaResponse extends PushxBaseResponse{

    private MediaData data;

    @Override
    public MediaData getData() {
        return data;
    }

    public void setData(MediaData data) {
        this.data = data;
    }

    public static class MediaData {
        private String media_id;

        private String url;

        // 临时素材特有
        private String type;
        // 临时素材特有
        private String created_at;

        public String getMedia_id() {
            return media_id;
        }

        public void setMedia_id(String media_id) {
            this.media_id = media_id;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }
    }
}
