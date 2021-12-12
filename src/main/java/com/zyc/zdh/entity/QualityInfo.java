package com.zyc.zdh.entity;

import java.sql.Timestamp;
import javax.persistence.*;

@Table(name = "quality")
public class QualityInfo {
    private String id;

    /**
     * 调度任务id
     */
    private String dispatch_task_id;

    /**
     * 任务id
     */
    private String etl_task_id;

    /**
     * 数据处理日期
     */
    private String etl_date;

    /**
     * 报告状态
     */
    private String status;

    /**
     * 报告信息
     */
    private String report;

    /**
     * 创建时间
     */
    private Timestamp create_time;

    /**
     * 拥有者
     */
    private String owner;

    /**
     * 调度任务名称
     */
    private String job_context;

    /**
     * 质量任务名称
     */
    private String etl_context;

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
     * 获取调度任务id
     *
     * @return dispatch_task_id - 调度任务id
     */
    public String getDispatch_task_id() {
        return dispatch_task_id;
    }

    /**
     * 设置调度任务id
     *
     * @param dispatch_task_id 调度任务id
     */
    public void setDispatch_task_id(String dispatch_task_id) {
        this.dispatch_task_id = dispatch_task_id;
    }

    /**
     * 获取任务id
     *
     * @return etl_task_id - 任务id
     */
    public String getEtl_task_id() {
        return etl_task_id;
    }

    /**
     * 设置任务id
     *
     * @param etl_task_id 任务id
     */
    public void setEtl_task_id(String etl_task_id) {
        this.etl_task_id = etl_task_id;
    }

    /**
     * 获取数据处理日期
     *
     * @return etl_date - 数据处理日期
     */
    public String getEtl_date() {
        return etl_date;
    }

    /**
     * 设置数据处理日期
     *
     * @param etl_date 数据处理日期
     */
    public void setEtl_date(String etl_date) {
        this.etl_date = etl_date;
    }

    /**
     * 获取报告状态
     *
     * @return status - 报告状态
     */
    public String getStatus() {
        return status;
    }

    /**
     * 设置报告状态
     *
     * @param status 报告状态
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * 获取报告信息
     *
     * @return report - 报告信息
     */
    public String getReport() {
        return report;
    }

    /**
     * 设置报告信息
     *
     * @param report 报告信息
     */
    public void setReport(String report) {
        this.report = report;
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
     * 获取调度任务名称
     *
     * @return job_context - 调度任务名称
     */
    public String getJob_context() {
        return job_context;
    }

    /**
     * 设置调度任务名称
     *
     * @param job_context 调度任务名称
     */
    public void setJob_context(String job_context) {
        this.job_context = job_context;
    }

    /**
     * 获取质量任务名称
     *
     * @return etl_context - 质量任务名称
     */
    public String getEtl_context() {
        return etl_context;
    }

    /**
     * 设置质量任务名称
     *
     * @param etl_context 质量任务名称
     */
    public void setEtl_context(String etl_context) {
        this.etl_context = etl_context;
    }
}