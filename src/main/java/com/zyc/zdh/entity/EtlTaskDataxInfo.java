package com.zyc.zdh.entity;

import com.zyc.zdh.util.Const;

import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;

@Table(name = "etl_task_datax_info")
public class EtlTaskDataxInfo {
    @Id
    private String id;

    /**
     * 任务说明
     */
    private String datax_context;

    /**
     * 输入数据源id
     */
    private String data_sources_choose_input;

    /**
     * 输入数据源类型
     */
    private String data_source_type_input;

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

    private String datax_json;

    private Timestamp update_time;
    private String is_delete= Const.NOT_DELETE;

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
     * @return datax_context - 任务说明
     */
    public String getDatax_context() {
        return datax_context;
    }

    /**
     * 设置任务说明
     *
     * @param datax_context 任务说明
     */
    public void setDatax_context(String datax_context) {
        this.datax_context = datax_context;
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
     * @return datax_json
     */
    public String getDatax_json() {
        return datax_json;
    }

    /**
     * @param datax_json
     */
    public void setDatax_json(String datax_json) {
        this.datax_json = datax_json;
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