package com.zyc.zdh.entity;

import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;

@Table(name = "etl_task_unstructure_info")
public class EtlTaskUnstructureInfo extends BaseProductAndDimGroupAuthInfo{
    @Id
    private String id;

    /**
     * 任务说明
     */
    private String unstructure_context;

    private String project_id;

    /**
     * 输入数据源
     */
    private String data_sources_choose_file_input;

    /**
     * 文件读取路径
     */
    private String input_path;

    private String data_sources_choose_file_output;

    private String data_sources_choose_jdbc_output;

    /**
     * 文件写入路径
     */
    private String output_path;

    private String unstructure_params_output;

    /**
     * 拥有者
     */
    private String owner;

    /**
     * 创建时间
     */
    private Timestamp create_time;

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
     * 更新时间
     */
    private Timestamp update_time;

    /**
     * 是否删除,0:未删除,1:删除
     */
    private String is_delete;

    private String etl_sql;

    /**
     * 归属组
     */
    private String dim_group;

    /**
     * 归属产品
     */
    private String product_code;

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
     * @return unstructure_context - 任务说明
     */
    public String getUnstructure_context() {
        return unstructure_context;
    }

    /**
     * 设置任务说明
     *
     * @param unstructure_context 任务说明
     */
    public void setUnstructure_context(String unstructure_context) {
        this.unstructure_context = unstructure_context;
    }

    /**
     * @return project_id
     */
    public String getProject_id() {
        return project_id;
    }

    /**
     * @param project_id
     */
    public void setProject_id(String project_id) {
        this.project_id = project_id;
    }

    /**
     * 获取输入数据源
     *
     * @return data_sources_choose_file_input - 输入数据源
     */
    public String getData_sources_choose_file_input() {
        return data_sources_choose_file_input;
    }

    /**
     * 设置输入数据源
     *
     * @param data_sources_choose_file_input 输入数据源
     */
    public void setData_sources_choose_file_input(String data_sources_choose_file_input) {
        this.data_sources_choose_file_input = data_sources_choose_file_input;
    }

    /**
     * 获取文件读取路径
     *
     * @return input_path - 文件读取路径
     */
    public String getInput_path() {
        return input_path;
    }

    /**
     * 设置文件读取路径
     *
     * @param input_path 文件读取路径
     */
    public void setInput_path(String input_path) {
        this.input_path = input_path;
    }

    /**
     * @return data_sources_choose_file_output
     */
    public String getData_sources_choose_file_output() {
        return data_sources_choose_file_output;
    }

    /**
     * @param data_sources_choose_file_output
     */
    public void setData_sources_choose_file_output(String data_sources_choose_file_output) {
        this.data_sources_choose_file_output = data_sources_choose_file_output;
    }

    /**
     * @return data_sources_choose_jdbc_output
     */
    public String getData_sources_choose_jdbc_output() {
        return data_sources_choose_jdbc_output;
    }

    /**
     * @param data_sources_choose_jdbc_output
     */
    public void setData_sources_choose_jdbc_output(String data_sources_choose_jdbc_output) {
        this.data_sources_choose_jdbc_output = data_sources_choose_jdbc_output;
    }

    /**
     * 获取文件写入路径
     *
     * @return output_path - 文件写入路径
     */
    public String getOutput_path() {
        return output_path;
    }

    /**
     * 设置文件写入路径
     *
     * @param output_path 文件写入路径
     */
    public void setOutput_path(String output_path) {
        this.output_path = output_path;
    }

    /**
     * @return unstructure_params_output
     */
    public String getUnstructure_params_output() {
        return unstructure_params_output;
    }

    /**
     * @param unstructure_params_output
     */
    public void setUnstructure_params_output(String unstructure_params_output) {
        this.unstructure_params_output = unstructure_params_output;
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
     * @return etl_sql
     */
    public String getEtl_sql() {
        return etl_sql;
    }

    /**
     * @param etl_sql
     */
    public void setEtl_sql(String etl_sql) {
        this.etl_sql = etl_sql;
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