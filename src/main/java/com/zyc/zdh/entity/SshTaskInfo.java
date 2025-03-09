package com.zyc.zdh.entity;

import com.zyc.zdh.util.Const;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;

@Table
public class SshTaskInfo extends BaseProductAndDimGroupAuthInfo{

    /**
     * 主键ID
     */
    @Id
    @Column
    private String id ;
    /**
     * ssh说明
     */
    private String ssh_context ;
    /**
     * 参数
     */
    private String ssh_params_input ;
    /**
     * 命令
     */
    private String ssh_cmd ;
    /**
     * 执行路径
     */
    private String ssh_script_path;
    /**
     * 脚本内容
     */
    private String ssh_script_context;
    /**
     * host
     */
    private String host ;
    /**
     * port
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

    public String getSsh_context() {
        return ssh_context;
    }

    public void setSsh_context(String ssh_context) {
        this.ssh_context = ssh_context;
    }

    public String getSsh_params_input() {
        return ssh_params_input;
    }

    public void setSsh_params_input(String ssh_params_input) {
        this.ssh_params_input = ssh_params_input;
    }

    public String getSsh_cmd() {
        return ssh_cmd;
    }

    public void setSsh_cmd(String ssh_cmd) {
        this.ssh_cmd = ssh_cmd;
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


    public String getSsh_script_path() {
        return ssh_script_path;
    }

    public void setSsh_script_path(String ssh_script_path) {
        this.ssh_script_path = ssh_script_path;
    }

    public String getSsh_script_context() {
        return ssh_script_context;
    }

    public void setSsh_script_context(String ssh_script_context) {
        this.ssh_script_context = ssh_script_context;
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
