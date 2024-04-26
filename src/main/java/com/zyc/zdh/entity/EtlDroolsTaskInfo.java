package com.zyc.zdh.entity;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;

@Table
public class EtlDroolsTaskInfo {

    /**
     * 主键ID
     */
    @Id
    @Column
    private String id;

    /**
     * etl任务ID
     */
    private String etl_id;

    /**
     * 任务模板,ETL,MORE_ETL,SQL
     */
    private String more_task;

    /**
     * drools任务说明
     */
    private String etl_context;

    /**
     * drools逻辑
     */
    private String etl_drools;
    /**
     * 输入源过滤
     */
    private String data_sources_filter_input;

    /**
     * 删除临时表名,多个逗号分割
     */
    private String drop_tmp_tables;

    /**
     * 输出数据源id
     */
    private String data_sources_choose_output;
    /**
     * 输出数据源类型
     */
    private String data_source_type_output;
    /**
     * 输出数据源表名
     */
    private String data_sources_table_name_output;
    /**
     * 输出数据源文件名
     */
    private String data_sources_file_name_output;
    /**
     * 文件类型
     */
    private String file_type_output;
    /**
     * 文件编码
     */
    private String encoding_output;
    /**
     * 文件分割符
     */
    private String sep_output;
    /**
     * 输出数据源其他参数
     */
    private String data_sources_params_output;
    /**
     * 输出数据源删除条件
     */
    private String data_sources_clear_output;

    /**
     * 拥有者
     */
    private String owner;

    /**
     * 创建时间
     */
    private Timestamp create_time;

    /**
     * 归属组
     */
    private String dim_group;

    /**
     * 归属产品
     */
    private String product_code;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getEtl_context() {
        return etl_context;
    }

    public void setEtl_context(String etl_context) {
        this.etl_context = etl_context;
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

    public String getDrop_tmp_tables() {
        return drop_tmp_tables;
    }

    public void setDrop_tmp_tables(String drop_tmp_tables) {
        this.drop_tmp_tables = drop_tmp_tables;
    }

    public String getFile_type_output() {
        return file_type_output;
    }

    public void setFile_type_output(String file_type_output) {
        this.file_type_output = file_type_output;
    }

    public String getEncoding_output() {
        return encoding_output;
    }

    public void setEncoding_output(String encoding_output) {
        this.encoding_output = encoding_output;
    }

    public String getSep_output() {
        return sep_output;
    }

    public void setSep_output(String sep_output) {
        this.sep_output = sep_output;
    }

    public String getEtl_id() {
        return etl_id;
    }

    public void setEtl_id(String etl_id) {
        this.etl_id = etl_id;
    }

    public String getEtl_drools() {
        return etl_drools;
    }

    public void setEtl_drools(String etl_drools) {
        this.etl_drools = etl_drools;
    }

    public String getData_sources_filter_input() {
        return data_sources_filter_input;
    }

    public void setData_sources_filter_input(String data_sources_filter_input) {
        this.data_sources_filter_input = data_sources_filter_input;
    }

    public String getMore_task() {
        return more_task;
    }

    public void setMore_task(String more_task) {
        this.more_task = more_task;
    }

    public String getDim_group() {
        return dim_group;
    }

    public void setDim_group(String dim_group) {
        this.dim_group = dim_group;
    }

    public String getProduct_code() {
        return product_code;
    }

    public void setProduct_code(String product_code) {
        this.product_code = product_code;
    }
}
