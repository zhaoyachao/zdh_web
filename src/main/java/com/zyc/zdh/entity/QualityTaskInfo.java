package com.zyc.zdh.entity;

import com.zyc.zdh.util.JsonUtil;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Table(name = "quality_task_info")
public class QualityTaskInfo {
    @Id
    private String id;

    /**
     * 任务说明
     */
    private String quality_context;

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
     * 输入数据源参数
     */
    private String data_sources_params_input;

    /**
     * 输入数据源过滤条件
     */
    private String data_sources_filter_input;

    /**
     * 输入文件类型
     */
    private String file_type_input;

    /**
     * 输入文件编码
     */
    private String encoding_input;

    /**
     * 输入分割符
     */
    private String sep_input;

    /**
     * 输入是否包含表头
     */
    private String header_input;

    /**
     * 洗牌个数默认空
     */
    private String repartition_num_input;

    /**
     * 洗牌字段默认空
     */
    private String repartition_cols_input;

    /**
     * 拥有者
     */
    private String owner;

    /**
     * 创建时间
     */
    private Timestamp create_time;

    /**
     * 更新时间
     */
    private Timestamp update_time;

    /**
     * 表所属公司
     */
    private String company;

    /**
     * 表所属部门
     */
    private String section;

    /**
     * 表所属服务
     */
    private String service;

    /**
     * 更新说明
     */
    private String update_context;

    /**
     * 输入表字段名
     */
    private String data_sources_table_columns;

    /**
     * 输入文件字段名
     */
    private String data_sources_file_columns;

    /**
     * 规则配置json
     */
    private String quality_rule_config;

    @Transient
    private List<Map<String, Object>>  quality_rule_list;

    /**
     * 产品code
     */
    private String product_code;

    /**
     * 归属组
     */
    private String dim_group;

    /**
     * 逻辑删除
     */
    private String is_delete;

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
     * 获取任务说明
     *
     * @return quality_context - 任务说明
     */
    public String getQuality_context() {
        return quality_context;
    }

