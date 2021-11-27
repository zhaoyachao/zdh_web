package com.zyc.zdh.entity;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;


public class ApplyIssueInfo {

    private String id;

    //审批单说明
    private String apply_context;

    //发布的数据信息id
    private String issue_id;

    //审批人id
    private String approve_id;

    //状态
    private String status;

    private Timestamp create_time;

    private Timestamp update_time;

    private String owner;

    private String reason;

    private String issue_context;

    //输入数据源id
    private String data_sources_choose_input;
    //输入数据源类型
    private String data_source_type_input;
    //输入数据源表名
    private String data_sources_table_name_input;
    //输入数据源文件名
    private String data_sources_file_name_input;
    //输入数据源文件中字段名
    private String data_sources_file_columns;
    //输入数据源表字段名
    private String data_sources_table_columns;

    //输入-输出 字段映射关系json
    private String column_datas;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getApply_context() {
        return apply_context;
    }

    public void setApply_context(String apply_context) {
        this.apply_context = apply_context;
    }

    public String getIssue_id() {
        return issue_id;
    }

    public void setIssue_id(String issue_id) {
        this.issue_id = issue_id;
    }

    public String getApprove_id() {
        return approve_id;
    }

    public void setApprove_id(String approve_id) {
        this.approve_id = approve_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Timestamp getCreate_time() {
        return create_time;
    }

    public void setCreate_time(Timestamp create_time) {
        this.create_time = create_time;
    }

    public Timestamp getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(Timestamp update_time) {
        this.update_time = update_time;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getIssue_context() {
        return issue_context;
    }

    public void setIssue_context(String issue_context) {
        this.issue_context = issue_context;
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
}
