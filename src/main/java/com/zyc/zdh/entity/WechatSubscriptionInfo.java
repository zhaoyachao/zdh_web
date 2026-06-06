package com.zyc.zdh.entity;

import java.sql.Timestamp;
import java.util.Date;
import javax.persistence.*;

@Table(name = "wechat_subscription_info")
public class WechatSubscriptionInfo extends BaseWechatChannelAuthInfo{
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
     * 备注
     */
    private String remark;

    /**
     * 关注状态:1-关注,2-取消关注
     */
    private String status;

    /**
     * 关注时间
     */
    private Timestamp subscribe_time;

    /**
     * 取注时间
     */
    private Timestamp unsubscribe_time;

    /**
     * 关注渠道,ADD_SCENE_SEARCH 公众号搜索，ADD_SCENE_ACCOUNT_MIGRATION 公众号迁移，ADD_SCENE_PROFILE_CARD 名片分享，ADD_SCENE_QR_CODE 扫描二维码，ADD_SCENE_PROFILE_LINK 图文页内名称点击，ADD_SCENE_PROFILE_ITEM 图文页右上角菜单，ADD_SCENE_PAID 支付后关注，ADD_SCENE_WECHAT_ADVERTISEMENT 微信广告，ADD_SCENE_REPRINT 他人转载，ADD_SCENE_LIVESTREAM 视频号直播，ADD_SCENE_CHANNELS 视频号，ADD_SCENE_WXA 小程序关注，ADD_SCENE_OTHERS 其他
     */
    private String subscribe_scene;

    /**
     * 二维码扫码场景描述（开发者自定义）
     */
    private String qr_scene_str;

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
     * 获取备注
     *
     * @return remark - 备注
     */
    public String getRemark() {
        return remark;
    }

    /**
     * 设置备注
     *
     * @param remark 备注
     */
    public void setRemark(String remark) {
        this.remark = remark;
    }

    /**
     * 获取关注状态:1-关注,2-取消关注
     *
     * @return status - 关注状态:1-关注,2-取消关注
     */
    public String getStatus() {
        return status;
    }

    /**
     * 设置关注状态:1-关注,2-取消关注
     *
     * @param status 关注状态:1-关注,2-取消关注
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * 获取关注时间
     *
     * @return subscribe_time - 关注时间
     */
    public Timestamp getSubscribe_time() {
        return subscribe_time;
    }

    /**
     * 设置关注时间
     *
     * @param subscribe_time 关注时间
     */
    public void setSubscribe_time(Timestamp subscribe_time) {
        this.subscribe_time = subscribe_time;
    }

    /**
     * 获取取注时间
     *
     * @return unsubscribe_time - 取注时间
     */
    public Timestamp getUnsubscribe_time() {
        return unsubscribe_time;
    }

    /**
     * 设置取注时间
     *
     * @param unsubscribe_time 取注时间
     */
    public void setUnsubscribe_time(Timestamp unsubscribe_time) {
        this.unsubscribe_time = unsubscribe_time;
    }

    /**
     * 获取关注渠道,ADD_SCENE_SEARCH 公众号搜索，ADD_SCENE_ACCOUNT_MIGRATION 公众号迁移，ADD_SCENE_PROFILE_CARD 名片分享，ADD_SCENE_QR_CODE 扫描二维码，ADD_SCENE_PROFILE_LINK 图文页内名称点击，ADD_SCENE_PROFILE_ITEM 图文页右上角菜单，ADD_SCENE_PAID 支付后关注，ADD_SCENE_WECHAT_ADVERTISEMENT 微信广告，ADD_SCENE_REPRINT 他人转载，ADD_SCENE_LIVESTREAM 视频号直播，ADD_SCENE_CHANNELS 视频号，ADD_SCENE_WXA 小程序关注，ADD_SCENE_OTHERS 其他
     *
     * @return subscribe_scene - 关注渠道,ADD_SCENE_SEARCH 公众号搜索，ADD_SCENE_ACCOUNT_MIGRATION 公众号迁移，ADD_SCENE_PROFILE_CARD 名片分享，ADD_SCENE_QR_CODE 扫描二维码，ADD_SCENE_PROFILE_LINK 图文页内名称点击，ADD_SCENE_PROFILE_ITEM 图文页右上角菜单，ADD_SCENE_PAID 支付后关注，ADD_SCENE_WECHAT_ADVERTISEMENT 微信广告，ADD_SCENE_REPRINT 他人转载，ADD_SCENE_LIVESTREAM 视频号直播，ADD_SCENE_CHANNELS 视频号，ADD_SCENE_WXA 小程序关注，ADD_SCENE_OTHERS 其他
     */
    public String getSubscribe_scene() {
        return subscribe_scene;
    }

    /**
     * 设置关注渠道,ADD_SCENE_SEARCH 公众号搜索，ADD_SCENE_ACCOUNT_MIGRATION 公众号迁移，ADD_SCENE_PROFILE_CARD 名片分享，ADD_SCENE_QR_CODE 扫描二维码，ADD_SCENE_PROFILE_LINK 图文页内名称点击，ADD_SCENE_PROFILE_ITEM 图文页右上角菜单，ADD_SCENE_PAID 支付后关注，ADD_SCENE_WECHAT_ADVERTISEMENT 微信广告，ADD_SCENE_REPRINT 他人转载，ADD_SCENE_LIVESTREAM 视频号直播，ADD_SCENE_CHANNELS 视频号，ADD_SCENE_WXA 小程序关注，ADD_SCENE_OTHERS 其他
     *
     * @param subscribe_scene 关注渠道,ADD_SCENE_SEARCH 公众号搜索，ADD_SCENE_ACCOUNT_MIGRATION 公众号迁移，ADD_SCENE_PROFILE_CARD 名片分享，ADD_SCENE_QR_CODE 扫描二维码，ADD_SCENE_PROFILE_LINK 图文页内名称点击，ADD_SCENE_PROFILE_ITEM 图文页右上角菜单，ADD_SCENE_PAID 支付后关注，ADD_SCENE_WECHAT_ADVERTISEMENT 微信广告，ADD_SCENE_REPRINT 他人转载，ADD_SCENE_LIVESTREAM 视频号直播，ADD_SCENE_CHANNELS 视频号，ADD_SCENE_WXA 小程序关注，ADD_SCENE_OTHERS 其他
     */
    public void setSubscribe_scene(String subscribe_scene) {
        this.subscribe_scene = subscribe_scene;
    }

    /**
     * 获取二维码扫码场景描述（开发者自定义）
     *
     * @return qr_scene_str - 二维码扫码场景描述（开发者自定义）
     */
    public String getQr_scene_str() {
        return qr_scene_str;
    }

    /**
     * 设置二维码扫码场景描述（开发者自定义）
     *
     * @param qr_scene_str 二维码扫码场景描述（开发者自定义）
     */
    public void setQr_scene_str(String qr_scene_str) {
        this.qr_scene_str = qr_scene_str;
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