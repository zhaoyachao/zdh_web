package com.zyc.zdh.entity;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;

@Table
public class SqlTaskInfo {

    @Id
    @Column
    private String id ;
    private String sql_context ;
    private String data_sources_choose_input ;
    private String data_source_type_input ;
    private String data_sources_params_input ;
    private String etl_sql ;
    private String data_sources_choose_output ;
    private String data_source_type_output ;
    private String data_sources_table_name_output ;
    private String data_sources_file_name_output ;
    private String data_sources_params_output ;
    private String data_sources_clear_output;
    private String owner ;
    private Timestamp create_time ;
    private String company  ;
    private String section  ;
    private String service  ;
    private String update_context ;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSql_context() {
        return sql_context;
    }

    public void setSql_context(String sql_context) {
        this.sql_context = sql_context;
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

    public String getData_sources_params_input() {
        return data_sources_params_input;
    }

    public void setData_sources_params_input(String data_sources_params_input) {
        this.data_sources_params_input = data_sources_params_input;
    }

    public String getEtl_sql() {
        return etl_sql;
    }

    public void setEtl_sql(String etl_sql) {
        this.etl_sql = etl_sql;
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

    public String getData_sources_params_output() {
        return data_sources_params_output;
    }

    public void setData_sources_params_output(String data_sources_params_output) {
        this.data_sources_params_output = data_sources_params_output;
    }

    public String getData_sources_clear_output() {
        return data_sources_clear_output;
    }

    public void setData_sources_clear_output(String data_sources_clear_output) {
        this.data_sources_clear_output = data_sources_clear_output;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
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
}
