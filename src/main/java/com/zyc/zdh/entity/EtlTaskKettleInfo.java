package com.zyc.zdh.entity;

import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;

@Table(name = "etl_task_kettle_info")
public class EtlTaskKettleInfo {
    @Id
    private String id;

    /**
     * 任务说明
     */
    private String etl_context;

    /**
     * 存储库类型,可选值:db,file
     */
    private String kettle_repository_type;

    /**
     * kettle存储库账号
     */
    private String kettle_repository_user;

    /**
     * kettle存储库密码
     */
    private String kettle_repository_password;

    /**
     * 输入数据源id
     */
    private String data_sources_choose_input;

    /**
     * 输入数据源类型
     */
    private String data_source_type_input;

    /**
     * 输入数据源参数
     */
    private String data_sources_params_input;

    /**
     * 存储库
     */
    private String kettle_repository;

    /**
     * 存储库类型,可选值:db,file
     */
    private String kettle_repository_path;

    /**
     * kettle类型，可选值,job,trans
     */
    private String kettle_type;

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

    /**
     * 产品code
     */
    private String product_code;

    /**
     * 用户组
     */
    private String dim_group;

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
     * @return etl_context - 任务说明
     */
    public String getEtl_context() {
        return etl_context;
    }

    /**
     * 设置任务说明
     *
     * @param etl_context 任务说明
     */
    public void setEtl_context(String etl_context) {
        this.etl_context = etl_context;
    }

    /**
     * 获取存储库类型,可选值:db,file
     *
     * @return kettle_repository_type - 存储库类型,可选值:db,file
     */
    public String getKettle_repository_type() {
        return kettle_repository_type;
    }

    /**
     * 设置存储库类型,可选值:db,file
     *
     * @param kettle_repository_type 存储库类型,可选值:db,file
     */
    public void setKettle_repository_type(String kettle_repository_type) {
        this.kettle_repository_type = kettle_repository_type;
    }

    /**
     * 获取kettle存储库账号
     *
     * @return kettle_repository_user - kettle存储库账号
     */
    public String getKettle_repository_user() {
        return kettle_repository_user;
    }

    /**
     * 设置kettle存储库账号
     *
     * @param kettle_repository_user kettle存储库账号
     */
    public void setKettle_repository_user(String kettle_repository_user) {
        this.kettle_repository_user = kettle_repository_user;
    }

    /**
     * 获取kettle存储库密码
     *
     * @return kettle_repository_password - kettle存储库密码
     */
    public String getKettle_repository_password() {
        return kettle_repository_password;
    }

    /**
     * 设置kettle存储库密码
     *
     * @param kettle_repository_password kettle存储库密码
     */
    public void setKettle_repository_password(String kettle_repository_password) {
        this.kettle_repository_password = kettle_repository_password;
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
     * 获取存储库类型,可选值:db,file
     *
     * @return kettle_repository_path - 存储库类型,可选值:db,file
     */
    public String getKettle_repository_path() {
        return kettle_repository_path;
    }

    /**
     * 设置存储库类型,可选值:db,file
     *
     * @param kettle_repository_path 存储库类型,可选值:db,file
     */
    public void setKettle_repository_path(String kettle_repository_path) {
        this.kettle_repository_path = kettle_repository_path;
    }

    public String getKettle_repository() {
        return kettle_repository;
    }

    public void setKettle_repository(String kettle_repository) {
        this.kettle_repository = kettle_repository;
    }

    /**
     * 获取kettle类型，可选值,job,trans
     *
     * @return kettle_type - kettle类型，可选值,job,trans
     */
    public String getKettle_type() {
        return kettle_type;
    }

    /**
     * 设置kettle类型，可选值,job,trans
     *
     * @param kettle_type kettle类型，可选值,job,trans
     */
    public void setKettle_type(String kettle_type) {
        this.kettle_type = kettle_type;
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
     * 获取产品code
     *
     * @return product_code - 产品code
     */
    public String getProduct_code() {
        return product_code;
    }

    /**
     * 设置产品code
     *
     * @param product_code 产品code
     */
    public void setProduct_code(String product_code) {
        this.product_code = product_code;
    }

    /**
     * 获取用户组
     *
     * @return dim_group - 用户组
     */
    public String getDim_group() {
        return dim_group;
    }

    /**
     * 设置用户组
     *
     * @param dim_group 用户组
     */
    public void setDim_group(String dim_group) {
        this.dim_group = dim_group;
    }
}