package com.zyc.zdh.entity;

import com.zyc.zdh.util.Const;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;

@Table
public class SshTaskInfo {

    @Id
    @Column
    private String id ;
    private String ssh_context ;
    private String ssh_params_input ;
    private String ssh_cmd ;
    private String ssh_script_path;
    private String ssh_script_context;
    private String host ;
    private String port;
    private String user_name;
    private String password;
    private String owner ;
    private Timestamp create_time ;
    private String company  ;
    private String section  ;
    private String service  ;
    private String update_context ;
    private Timestamp update_time;
    private String is_delete= Const.NOT_DELETE;


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
}
