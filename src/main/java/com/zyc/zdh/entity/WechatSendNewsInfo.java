package com.zyc.zdh.entity;

import java.sql.Timestamp;
import javax.persistence.*;

@Table(name = "wechat_send_news_info")
public class WechatSendNewsInfo {
    @Id
    private String id;

    /**
     * 事件,PUBLISHJOBFINISH:发布草稿, MASSSENDJOBFINISH:群发消息
     */
    private String event;

    /**
     * 微信服务号
     */
    private String wechat_channel;

    /**
     * 发布任务id
     */
    private String publish_id;

    /**
     * 群发消息id
     */
    private String msg_id;

    /**
     * 内容id
     */
    private String msg_data_id;

    /**
     * 发布成功后的id
     */
    private String article_id;

    /**
     * 状态,0:成功, 1:失败
     */
    private String status;

    /**
     * 草稿/群发消息原始错误
     */
    private String wechat_status;

    /**
     * 评论同步时间
     */
    private String wechat_comment_sync_time;

    /**
     * 创建时间
     */
    private Timestamp create_time;

    /**
     * 更新时间
     */
    private Timestamp update_time;

    /**
     * 消息回调数据
     */
    private String ext;

    /**
     * @return id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * 获取事件,PUBLISHJOBFINISH:发布草稿, MASSSENDJOBFINISH:群发消息
     *
     * @return event - 事件,PUBLISHJOBFINISH:发布草稿, MASSSENDJOBFINISH:群发消息
     */
    public String getEvent() {
        return event;
    }

    /**
     * 设置事件,PUBLISHJOBFINISH:发布草稿, MASSSENDJOBFINISH:群发消息
     *
     * @param event 事件,PUBLISHJOBFINISH:发布草稿, MASSSENDJOBFINISH:群发消息
     */
    public void setEvent(String event) {
        this.event = event;
    }

    /**
     * 获取微信服务号
     *
     * @return wechat_channel - 微信服务号
     */
    public String getWechat_channel() {
        return wechat_channel;
    }

    /**
     * 设置微信服务号
     *
     * @param wechat_channel 微信服务号
     */
    public void setWechat_channel(String wechat_channel) {
        this.wechat_channel = wechat_channel;
    }

    /**
     * 获取发布任务id
     *
     * @return publish_id - 发布任务id
     */
    public String getPublish_id() {
        return publish_id;
    }

    /**
     * 设置发布任务id
     *
     * @param publish_id 发布任务id
     */
    public void setPublish_id(String publish_id) {
        this.publish_id = publish_id;
    }

    /**
     * 获取群发消息id
     *
     * @return msg_id - 群发消息id
     */
    public String getMsg_id() {
        return msg_id;
    }

    /**
     * 设置群发消息id
     *
     * @param msg_id 群发消息id
     */
    public void setMsg_id(String msg_id) {
        this.msg_id = msg_id;
    }

    /**
     * 获取内容id
     *
     * @return msg_data_id - 内容id
     */
    public String getMsg_data_id() {
        return msg_data_id;
    }

    /**
     * 设置内容id
     *
     * @param msg_data_id 内容id
     */
    public void setMsg_data_id(String msg_data_id) {
        this.msg_data_id = msg_data_id;
    }

    /**
     * 获取发布成功后的id
     *
     * @return article_id - 发布成功后的id
     */
    public String getArticle_id() {
        return article_id;
    }

    /**
     * 设置发布成功后的id
     *
     * @param article_id 发布成功后的id
     */
    public void setArticle_id(String article_id) {
        this.article_id = article_id;
    }

    /**
     * 获取状态,0:成功, 1:失败
     *
     * @return status - 状态,0:成功, 1:失败
     */
    public String getStatus() {
        return status;
    }

    /**
     * 设置状态,0:成功, 1:失败
     *
     * @param status 状态,0:成功, 1:失败
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * 获取草稿/群发消息原始错误
     *
     * @return wechat_status - 草稿/群发消息原始错误
     */
    public String getWechat_status() {
        return wechat_status;
    }

    /**
     * 设置草稿/群发消息原始错误
     *
     * @param wechat_status 草稿/群发消息原始错误
     */
    public void setWechat_status(String wechat_status) {
        this.wechat_status = wechat_status;
    }

    /**
     * 获取评论同步时间
     *
     * @return wechat_comment_sync_time - 评论同步时间
     */
    public String getWechat_comment_sync_time() {
        return wechat_comment_sync_time;
    }

    /**
     * 设置评论同步时间
     *
     * @param wechat_comment_sync_time 评论同步时间
     */
    public void setWechat_comment_sync_time(String wechat_comment_sync_time) {
        this.wechat_comment_sync_time = wechat_comment_sync_time;
    }

    /**
     * 获取创建时间
     *
     * @return create_time - 创建时间
     */
    public Timestamp getCreate_time() {
        return create_time;
    }

    /**
     * 设置创建时间
     *
     * @param create_time 创建时间
     */
    public void setCreate_time(Timestamp create_time) {
        this.create_time = create_time;
    }

    /**
     * 获取更新时间
     *
     * @return update_time - 更新时间
     */
    public Timestamp getUpdate_time() {
        return update_time;
    }

    /**
     * 设置更新时间
     *
     * @param update_time 更新时间
     */
    public void setUpdate_time(Timestamp update_time) {
        this.update_time = update_time;
    }

    /**
     * 获取消息回调数据
     *
     * @return ext - 消息回调数据
     */
    public String getExt() {
        return ext;
    }

    /**
     * 设置消息回调数据
     *
     * @param ext 消息回调数据
     */
    public void setExt(String ext) {
        this.ext = ext;
    }
}