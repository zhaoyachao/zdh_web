package com.zyc.zdh.entity;

import com.zyc.zdh.util.Const;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;

@Table
public class EtlTaskFlinkInfo extends BaseProductAndDimGroupAuthInfo{

    /**
     * 主键ID
     */
    @Id
    @Column
    private String id ;
    /**
     * 任务说明
     */
    private String sql_context ;
    /**
     * 参数
     */
    private String data_sources_params_input ;
    /**
     * flink sql
     */
    private String etl_sql ;
    /**
     * flink 客户端host
     */
    private String host ;
    /**
     * flink 客户端port
     */
    private String port;
    /**
     * 用户名
     */
    private String user_name;
    /**
     * 密码
     */
    private String password;
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
     * 检查点path
     */
    private String checkpoint;
    /**
     * 服务器类型 windows,linux
     */
    private String server_type;//windows,linux
    /**
     * flink任务启动命令行
     */
    private String command;
    /**
     * 更新时间
     */
    private Timestamp update_time;
    /**
     * 是否删除，0:否,1:删除
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

    public String getSql_context() {
        return sql_context;
    }

    public void setSql_context(String sql_context) {
        this.sql_context = sql_context;
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


    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public String getCheckpoint() {
        return checkpoint;
    }

    public void setCheckpoint(String checkpoint) {
        this.checkpoint = checkpoint;
    }

    public String getServer_type() {
        return server_type;
    }

    public void setServer_type(String server_type) {
        this.server_type = server_type;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
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
