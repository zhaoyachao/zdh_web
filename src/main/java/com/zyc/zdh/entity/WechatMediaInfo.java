package com.zyc.zdh.entity;

import java.sql.Timestamp;
import javax.persistence.*;

@Table(name = "wechat_media_info")
public class WechatMediaInfo extends BaseProductAuthInfo{
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
     * 规则名称
     */
    private String media_name;

    /**
     * 素材说明
     */
    private String media_desc;

    /**
     * 素材分类,1:永久,2:临时
     */
    private String media_category;

    /**
     * 素材类型,image, voice, video, thumb
     */
    private String media_type;

    /**
     * 素材字符串,base64加密,素材不超过1M大小
     */
    private String media_str;
    /**
     * 素材id
     */
    private String media_id;

    /**
     * 视频标题
     */
    private String title;

    /**
     * 拥有者
     */
    private String owner;

    /**
     * 产品code
     */
    private String product_code;

    /**
     * 是否删除,0:未删除,1:删除
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
     * 素材链接
     */
    private String url;

    /**
     * 视频标题简介
     */
    private String introduction;

    private String valid_start_time;

    private String valid_end_time;

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
     * 获取规则名称
     *
     * @return media_name - 规则名称
     */
    public String getMedia_name() {
        return media_name;
    }

    /**
     * 设置规则名称
     *
     * @param media_name 规则名称
     */
    public void setMedia_name(String media_name) {
        this.media_name = media_name;
    }

    /**
     * 获取素材说明
     *
     * @return media_desc - 素材说明
     */
    public String getMedia_desc() {
        return media_desc;
    }

    /**
     * 设置素材说明
     *
     * @param media_desc 素材说明
     */
    public void setMedia_desc(String media_desc) {
        this.media_desc = media_desc;
    }

    /**
     * 获取素材分类,1:永久,2:临时
     *
     * @return media_category - 素材分类,1:永久,2:临时
     */
    public String getMedia_category() {
        return media_category;
    }

    /**
     * 设置素材分类,1:永久,2:临时
     *
     * @param media_category 素材分类,1:永久,2:临时
     */
    public void setMedia_category(String media_category) {
        this.media_category = media_category;
    }

    /**
     * 获取素材类型,image, voice, video, thumb
     *
     * @return media_type - 素材类型,image, voice, video, thumb
     */
    public String getMedia_type() {
        return media_type;
    }

    /**
     * 设置素材类型,image, voice, video, thumb
     *
     * @param media_type 素材类型,image, voice, video, thumb
     */
    public void setMedia_type(String media_type) {
        this.media_type = media_type;
    }


    public String getMedia_str() {
        return media_str;
    }

    public void setMedia_str(String media_str) {
        this.media_str = media_str;
    }

    /**
     * 获取素材id
     *
     * @return media_id - 素材id
     */
    public String getMedia_id() {
        return media_id;
    }

    /**
     * 设置素材id
     *
     * @param media_id 素材id
     */
    public void setMedia_id(String media_id) {
        this.media_id = media_id;
    }

    /**
     * 获取视频标题
     *
     * @return title - 视频标题
     */
    public String getTitle() {
        return title;
    }

    /**
     * 设置视频标题
     *
     * @param title 视频标题
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * 获取拥有者
     *
     * @return owner - 拥有者
     */
    public String getOwner() {
        return owner;
    }

    /**
     * 设置拥有者
     *
     * @param owner 拥有者
     */
    public void setOwner(String owner) {
        this.owner = owner;
    }

    /**
     * 获取产品code
     *
     * @return product_code - 产品code
     */
    public String getProduct_code() {
        return product_code;
    }

    /**
     * 设置产品code
     *
     * @param product_code 产品code
     */
    public void setProduct_code(String product_code) {
        this.product_code = product_code;
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
     * 获取素材链接
     *
     * @return url - 素材链接
     */
    public String getUrl() {
        return url;
    }

    /**
     * 设置素材链接
     *
     * @param url 素材链接
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * 获取视频标题简介
     *
     * @return introduction - 视频标题简介
     */
    public String getIntroduction() {
        return introduction;
    }

    /**
     * 设置视频标题简介
     *
     * @param introduction 视频标题简介
     */
    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getValid_start_time() {
        return valid_start_time;
    }

    public void setValid_start_time(String valid_start_time) {
        this.valid_start_time = valid_start_time;
    }

    public String getValid_end_time() {
        return valid_end_time;
    }

    public void setValid_end_time(String valid_end_time) {
        this.valid_end_time = valid_end_time;
    }
}