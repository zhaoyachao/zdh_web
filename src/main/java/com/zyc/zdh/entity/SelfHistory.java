package com.zyc.zdh.entity;

import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;

@Table(name = "self_history")
public class SelfHistory {
    @Id
    private String id;

    /**
     * 说明
     */
    private String history_context;

    /**
     * 输入数据源id
     */
    private String data_sources_choose_input;

    /**
     * 账号
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
     * sql
     */
    private String etl_sql;

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
     * 获取说明
     *
     * @return history_context - 说明
     */
    public String getHistory_context() {
        return history_context;
    }

    /**
     * 设置说明
     *
     * @param history_context 说明
     */
    public void setHistory_context(String history_context) {
        this.history_context = history_context;
    }

    /**
     * 获取输入数据源id
     *
     * @return data_sources_choose_input - 输入数据源id
     */
    public String getData_sources_choose_input() {
        return data_sources_choose_input;
    }

    /**
     * 设置输入数据源id
     *
     * @param data_sources_choose_input 输入数据源id
     */
    public void setData_sources_choose_input(String data_sources_choose_input) {
        this.data_sources_choose_input = data_sources_choose_input;
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
     * 获取sql
     *
     * @return etl_sql - sql
     */
    public String getEtl_sql() {
        return etl_sql;
    }

    /**
     * 设置sql
     *
     * @param etl_sql sql
     */
    public void setEtl_sql(String etl_sql) {
        this.etl_sql = etl_sql;
    }
}