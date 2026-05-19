package com.zyc.zdh.pushx.entity;

public class WechatDraftResponse extends PushxBaseResponse{

    private DraftData data;

    @Override
    public DraftData getData() {
        return data;
    }

    public void setData(DraftData data) {
        this.data = data;
    }

    public static class DraftData {
        private String media_id;
        public String getMedia_id() {
            return media_id;
        }
        public void setMedia_id(String media_id) {
            this.media_id = media_id;
        }
    }
}
