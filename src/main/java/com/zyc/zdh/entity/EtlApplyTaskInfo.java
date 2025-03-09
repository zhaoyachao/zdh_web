package com.zyc.zdh.entity;


import com.fasterxml.jackson.core.type.TypeReference;
import com.zyc.zdh.util.Const;
import com.zyc.zdh.util.JsonUtil;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.sql.Timestamp;
import java.util.List;

@Table
public class EtlApplyTaskInfo extends BaseProductAndDimGroupAuthInfo{

    @Id
    @Column
    private String id;

    @Column
    private String etl_context;

    //输入数据源id(申请流程ID)
    private String data_sources_choose_input;
    //输入数据源类型
    private String data_source_type_input;
    //输入数据源表名
    private String data_sources_table_name_input;
    //输入数据源文件名
    private String data_sources_file_name_input="";
    //输入数据源文件中字段名
    private String data_sources_file_columns="";
    //输入数据源表字段名
    private String data_sources_table_columns;
    //文件类型
    private String file_type_input;
    //文件编码
    private String encoding_input;
    //是否有头标题
    private String header_input;
    //文件分割符
    private String sep_input;
    //重新洗牌个数
    private String repartition_num_input;
    //重新洗牌字段
    private String repartition_cols_input;
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
    //文件类型
    private String file_type_output;
    //文件编码
    private String encoding_output;
    //是否有头标题
    private String header_output;
    //文件分割符
    private String sep_output;
    //写入模式
    private String model_output;
    //分区字段
    private String partition_by_output;
    //合并小文件个数
    private String merge_output;

    //输出数据源其他参数
    private String data_sources_params_output;

    //输入-输出 字段映射关系json
    private String column_datas;
    //输出数据源删除条件
    private String data_sources_clear_output;

    //输入-输出 字段映射关系class
    @Transient
    private List<column_data> column_data_list;

    private String owner;

    private Timestamp create_time;

    private String company;

    private String section;

    private String service;

    private String update_context;

    private String primary_columns;

    private String column_size;

    private String rows_range;//xxx-xxx

    private String error_rate;//容错率

    private String enable_quality;

    private String duplicate_columns;//去重字段

    private String is_delete= Const.NOT_DELETE;

    private Timestamp update_time;

    /**
     * 归属组
     */
    private String dim_group;

    /**
     * 归属产品
     */
    private String product_code;

    @Transient
    private String merge;

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
        if (getColumn_datas() != null && !getColumn_datas().equals("")) {
            return JsonUtil.toJavaObj(getColumn_datas(), new TypeReference<List<column_data>>() {});
        } else {
            return null;
        }
    }

    public void setColumn_data_list(List<column_data> column_data_list) {
        this.column_data_list = JsonUtil.toJavaObj(getColumn_datas(), new TypeReference<List<column_data>>() {});
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

    public String getPrimary_columns() {
        return primary_columns;
    }

    public void setPrimary_columns(String primary_columns) {
        this.primary_columns = primary_columns;
    }

    public String getColumn_size() {
        return column_size;
    }

    public void setColumn_size(String column_size) {
        this.column_size = column_size;
    }

    public String getRows_range() {
        return rows_range;
    }

    public void setRows_range(String rows_range) {
        this.rows_range = rows_range;
    }

    public String getError_rate() {
        return error_rate;
    }

    public void setError_rate(String error_rate) {
        this.error_rate = error_rate;
    }

    public String getEnable_quality() {
        return enable_quality;
    }

    public void setEnable_quality(String enable_quality) {
        this.enable_quality = enable_quality;
    }

    public String getDuplicate_columns() {
        return duplicate_columns;
    }

    public void setDuplicate_columns(String duplicate_columns) {
        this.duplicate_columns = duplicate_columns;
    }

    public String getFile_type_input() {
        return file_type_input;
    }

    public void setFile_type_input(String file_type_input) {
        this.file_type_input = file_type_input;
    }

    public String getEncoding_input() {
        return encoding_input;
    }

    public void setEncoding_input(String encoding_input) {
        this.encoding_input = encoding_input;
    }

    public String getSep_input() {
        return sep_input;
    }

    public void setSep_input(String sep_input) {
        this.sep_input = sep_input;
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

    public String getHeader_input() {
        return header_input;
    }

    public void setHeader_input(String header_input) {
        this.header_input = header_input;
    }

    public String getHeader_output() {
        return header_output;
    }

    public void setHeader_output(String header_output) {
        this.header_output = header_output;
    }

    public String getIs_delete() {
        return is_delete;
    }

    public void setIs_delete(String is_delete) {
        this.is_delete = is_delete;
    }

    public String getRepartition_num_input() {
        return repartition_num_input;
    }

    public void setRepartition_num_input(String repartition_num_input) {
        this.repartition_num_input = repartition_num_input;
    }

    public String getRepartition_cols_input() {
        return repartition_cols_input;
    }

    public void setRepartition_cols_input(String repartition_cols_input) {
        this.repartition_cols_input = repartition_cols_input;
    }

    public String getModel_output() {
        return model_output;
    }

    public void setModel_output(String model_output) {
        this.model_output = model_output;
    }

    public String getPartition_by_output() {
        return partition_by_output;
    }

    public void setPartition_by_output(String partition_by_output) {
        this.partition_by_output = partition_by_output;
    }

    public String getMerge_output() {
        return merge_output;
    }

    public void setMerge_output(String merge_output) {
        this.merge_output = merge_output;
    }

    public String getMerge() {
        return merge;
    }

    public void setMerge(String merge) {
        this.merge = merge;
    }

    public Timestamp getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(Timestamp update_time) {
        this.update_time = update_time;
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