    /**
     * 设置任务说明
     *
     * @param quality_context 任务说明
     */
    public void setQuality_context(String quality_context) {
        this.quality_context = quality_context;
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
     * 获取输入数据源类型
     *
     * @return data_source_type_input - 输入数据源类型
     */
    public String getData_source_type_input() {
        return data_source_type_input;
    }

    /**
     * 设置输入数据源类型
     *
     * @param data_source_type_input 输入数据源类型
     */
    public void setData_source_type_input(String data_source_type_input) {
        this.data_source_type_input = data_source_type_input;
    }

    /**
     * 获取输入数据源表名
     *
     * @return data_sources_table_name_input - 输入数据源表名
     */
    public String getData_sources_table_name_input() {
        return data_sources_table_name_input;
    }

    /**
     * 设置输入数据源表名
     *
     * @param data_sources_table_name_input 输入数据源表名
     */
    public void setData_sources_table_name_input(String data_sources_table_name_input) {
        this.data_sources_table_name_input = data_sources_table_name_input;
    }

    /**
     * 获取输入数据源文件名
     *
     * @return data_sources_file_name_input - 输入数据源文件名
     */
    public String getData_sources_file_name_input() {
        return data_sources_file_name_input;
    }

    /**
     * 设置输入数据源文件名
     *
     * @param data_sources_file_name_input 输入数据源文件名
     */
    public void setData_sources_file_name_input(String data_sources_file_name_input) {
        this.data_sources_file_name_input = data_sources_file_name_input;
    }

    /**
     * 获取输入数据源参数
     *
     * @return data_sources_params_input - 输入数据源参数
     */
    public String getData_sources_params_input() {
        return data_sources_params_input;
    }

    /**
     * 设置输入数据源参数
     *
     * @param data_sources_params_input 输入数据源参数
     */
    public void setData_sources_params_input(String data_sources_params_input) {
        this.data_sources_params_input = data_sources_params_input;
    }

    /**
     * 获取输入数据源过滤条件
     *
     * @return data_sources_filter_input - 输入数据源过滤条件
     */
    public String getData_sources_filter_input() {
        return data_sources_filter_input;
    }

    /**
     * 设置输入数据源过滤条件
     *
     * @param data_sources_filter_input 输入数据源过滤条件
     */
    public void setData_sources_filter_input(String data_sources_filter_input) {
        this.data_sources_filter_input = data_sources_filter_input;
    }

    /**
     * 获取输入文件类型
     *
     * @return file_type_input - 输入文件类型
     */
    public String getFile_type_input() {
        return file_type_input;
    }

    /**
     * 设置输入文件类型
     *
     * @param file_type_input 输入文件类型
     */
    public void setFile_type_input(String file_type_input) {
        this.file_type_input = file_type_input;
    }

    /**
     * 获取输入文件编码
     *
     * @return encoding_input - 输入文件编码
     */
    public String getEncoding_input() {
        return encoding_input;
    }

    /**
     * 设置输入文件编码
     *
     * @param encoding_input 输入文件编码
     */
    public void setEncoding_input(String encoding_input) {
        this.encoding_input = encoding_input;
    }

    /**
     * 获取输入分割符
     *
     * @return sep_input - 输入分割符
     */
    public String getSep_input() {
        return sep_input;
    }

    /**
     * 设置输入分割符
     *
     * @param sep_input 输入分割符
     */
    public void setSep_input(String sep_input) {
        this.sep_input = sep_input;
    }

    /**
     * 获取输入是否包含表头
     *
     * @return header_input - 输入是否包含表头
     */
    public String getHeader_input() {
        return header_input;
    }

    /**
     * 设置输入是否包含表头
     *
     * @param header_input 输入是否包含表头
     */
    public void setHeader_input(String header_input) {
        this.header_input = header_input;
    }

    /**
     * 获取洗牌个数默认空
     *
     * @return repartition_num_input - 洗牌个数默认空
     */
    public String getRepartition_num_input() {
        return repartition_num_input;
    }

    /**
     * 设置洗牌个数默认空
     *
     * @param repartition_num_input 洗牌个数默认空
     */
    public void setRepartition_num_input(String repartition_num_input) {
        this.repartition_num_input = repartition_num_input;
    }

    /**
     * 获取洗牌字段默认空
     *
     * @return repartition_cols_input - 洗牌字段默认空
     */
    public String getRepartition_cols_input() {
        return repartition_cols_input;
    }

    /**
     * 设置洗牌字段默认空
     *
     * @param repartition_cols_input 洗牌字段默认空
     */
    public void setRepartition_cols_input(String repartition_cols_input) {
        this.repartition_cols_input = repartition_cols_input;
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
     * 获取表所属公司
     *
     * @return company - 表所属公司
     */
    public String getCompany() {
        return company;
    }

    /**
     * 设置表所属公司
     *
     * @param company 表所属公司
     */
    public void setCompany(String company) {
        this.company = company;
    }

    /**
     * 获取表所属部门
     *
     * @return section - 表所属部门
     */
    public String getSection() {
        return section;
    }

    /**
     * 设置表所属部门
     *
     * @param section 表所属部门
     */
    public void setSection(String section) {
        this.section = section;
    }

    /**
     * 获取表所属服务
     *
     * @return service - 表所属服务
     */
    public String getService() {
        return service;
    }

    /**
     * 设置表所属服务
     *
     * @param service 表所属服务
     */
    public void setService(String service) {
        this.service = service;
    }

    /**
     * 获取更新说明
     *
     * @return update_context - 更新说明
     */
    public String getUpdate_context() {
        return update_context;
    }

    /**
     * 设置更新说明
     *
     * @param update_context 更新说明
     */
    public void setUpdate_context(String update_context) {
        this.update_context = update_context;
    }

    /**
     * 获取输入表字段名
     *
     * @return data_sources_table_columns - 输入表字段名
     */
    public String getData_sources_table_columns() {
        return data_sources_table_columns;
    }

    /**
     * 设置输入表字段名
     *
     * @param data_sources_table_columns 输入表字段名
     */
    public void setData_sources_table_columns(String data_sources_table_columns) {
        this.data_sources_table_columns = data_sources_table_columns;
    }

    /**
     * 获取输入文件字段名
     *
     * @return data_sources_file_columns - 输入文件字段名
     */
    public String getData_sources_file_columns() {
        return data_sources_file_columns;
    }

    /**
     * 设置输入文件字段名
     *
     * @param data_sources_file_columns 输入文件字段名
     */
    public void setData_sources_file_columns(String data_sources_file_columns) {
        this.data_sources_file_columns = data_sources_file_columns;
    }

    /**
     * 获取规则配置json
     *
     * @return quality_rule_config - 规则配置json
     */
    public String getQuality_rule_config() {
        return quality_rule_config;
    }

    /**
     * 设置规则配置json
     *
     * @param quality_rule_config 规则配置json
     */
    public void setQuality_rule_config(String quality_rule_config) {
        this.quality_rule_config = quality_rule_config;
    }

    public List<Map<String, Object>> getQuality_rule_list() {
        if(StringUtils.isEmpty(getQuality_rule_config())){
            return new ArrayList<>();
        }

        return JsonUtil.toJavaListMap(getQuality_rule_config());
    }

    public String getProduct_code() {
        return product_code;
    }

    public void setProduct_code(String product_code) {
        this.product_code = product_code;
    }

    public String getDim_group() {
        return dim_group;
    }

    public void setDim_group(String dim_group) {
        this.dim_group = dim_group;
    }

    public String getIs_delete() {
        return is_delete;
    }

    public void setIs_delete(String is_delete) {
        this.is_delete = is_delete;
    }
}