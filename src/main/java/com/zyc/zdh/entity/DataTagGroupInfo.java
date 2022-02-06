package com.zyc.zdh.entity;

import java.sql.Timestamp;
import javax.persistence.*;

@Table(name = "data_tag_group_info")
public class DataTagGroupInfo {
    @Id
    private String id;

    /**
     * 标识组code
     */
    private String tag_group_code;

    /**
     * 标识组名称
     */
    private String tag_group_name;

    /**
     * 标识code列表,逗号分割
     */
    private String tag_codes;

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
     * 获取标识组code
     *
     * @return tag_group_code - 标识组code
     */
    public String getTag_group_code() {
        return tag_group_code;
    }

    /**
     * 设置标识组code
     *
     * @param tag_group_code 标识组code
     */
    public void setTag_group_code(String tag_group_code) {
        this.tag_group_code = tag_group_code;
    }

    /**
     * 获取标识组名称
     *
     * @return tag_group_name - 标识组名称
     */
    public String getTag_group_name() {
        return tag_group_name;
    }

    /**
     * 设置标识组名称
     *
     * @param tag_group_name 标识组名称
     */
    public void setTag_group_name(String tag_group_name) {
        this.tag_group_name = tag_group_name;
    }

    /**
     * 获取标识code列表,逗号分割
     *
     * @return tag_codes - 标识code列表,逗号分割
     */
    public String getTag_codes() {
        return tag_codes;
    }

    /**
     * 设置标识code列表,逗号分割
     *
     * @param tag_codes 标识code列表,逗号分割
     */
    public void setTag_codes(String tag_codes) {
        this.tag_codes = tag_codes;
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