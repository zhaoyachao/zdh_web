package com.zyc.zdh.entity;

import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;

@Table(name = "wechat_tag_info")
public class WechatTagInfo extends BaseProductAuthInfo{
    @Id
    private String id;

    /**
     * 微信tag_id
     */
    private String tid;

    /**
     * 微信tag_name
     */
    private String tname;

    /**
     * 用户id类型
     */
    private Long count;

    /**
     * 公众号
     */
    private String wechat_app;

    /**
     * 拥有者
     */
    private String owner;

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
     * 产品code
     */
    private String product_code;

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
     * 获取微信tag_id
     *
     * @return tid - 微信tag_id
     */
    public String getTid() {
        return tid;
    }

    /**
     * 设置微信tag_id
     *
     * @param tid 微信tag_id
     */
    public void setTid(String tid) {
        this.tid = tid;
    }

    /**
     * 获取微信tag_name
     *
     * @return tname - 微信tag_name
     */
    public String getTname() {
        return tname;
    }

    /**
     * 设置微信tag_name
     *
     * @param tname 微信tag_name
     */
    public void setTname(String tname) {
        this.tname = tname;
    }

    /**
     * 获取用户id类型
     *
     * @return count - 用户id类型
     */
    public Long getCount() {
        return count;
    }

    /**
     * 设置用户id类型
     *
     * @param count 用户id类型
     */
    public void setCount(Long count) {
        this.count = count;
    }

    /**
     * 获取公众号
     *
     * @return wechat_app - 公众号
     */
    public String getWechat_app() {
        return wechat_app;
    }

    /**
     * 设置公众号
     *
     * @param wechat_app 公众号
     */
    public void setWechat_app(String wechat_app) {
        this.wechat_app = wechat_app;
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
}