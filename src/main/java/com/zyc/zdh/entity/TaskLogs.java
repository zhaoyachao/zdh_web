package com.zyc.zdh.entity;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;


@Table
public class TaskLogs implements Serializable {

    @Id
    @Column
    private String id;//唯一标识
    private String job_id;//任务id,
    private String job_context;//任务说明
    private String etl_date;// 起始时间
    private String status;// 任务状态,dispatch,wait_retry,finish,error,etl,kill
    private Timestamp start_time;//任务开始时间
    private Timestamp update_time;
    private String  owner;
    private String is_notice="false";
    private String process="1";//默认是1,开始调度是5,调整调度时间etl_date是7,检查调度次数是8,调度执行的任务命令失败是9,完成拼接信息是10,发送成功/失败是15/17,超过20表示在server端执行
    private String thread_id;//myid+threadId+id,通过'_'连接
    private Timestamp retry_time;
    private String executor;
    private String etl_info;
    private String url;
    private String application_id;
    private String history_server;
    private String master;
    private String server_ack="0";//server端ack 保证任务故障转移时不会重复发送

    public String getJob_id() {
        return job_id;
    }

    public void setJob_id(String job_id) {
        this.job_id = job_id;
    }

    public String getJob_context() {
        return job_context;
    }

    public void setJob_context(String job_context) {
        this.job_context = job_context;
    }

    public String getEtl_date() {
        return etl_date;
    }

    public void setEtl_date(String etl_date) {
        this.etl_date = etl_date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public Timestamp getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(Timestamp update_time) {
        this.update_time = update_time;
    }

    public String getIs_notice() {
        return is_notice;
    }

    public void setIs_notice(String is_notice) {
        this.is_notice = is_notice;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProcess() {
        return process;
    }

    public void setProcess(String process) {
        this.process = process;
    }

    public Timestamp getStart_time() {
        return start_time;
    }

    public void setStart_time(Timestamp start_time) {
        this.start_time = start_time;
    }

    public String getThread_id() {
        return thread_id;
    }

    public void setThread_id(String thread_id) {
        this.thread_id = thread_id;
    }

    public Timestamp getRetry_time() {
        return retry_time;
    }

    public void setRetry_time(Timestamp retry_time) {
        this.retry_time = retry_time;
    }

    public String getExecutor() {
        return executor;
    }

    public void setExecutor(String executor) {
        this.executor = executor;
    }

    public String getEtl_info() {
        return etl_info;
    }

    public void setEtl_info(String etl_info) {
        this.etl_info = etl_info;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getApplication_id() {
        return application_id;
    }

    public void setApplication_id(String application_id) {
        this.application_id = application_id;
    }

    public String getHistory_server() {
        return history_server;
    }

    public void setHistory_server(String history_server) {
        this.history_server = history_server;
    }

    public String getMaster() {
        return master;
    }

    public void setMaster(String master) {
        this.master = master;
    }

    public String getServer_ack() {
        return server_ack;
    }

    public void setServer_ack(String server_ack) {
        this.server_ack = server_ack;
    }

}
