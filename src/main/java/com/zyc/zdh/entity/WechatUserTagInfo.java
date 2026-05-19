package com.zyc.zdh.entity;

import java.sql.Timestamp;
import javax.persistence.*;

@Table(name = "wechat_user_tag_info")
public class WechatUserTagInfo {
    @Id
    private String id;

    /**
     * 服务号
     */
    private String wechat_channel;

    /**
     * 服务号ID
     */
    private String wechat_id;

    /**
     * 用户OpenID
     */
    private String openid;

    /**
     * 用户unionid
     */
    private String unionid;

    /**
     * 微信标签id
     */
    private String tag_id;

    @Transient
    private String tag_name;

    /**
     * 创建时间
     */
    private Timestamp create_time;

    /**
     * 更新时间
     */
    private Timestamp update_time;

    /**
     * 是否删除,0:未删除,1:删除
     */
    private String is_delete;

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
     * 获取服务号
     *
     * @return wechat_channel - 服务号
     */
    public String getWechat_channel() {
        return wechat_channel;
    }

    /**
     * 设置服务号
     *
     * @param wechat_channel 服务号
     */
    public void setWechat_channel(String wechat_channel) {
        this.wechat_channel = wechat_channel;
    }

    /**
     * 获取服务号ID
     *
     * @return wechat_id - 服务号ID
     */
    public String getWechat_id() {
        return wechat_id;
    }

    /**
     * 设置服务号ID
     *
     * @param wechat_id 服务号ID
     */
    public void setWechat_id(String wechat_id) {
        this.wechat_id = wechat_id;
    }

    /**
     * 获取用户OpenID
     *
     * @return openid - 用户OpenID
     */
    public String getOpenid() {
        return openid;
    }

    /**
     * 设置用户OpenID
     *
     * @param openid 用户OpenID
     */
    public void setOpenid(String openid) {
        this.openid = openid;
    }

    /**
     * 获取用户unionid
     *
     * @return unionid - 用户unionid
     */
    public String getUnionid() {
        return unionid;
    }

    /**
     * 设置用户unionid
     *
     * @param unionid 用户unionid
     */
    public void setUnionid(String unionid) {
        this.unionid = unionid;
    }

    /**
     * 获取微信标签id
     *
     * @return tag_id - 微信标签id
     */
    public String getTag_id() {
        return tag_id;
    }

    /**
     * 设置微信标签id
     *
     * @param tag_id 微信标签id
     */
    public void setTag_id(String tag_id) {
        this.tag_id = tag_id;
    }


    public String getTag_name() {
		return tag_name;
	}

    public void setTag_name(String tag_name) {
		this.tag_name = tag_name;
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
     * 获取是否删除,0:未删除,1:删除
     *
     * @return is_delete - 是否删除,0:未删除,1:删除
     */
    public String getIs_delete() {
        return is_delete;
    }

    /**
     * 设置是否删除,0:未删除,1:删除
     *
     * @param is_delete 是否删除,0:未删除,1:删除
     */
    public void setIs_delete(String is_delete) {
        this.is_delete = is_delete;
    }
}