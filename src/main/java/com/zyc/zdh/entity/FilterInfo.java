package com.zyc.zdh.entity;

import java.sql.Timestamp;
import javax.persistence.*;

@Table(name = "filter_info")
public class FilterInfo {
    @Id
    private String id;

    /**
     * 过滤code
     */
    private String filter_code;

    /**
     * 过滤名称
     */
    private String filter_name;

    /**
     * 账号
     */
    private String owner;

    /**
     * 启用状态,1:启用,2:未启用
     */
    private String enable;

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
     * 计算引擎,file,redis,hive
     */
    private String engine_type;

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
     * 获取过滤code
     *
     * @return filter_code - 过滤code
     */
    public String getFilter_code() {
        return filter_code;
    }

    /**
     * 设置过滤code
     *
     * @param filter_code 过滤code
     */
    public void setFilter_code(String filter_code) {
        this.filter_code = filter_code;
    }

    /**
     * 获取过滤名称
     *
     * @return filter_name - 过滤名称
     */
    public String getFilter_name() {
        return filter_name;
    }

    /**
     * 设置过滤名称
     *
     * @param filter_name 过滤名称
     */
    public void setFilter_name(String filter_name) {
        this.filter_name = filter_name;
    }

    /**
     * 获取账号
     *
     * @return owner - 账号
     */
    public String getOwner() {
        return owner;
    }

    /**
     * 设置账号
     *
     * @param owner 账号
     */
    public void setOwner(String owner) {
        this.owner = owner;
    }

    /**
     * 获取启用状态,1:启用,2:未启用
     *
     * @return enable - 启用状态,1:启用,2:未启用
     */
    public String getEnable() {
        return enable;
    }

    /**
     * 设置启用状态,1:启用,2:未启用
     *
     * @param enable 启用状态,1:启用,2:未启用
     */
    public void setEnable(String enable) {
        this.enable = enable;
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
     * 获取计算引擎
     * @return
     */
    public String getEngine_type() {
        return engine_type;
    }

    /**
     * 设置计算引擎
     * @param engine_type
     */
    public void setEngine_type(String engine_type) {
        this.engine_type = engine_type;
    }
}