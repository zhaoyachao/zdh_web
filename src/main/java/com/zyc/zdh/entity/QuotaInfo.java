package com.zyc.zdh.entity;


import com.alibaba.fastjson.JSONArray;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.sql.Timestamp;
import java.util.List;

@Table
public class QuotaInfo {

    @Id
    @Column
    private String id;

    @Column
    private String etl_context;

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
    //输入数据源其他参数
    private String data_sources_params_input;

    //输入数据源过滤条件
    private String data_sources_filter_input;

    //输出数据源id
    private String data_sources_choose_output;
    //输出数据源类型
    private String data_source_type_output;
    //输出数据源表名
    private String data_sources_table_name_output;
    //输出数据源文件名
    private String data_sources_file_name_output;
    //输出数据源其他参数
    private String data_sources_params_output;

    //输入-输出 字段映射关系json
    private String column_datas;
    //输出数据源删除条件
    private String data_sources_clear_output;

    //输入-输出 字段映射关系class
    @Transient
    private List<column_data> column_data_list;

    private String  owner;

    private Timestamp create_time;

    private String path;

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

    public String getData_sources_choose_output() {
        return data_sources_choose_output;
    }

    public void setData_sources_choose_output(String data_sources_choose_output) {
        this.data_sources_choose_output = data_sources_choose_output;
    }

    public String getData_source_type_output() {
        return data_source_type_output;
    }

    public void setData_source_type_output(String data_source_type_output) {
        this.data_source_type_output = data_source_type_output;
    }

    public String getData_sources_table_name_output() {
        return data_sources_table_name_output;
    }

    public void setData_sources_table_name_output(String data_sources_table_name_output) {
        this.data_sources_table_name_output = data_sources_table_name_output;
    }

    public String getData_sources_file_name_output() {
        return data_sources_file_name_output;
    }

    public void setData_sources_file_name_output(String data_sources_file_name_output) {
        this.data_sources_file_name_output = data_sources_file_name_output;
    }

    public String getColumn_datas() {
        return column_datas;
    }

    public void setColumn_datas(String column_datas) {
        this.column_datas = column_datas;
    }

    public List<column_data> getColumn_data_list() {
        if(getColumn_datas()!=null && !getColumn_datas().equals(""))
        return JSONArray.parseArray(getColumn_datas(),column_data.class);
        else
            return null;
    }

    public void setColumn_data_list(List<column_data> column_data_list) {
        this.column_data_list = JSONArray.parseArray(getColumn_datas(),column_data.class);
    }


    public String getData_sources_params_input() {
        return data_sources_params_input;
    }

    public void setData_sources_params_input(String data_sources_params_input) {
        this.data_sources_params_input = data_sources_params_input;
    }

    public String getData_sources_params_output() {
        return data_sources_params_output;
    }

    public void setData_sources_params_output(String data_sources_params_output) {
        this.data_sources_params_output = data_sources_params_output;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getData_sources_filter_input() {
        return data_sources_filter_input;
    }

    public void setData_sources_filter_input(String data_sources_filter_input) {
        this.data_sources_filter_input = data_sources_filter_input;
    }

    public String getData_sources_clear_output() {
        return data_sources_clear_output;
    }

    public void setData_sources_clear_output(String data_sources_clear_output) {
        this.data_sources_clear_output = data_sources_clear_output;
    }

    public String getEtl_context() {
        return etl_context;
    }

    public void setEtl_context(String etl_context) {
        this.etl_context = etl_context;
    }

    public Timestamp getCreate_time() {
        return create_time;
    }

    public void setCreate_time(Timestamp create_time) {
        this.create_time = create_time;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}

