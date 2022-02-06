package com.zyc.zdh.entity;

import java.sql.Timestamp;
import javax.persistence.*;

@Table(name = "product_tag_info")
public class ProductTagInfo {
    @Id
    private String id;

    /**
     * 产品标识code
     */
    private String product_code;

    /**
     * 产品名称
     */
    private String product_name;

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
     * 获取产品标识code
     *
     * @return product_code - 产品标识code
     */
    public String getProduct_code() {
        return product_code;
    }

    /**
     * 设置产品标识code
     *
     * @param product_code 产品标识code
     */
    public void setProduct_code(String product_code) {
        this.product_code = product_code;
    }

    /**
     * 获取产品名称
     *
     * @return product_name - 产品名称
     */
    public String getProduct_name() {
        return product_name;
    }

    /**
     * 设置产品名称
     *
     * @param product_name 产品名称
     */
    public void setProduct_name(String product_name) {
        this.product_name = product_name;
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