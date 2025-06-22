package com.zyc.zdh.entity;

import com.zyc.zdh.util.Const;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;

@Table
public class EtlTaskJdbcInfo extends BaseProductAndDimGroupAuthInfo{

    /**
     * 主键ID
     */
    @Id
    @Column
    private String id ;
    /**
     * 任务说明
     */
    private String etl_context ;
    /**
     * 计算引擎,local,spark,目前只支持local
     */
    private String engine_type;
    /**
     * 数据源ID
     */
    private String data_sources_choose_input ;
    /**
     * 数据源类型
     */
    private String data_source_type_input ;
    /**
     * jdbc sql
     */
    private String etl_sql ;
    /**
     * 输出数据源ID
     */
    private String data_sources_choose_output ;
    /**
     * 文件类型
     */
    private String file_type_output;
    /**
     * 文件编码
     */
    private String encoding_output;
    /**
     * 是否有头标题
     */
    private String header_output;
    /**
     * 文件分割符
     */
    private String sep_output;
    /**
     * 写入模式
     */
    private String model_output;
    /**
     * 分区字段
     */
    private String partition_by_output;
    /**
     * 合并小文件个数
     */
    private String merge_output;
    /**
     * 废弃
     */
    private String data_source_type_output ;
    /**
     * 废弃
     */
    private String data_sources_table_name_output ;
    /**
     * 废弃
     */
    private String data_sources_file_name_output ;
    /**
     * 废弃
     */
    private String data_sources_params_output ;
    /**
     * 废弃
     */
    private String data_sources_clear_output;
    /**
     * 拥有者
     */
    private String owner ;
    /**
     * 创建时间
     */
    private Timestamp create_time ;
    /**
     * 公司
     */
    private String company  ;
    /**
     * 部门
     */
    private String section  ;
    /**
     * 业务
     */
    private String service  ;
    /**
     * 更新内容
     */
    private String update_context ;
    /**
     * 更新时间
     */
    private Timestamp update_time;
    /**
     * 是否删除,0:否,1:删除
     */
    private String is_delete= Const.NOT_DELETE;

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

    public String getEngine_type() {
        return engine_type;
    }

    public void setEngine_type(String engine_type) {
        this.engine_type = engine_type;
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

    public String getHeader_output() {
        return header_output;
    }

    public void setHeader_output(String header_output) {
        this.header_output = header_output;
    }

    public String getSep_output() {
        return sep_output;
    }

    public void setSep_output(String sep_output) {
        this.sep_output = sep_output;
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

    public Timestamp getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(Timestamp update_time) {
        this.update_time = update_time;
    }

    public String getIs_delete() {
        return is_delete;
    }

    public void setIs_delete(String is_delete) {
        this.is_delete = is_delete;
    }

    @Override
    public String getDim_group() {
        return dim_group;
    }

    @Override
    public void setDim_group(String dim_group) {
        this.dim_group = dim_group;
    }

    @Override
    public String getProduct_code() {
        return product_code;
    }

    @Override
    public void setProduct_code(String product_code) {
        this.product_code = product_code;
    }
}
