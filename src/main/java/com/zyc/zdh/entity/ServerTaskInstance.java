package com.zyc.zdh.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.hadoop.yarn.util.Times;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;
import java.util.Date;

@Table(name = "server_task_instance")
public class ServerTaskInstance {
    @Id
    private String id;

    @Column(name = "templete_id")
    private String templete_id;

    /**
     * 构建任务说明
     */
    @Column(name = "build_task")
    private String build_task;

    /**
     * 构建服务器
     */
    @Column(name = "build_ip")
    private String build_ip;

    private String build_username;

    private String build_privatekey;

    private String build_path;

    /**
     * git地址
     */
    @Column(name = "git_url")
    private String git_url;

    /**
     * 构建工具类型,GRADLE/MAVEN
     */
    @Column(name = "build_type")
    private String build_type;

    /**
     * 部署服务器
     */
    @Column(name = "remote_ip")
    private String remote_ip;

    /**
     * 部署路径
     */
    @Column(name = "remote_path")
    private String remote_path;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Timestamp create_time;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Timestamp update_time;

    /**
     * 拥有者
     */
    private String owner;

    /**
     * 构建命令
     */
    @Column(name = "build_command")
    private String build_command;

    @Column(name = "version_type")
    private String version_type;//branch/tag

    private String version;

    @Column(name = "build_branch")
    private String build_branch;

    private String status;//0进行中,1成功,2失败

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBuild_task() {
        return build_task;
    }

    public void setBuild_task(String build_task) {
        this.build_task = build_task;
    }

    public String getBuild_ip() {
        return build_ip;
    }

    public void setBuild_ip(String build_ip) {
        this.build_ip = build_ip;
    }

    public String getGit_url() {
        return git_url;
    }

    public void setGit_url(String git_url) {
        this.git_url = git_url;
    }

    public String getBuild_type() {
        return build_type;
    }

    public void setBuild_type(String build_type) {
        this.build_type = build_type;
    }

    public String getRemote_ip() {
        return remote_ip;
    }

    public void setRemote_ip(String remote_ip) {
        this.remote_ip = remote_ip;
    }

    public String getRemote_path() {
        return remote_path;
    }

    public void setRemote_path(String remote_path) {
        this.remote_path = remote_path;
    }

    public Timestamp getCreate_time() {
        return create_time;
    }

    public void setCreate_time(Timestamp create_time) {
        this.create_time = create_time;
    }

    public Timestamp getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(Timestamp update_time) {
        this.update_time = update_time;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getBuild_command() {
        return build_command;
    }

    public void setBuild_command(String build_command) {
        this.build_command = build_command;
    }

    public String getVersion_type() {
        return version_type;
    }

    public void setVersion_type(String version_type) {
        this.version_type = version_type;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getTemplete_id() {
        return templete_id;
    }

    public void setTemplete_id(String templete_id) {
        this.templete_id = templete_id;
    }

    public String getBuild_branch() {
        return build_branch;
    }

    public void setBuild_branch(String build_branch) {
        this.build_branch = build_branch;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getBuild_username() {
        return build_username;
    }

    public void setBuild_username(String build_username) {
        this.build_username = build_username;
    }

    public String getBuild_privatekey() {
        return build_privatekey;
    }

    public void setBuild_privatekey(String build_privatekey) {
        this.build_privatekey = build_privatekey;
    }

    public String getBuild_path() {
        return build_path;
    }

    public void setBuild_path(String build_path) {
        this.build_path = build_path;
    }
}