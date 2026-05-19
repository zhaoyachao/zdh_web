package com.zyc.zdh.pushx.entity;


public class WechatQrcodeResponse extends PushxBaseResponse{

    private QrcodeData data;

    @Override
    public QrcodeData getData() {
        return data;
    }

    public void setData(QrcodeData data) {
        this.data = data;
    }

    public static class QrcodeData {
        private String ticket;
        private Integer expire_seconds;
        private String url;

        public String getTicket() {
            return ticket;
        }
        public void setTicket(String ticket) {
            this.ticket = ticket;
        }

        public Integer getExpire_seconds() {
            return expire_seconds;
        }

        public void setExpire_seconds(Integer expire_seconds) {
            this.expire_seconds = expire_seconds;
        }

        public String getUrl() {
            return url;
        }
        public void setUrl(String url) {
            this.url = url;
        }
    }

}