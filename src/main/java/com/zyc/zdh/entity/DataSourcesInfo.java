package com.zyc.zdh.entity;

import java.sql.Timestamp;
import javax.persistence.*;

@Table(name = "data_sources_info")
public class DataSourcesInfo {
    @Id
    private String id;

    /**
     * 数据源说明
     */
    private String data_source_context;

    /**
     * 数据源类型
     */
    private String data_source_type;

    /**
     * 驱动连接串
     */
    private String driver;

    /**
     * 连接url
     */
    private String url;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 拥有者
     */
    private String owner;

    /**
     * 是否删除,0:未删除,1:删除
     */
    private String is_delete;

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
     * 获取数据源说明
     *
     * @return data_source_context - 数据源说明
     */
    public String getData_source_context() {
        return data_source_context;
    }

    /**
     * 设置数据源说明
     *
     * @param data_source_context 数据源说明
     */
    public void setData_source_context(String data_source_context) {
        this.data_source_context = data_source_context;
    }

    /**
     * 获取数据源类型
     *
     * @return data_source_type - 数据源类型
     */
    public String getData_source_type() {
        return data_source_type;
    }

    /**
     * 设置数据源类型
     *
     * @param data_source_type 数据源类型
     */
    public void setData_source_type(String data_source_type) {
        this.data_source_type = data_source_type;
    }

    /**
     * 获取驱动连接串
     *
     * @return driver - 驱动连接串
     */
    public String getDriver() {
        return driver;
    }

    /**
     * 设置驱动连接串
     *
     * @param driver 驱动连接串
     */
    public void setDriver(String driver) {
        this.driver = driver;
    }

    /**
     * 获取连接url
     *
     * @return url - 连接url
     */
    public String getUrl() {
        return url;
    }

    /**
     * 设置连接url
     *
     * @param url 连接url
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * 获取用户名
     *
     * @return username - 用户名
     */
    public String getUsername() {
        return username;
    }

    /**
     * 设置用户名
     *
     * @param username 用户名
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * 获取密码
     *
     * @return password - 密码
     */
    public String getPassword() {
        return password;
    }

    /**
     * 设置密码
     *
     * @param password 密码
     */
    public void setPassword(String password) {
        this.password = password;
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