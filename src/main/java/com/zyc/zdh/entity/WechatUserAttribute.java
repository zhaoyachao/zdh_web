package com.zyc.zdh.entity;

import java.sql.Timestamp;
import javax.persistence.*;

@Table(name = "wechat_user_attribute")
public class WechatUserAttribute extends BaseWechatChannelAuthInfo{
    /**
     * 主键ID
     */
    @Id
    private String id;

    /**
     * 服务号
     */
    private String wechat_channel;

    /**
     * 用户OpenID
     */
    private String openid;

    /**
     * 属性类型:phone/email/name/id_card/company/position
     */
    private String attribute_type;

    /**
     * 属性值
     */
    private String attribute_value;

    /**
     * 状态:1-有效,0-已解绑
     */
    private String status;

    /**
     * 删除标志:0-未删除,1-已删除
     */
    private String is_delete;

    /**
     * 创建时间
     */
    private Timestamp create_time;

    /**
     * 更新时间
     */
    private Timestamp update_time;

    /**
     * 获取主键ID
     *
     * @return id - 主键ID
     */
    public String getId() {
        return id;
    }

    /**
     * 设置主键ID
     *
     * @param id 主键ID
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
     * 获取属性类型:phone/email/name/id_card/company/position
     *
     * @return attribute_type - 属性类型:phone/email/name/id_card/company/position
     */
    public String getAttribute_type() {
        return attribute_type;
    }

    /**
     * 设置属性类型:phone/email/name/id_card/company/position
     *
     * @param attribute_type 属性类型:phone/email/name/id_card/company/position
     */
    public void setAttribute_type(String attribute_type) {
        this.attribute_type = attribute_type;
    }

    /**
     * 获取属性值
     *
     * @return attribute_value - 属性值
     */
    public String getAttribute_value() {
        return attribute_value;
    }

    /**
     * 设置属性值
     *
     * @param attribute_value 属性值
     */
    public void setAttribute_value(String attribute_value) {
        this.attribute_value = attribute_value;
    }

    /**
     * 获取状态:1-有效,0-已解绑
     *
     * @return status - 状态:1-有效,0-已解绑
     */
    public String getStatus() {
        return status;
    }

    /**
     * 设置状态:1-有效,0-已解绑
     *
     * @param status 状态:1-有效,0-已解绑
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * 获取删除标志:0-未删除,1-已删除
     *
     * @return is_delete - 删除标志:0-未删除,1-已删除
     */
    public String getIs_delete() {
        return is_delete;
    }

    /**
     * 设置删除标志:0-未删除,1-已删除
     *
     * @param is_delete 删除标志:0-未删除,1-已删除
     */
    public void setIs_delete(String is_delete) {
        this.is_delete = is_delete;
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
}