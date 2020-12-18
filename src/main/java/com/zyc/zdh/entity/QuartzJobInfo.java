package com.zyc.zdh.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;


@Table
public class QuartzJobInfo implements Serializable {

    @Id
    @Column
    private String job_id;//任务id,
    private String job_context;//任务说明
    private String more_task;//多源任务 值：多源ETL,单源ETL,SQL
    private String job_type;// 任务类型,SHELL,FTP,CLASS
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Timestamp start_time;// 起始时间
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Timestamp end_time;// 结束时间
    private String step_size;
    private String job_model;// 执行模式(顺时间执行1，执行一次2，重复执行3),
    private String plan_count;//计划执行次数
    private long count=0;//执行次数
    private String command;// command,
    private String params;// 参数,
    private String last_status;// 上次任务是否执行完必,dispatch,finish,etl,error,wait_retry,retry用来记录异步采集程序的状态
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Timestamp last_time;// 上次任务执行时间,
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Timestamp next_time;// 下次任务执行时间,
    private String expr;// quartz 表达式
    private String status;// 任务状态,create,running,pause,finish,remove,error
    private String ip;//服务器地址,
    private String user;//用户名,
    private String password;//密码
    private String etl_task_id;
    private String etl_context;
    private String owner;
    private String is_script;
    private String job_ids;
    private String jump_dep;
    private String jump_script;
    private String interval_time;
    private String alarm_enabled;
    private String email_and_sms;
    private String alarm_account;
    private String task_log_id;
    private String time_out="86400";
    private String priority="5";//优先级
    private Timestamp quartz_time;
    private String use_quartz_time="off";//是否使用quartz触发时间,on,off,null
    private String time_diff;//
    private String jsmind_data;//json 形式,作业直接的关系

    public String getJsmind_data() {
        return jsmind_data;
    }

    public void setJsmind_data(String jsmind_data) {
        this.jsmind_data = jsmind_data;
    }

    public String getTime_diff() {
        return time_diff;
    }

    public void setTime_diff(String time_diff) {
        this.time_diff = time_diff;
    }

    public Timestamp getQuartz_time() {
        return quartz_time;
    }

    public void setQuartz_time(Timestamp quartz_time) {
        this.quartz_time = quartz_time;
    }

    public String getUse_quartz_time() {
        return use_quartz_time;
    }

    public void setUse_quartz_time(String use_quartz_time) {
        this.use_quartz_time = use_quartz_time;
    }

    public String getPriority() {
        return priority.equals("")?"5":priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public String getEtl_context() {
        return etl_context;
    }

    public void setEtl_context(String etl_context) {
        this.etl_context = etl_context;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }


    public String getPlan_count() {
        return plan_count;
    }

    public void setPlan_count(String plan_count) {
        this.plan_count = plan_count;
    }

    public String getJob_context() {
        return job_context;
    }

    public void setJob_context(String job_context) {
        this.job_context = job_context;
    }

    public String getJob_id() {
        return job_id;
    }

    public void setJob_id(String job_id) {
        this.job_id = job_id;
    }

    public String getJob_type() {
        return job_type;
    }

    public void setJob_type(String job_type) {
        this.job_type = job_type;
    }

    public Timestamp getStart_time() {
        return start_time;
    }

    public void setStart_time(Timestamp start_time) {
        this.start_time = start_time;
    }

    public Timestamp getEnd_time() {
        return end_time;
    }

    public void setEnd_time(Timestamp end_time) {
        this.end_time = end_time;
    }

    public String getStep_size() {
        return step_size;
    }

    public void setStep_size(String step_size) {
        this.step_size = step_size;
    }

    public String getJob_model() {
        return job_model;
    }

    public void setJob_model(String job_model) {
        this.job_model = job_model;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public String getLast_status() {
        return last_status;
    }

    public void setLast_status(String last_status) {
        this.last_status = last_status;
    }

    public Timestamp getLast_time() {
        return last_time;
    }

    public void setLast_time(Timestamp last_time) {
        this.last_time = last_time;
    }

    public Timestamp getNext_time() {
        return next_time;
    }

    public void setNext_time(Timestamp next_time) {
        this.next_time = next_time;
    }

    public String getExpr() {
        return expr;
    }

    public void setExpr(String expr) {
        this.expr = expr;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEtl_task_id() {
        return etl_task_id;
    }

    public void setEtl_task_id(String etl_task_id) {
        this.etl_task_id = etl_task_id;
    }

    public String getMore_task() {
        return more_task;
    }

    public void setMore_task(String more_task) {
        this.more_task = more_task;
    }

    public String getIs_script() {
        return is_script;
    }

    public void setIs_script(String is_script) {
        this.is_script = is_script;
    }

    public String getJob_ids() {
        return job_ids;
    }

    public void setJob_ids(String job_ids) {
        this.job_ids = job_ids;
    }

    public String getJump_dep() {
        return jump_dep;
    }

    public void setJump_dep(String jump_dep) {
        this.jump_dep = jump_dep;
    }

    public String getJump_script() {
        return jump_script;
    }

    public void setJump_script(String jump_script) {
        this.jump_script = jump_script;
    }

    public String getInterval_time() {
        return interval_time;
    }

    public void setInterval_time(String interval_time) {
        this.interval_time = interval_time;
    }

    public String getAlarm_enabled() {
        return alarm_enabled;
    }

    public void setAlarm_enabled(String alarm_enabled) {
        this.alarm_enabled = alarm_enabled;
    }

    public String getEmail_and_sms() {
        return email_and_sms;
    }

    public void setEmail_and_sms(String email_and_sms) {
        this.email_and_sms = email_and_sms;
    }

    public String getAlarm_account() {
        return alarm_account;
    }

    public void setAlarm_account(String alarm_account) {
        this.alarm_account = alarm_account;
    }

    public String getTask_log_id() {
        return task_log_id;
    }

    public void setTask_log_id(String task_log_id) {
        this.task_log_id = task_log_id;
    }

    public String getTime_out() {
        return time_out;
    }

    public void setTime_out(String time_out) {
        this.time_out = time_out;
    }
}
