package com.zyc.zdh.entity;

import com.zyc.zdh.util.Const;

import javax.persistence.Column;
import javax.persistence.Id;
import java.sql.Timestamp;

public class EmailTaskLogs {
    @Id
    @Column
    private String id;//唯一标识
    private String job_id;//任务id,
    private String job_context;//任务说明
    private String group_id;
    private String group_context;
    private String etl_date;// 起始时间
    private String status;// 任务状态
    private Timestamp start_time;//任务开始时间
    private Timestamp update_time;
    private String  owner;
    private String is_notice=Const.FALSE;
    private String process="1";
    private String alarm_email= Const.OFF;
    private String alarm_sms=Const.OFF;
    private String alarm_zdh=Const.OFF;

    private String notice_error=Const.OFF;
    private String notice_finish=Const.OFF;
    private String notice_timeout=Const.OFF;


    private String userName;
    private String email;
    private String phone;
    private String is_use_email="";
    private String is_use_phone="";
    private String etl_context;
    private String etl_task_id;



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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getIs_use_email() {
        return is_use_email;
    }

    public void setIs_use_email(String is_use_email) {
        this.is_use_email = is_use_email;
    }

    public String getIs_use_phone() {
        return is_use_phone;
    }

    public void setIs_use_phone(String is_use_phone) {
        this.is_use_phone = is_use_phone;
    }

    public String getGroup_id() {
        return group_id;
    }

    public void setGroup_id(String group_id) {
        this.group_id = group_id;
    }

    public String getGroup_context() {
        return group_context;
    }

    public void setGroup_context(String group_context) {
        this.group_context = group_context;
    }

    public String getEtl_context() {
        return etl_context;
    }

    public void setEtl_context(String etl_context) {
        this.etl_context = etl_context;
    }

    public String getEtl_task_id() {
        return etl_task_id;
    }

    public void setEtl_task_id(String etl_task_id) {
        this.etl_task_id = etl_task_id;
    }

    public String getAlarm_email() {
        return alarm_email;
    }

    public void setAlarm_email(String alarm_email) {
        this.alarm_email = alarm_email;
    }

    public String getAlarm_sms() {
        return alarm_sms;
    }

    public void setAlarm_sms(String alarm_sms) {
        this.alarm_sms = alarm_sms;
    }

    public String getAlarm_zdh() {
        return alarm_zdh;
    }

    public void setAlarm_zdh(String alarm_zdh) {
        this.alarm_zdh = alarm_zdh;
    }

    public String getNotice_error() {
        return notice_error;
    }

    public void setNotice_error(String notice_error) {
        this.notice_error = notice_error;
    }

    public String getNotice_finish() {
        return notice_finish;
    }

    public void setNotice_finish(String notice_finish) {
        this.notice_finish = notice_finish;
    }

    public String getNotice_timeout() {
        return notice_timeout;
    }

    public void setNotice_timeout(String notice_timeout) {
        this.notice_timeout = notice_timeout;
    }
}
