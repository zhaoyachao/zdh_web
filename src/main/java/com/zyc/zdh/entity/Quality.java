package com.zyc.zdh.entity;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.sql.Timestamp;


@Table
public class Quality implements Serializable {

    @Id
    @Column
    private String id;//唯一标识
    private String dispatch_task_id;//调度任务id,
    private String etl_task_id;//etl任务id
    private String etl_date;// 起始时间
    private String status;// 任务状态,finish,error,etl
    private Timestamp create_time;
    private String  owner;
    private String etl_context;
    private String job_context;

    private String report;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDispatch_task_id() {
        return dispatch_task_id;
    }

    public void setDispatch_task_id(String dispatch_task_id) {
        this.dispatch_task_id = dispatch_task_id;
    }

    public String getEtl_task_id() {
        return etl_task_id;
    }

    public void setEtl_task_id(String etl_task_id) {
        this.etl_task_id = etl_task_id;
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

    public Timestamp getCreate_time() {
        return create_time;
    }

    public void setCreate_time(Timestamp create_time) {
        this.create_time = create_time;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getReport() {
        return report;
    }

    public void setReport(String report) {
        this.report = report;
    }

    public String getEtl_context() {
        return etl_context;
    }

    public void setEtl_context(String etl_context) {
        this.etl_context = etl_context;
    }

    public String getJob_context() {
        return job_context;
    }

    public void setJob_context(String job_context) {
        this.job_context = job_context;
    }
}
