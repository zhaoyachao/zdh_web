package com.zyc.zdh.entity;

/**
 * zdh 参数基础配置
 */
public class ZdhBaseInfo {

    private String push_type;

    private String queue;

    private String url;

    private String excutor_id;

    public String getPush_type() {
        return push_type;
    }

    public void setPush_type(String push_type) {
        this.push_type = push_type;
    }

    public String getQueue() {
        return queue;
    }

    public void setQueue(String queue) {
        this.queue = queue;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getExcutor_id() {
        return excutor_id;
    }

    public void setExcutor_id(String excutor_id) {
        this.excutor_id = excutor_id;
    }
}
