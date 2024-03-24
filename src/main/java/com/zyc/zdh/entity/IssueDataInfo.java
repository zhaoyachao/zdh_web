package com.zyc.zdh.entity;


import com.alibaba.fastjson.JSONArray;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

@Table
public class IssueDataInfo implements Serializable {

    /**
     * 主键ID
     */
    @Id
    @Column
    private String id;
    /**
     * 发布说明
     */
    @Column
    private String issue_context;

    /**
     * 输入数据源id
     */
    private String data_sources_choose_input;
    /**
     * 输入数据源类型
     */
    private String data_source_type_input;
    /**
     * 输入数据源表名
     */
    private String data_sources_table_name_input;
    /**
     * 输入数据源文件名
     */
    private String data_sources_file_name_input;
    /**
     * 输入数据源文件中字段名
     */
    private String data_sources_file_columns;
    /**
     * 输入数据源表字段名
     */
    private String data_sources_table_columns;

    /**
     * 输入-输出 字段映射关系json
     */
    private String column_datas;

    //输入-输出 字段映射关系class
    @Transient
    private List<column_data> column_data_list;

    /**
     * 拥有者
     */
    private String owner;

    /**
     * 创建时间
     */
    private Timestamp create_time;

    /**
     * 公司
     */
    private String company;

    /**
     * 部门
     */
    private String section;

    /**
     * 业务
     */
    private String service;

    /**
     * 更新内容
     */
    private String update_context;

    @Transient
    private String user_name;

    /**
     * 状态：1：已发布, 2:未发布
     */
    private String status;//

    /**
     * 数据标签
     */
    private String label_params="";

    public String getIssue_context() {
        return issue_context;
    }

    public void setIssue_context(String issue_context) {
        this.issue_context = issue_context;
    }


    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }


    public String getData_sources_choose_input() {
        return data_sources_choose_input;
    }

    public void setData_sources_choose_input(String data_sources_choose_input) {
        this.data_sources_choose_input = data_sources_choose_input;
    }

    public String getData_source_type_input() {
        return data_source_type_input;
    }

    public void setData_source_type_input(String data_source_type_input) {
        this.data_source_type_input = data_source_type_input;
    }

    public String getData_sources_table_name_input() {
        return data_sources_table_name_input;
    }

    public void setData_sources_table_name_input(String data_sources_table_name_input) {
        this.data_sources_table_name_input = data_sources_table_name_input;
    }

    public String getData_sources_file_name_input() {
        return data_sources_file_name_input;
    }

    public void setData_sources_file_name_input(String data_sources_file_name_input) {
        this.data_sources_file_name_input = data_sources_file_name_input;
    }

    public String getData_sources_file_columns() {
        return data_sources_file_columns;
    }

    public void setData_sources_file_columns(String data_sources_file_columns) {
        this.data_sources_file_columns = data_sources_file_columns;
    }

    public String getData_sources_table_columns() {
        return data_sources_table_columns;
    }

    public void setData_sources_table_columns(String data_sources_table_columns) {
        this.data_sources_table_columns = data_sources_table_columns;
    }



    public String getColumn_datas() {
        return column_datas;
    }

    public void setColumn_datas(String column_datas) {
        this.column_datas = column_datas;
    }

    public List<column_data> getColumn_data_list() {
        if (getColumn_datas() != null && !getColumn_datas().equals("")) {
            return JSONArray.parseArray(getColumn_datas(), column_data.class);
        } else {
            return null;
        }
    }

    public void setColumn_data_list(List<column_data> column_data_list) {
        this.column_data_list = JSONArray.parseArray(getColumn_datas(), column_data.class);
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Timestamp getCreate_time() {
        return create_time;
    }

    public void setCreate_time(Timestamp create_time) {
        this.create_time = create_time;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getUpdate_context() {
        return update_context;
    }

    public void setUpdate_context(String update_context) {
        this.update_context = update_context;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLabel_params() {
        return label_params;
    }

    public void setLabel_params(String label_params) {
        this.label_params = label_params;
    }
}

