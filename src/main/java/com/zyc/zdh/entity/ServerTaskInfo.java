package com.zyc.zdh.entity;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "server_task_info")
public class ServerTaskInfo {
    @Id
    @Column
    private String id;

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

    @Column(name = "create_time")
    private Date create_time;

    @Column(name = "update_time")
    private Date update_time;

    /**
     * 拥有者
     */
    private String owner;

    /**
     * 构建命令
     */
    @Column(name = "build_command")
    private String build_command;

    @Column(name = "build_branch")
    private String build_branch="master";


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

    public Date getCreate_time() {
        return create_time;
    }

    public void setCreate_time(Date create_time) {
        this.create_time = create_time;
    }

    public Date getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(Date update_time) {
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

    public String getBuild_branch() {
        return build_branch;
    }

    public void setBuild_branch(String build_branch) {
        this.build_branch = build_branch;
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