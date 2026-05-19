package com.zyc.zdh.entity;

import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;

@Table(name = "data_tag_info")
public class DataTagInfo extends BaseProductAuthInfo{
    @Id
    private String id;

    /**
     * 标识code
     */
    private String tag_code;

    /**
     * 标识名称
     */
    private String tag_name;

    /**
     * 产品code
     */
    private String product_code;

    /**
     * 拥有者
     */
    private String owner;

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
     * 获取标识code
     *
     * @return tag_code - 标识code
     */
    public String getTag_code() {
        return tag_code;
    }

    /**
     * 设置标识code
     *
     * @param tag_code 标识code
     */
    public void setTag_code(String tag_code) {
        this.tag_code = tag_code;
    }

    /**
     * 获取标识名称
     *
     * @return tag_name - 标识名称
     */
    public String getTag_name() {
        return tag_name;
    }

    /**
     * 设置标识名称
     *
     * @param tag_name 标识名称
     */
    public void setTag_name(String tag_name) {
        this.tag_name = tag_name;
    }

    /**
     * 获取产品code
     *
     * @return product_code - 产品code
     */
    @Override
    public String getProduct_code() {
        return product_code;
    }

    /**
     * 设置产品code
     *
     * @param product_code 产品code
     */
    @Override
    public void setProduct_code(String product_code) {
        this.product_code = product_code;
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
}