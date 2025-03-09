package com.zyc.zdh.entity;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.zyc.zdh.job.JobStatus;
import com.zyc.zdh.util.JsonUtil;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;
import java.sql.Timestamp;


@Table
public class TaskGroupLogInstance extends BaseProductAndDimGroupAuthInfo implements Serializable {

    @Id
    @Column
    private String id;//唯一标识
    private String job_id;//任务id,
    private String job_context;//任务说明
    private String etl_date;// 起始时间
    private String status;// 任务状态,dispatch,check_dep,wait_retry,finish,error,etl,kill,killed
    private Timestamp run_time;//任务开始时间
    private Timestamp update_time;
    private String  owner;
    private String is_notice="false";
    private String process="1";//默认是1,开始调度是5,调整调度时间etl_date是6,检查调度依赖7,检查调度次数是8,调度执行的任务命令失败是9,完成拼接信息是10,发送成功/失败是15/17,超过20表示在server端执行
    @Transient
    private String process_msg="未开始";
    private String thread_id;//myid+threadId+id,通过'_'连接
    private Timestamp retry_time=Timestamp.valueOf("2000-01-01 00:00:00");
    private String executor;
    private String etl_info;
    private String url;
    private String application_id;//数据采集执行器标识
    private String history_server;
    private String master;//example: local,yarn,spark://ip:port
    private String server_ack="0";//server端ack 保证任务故障转移时不会重复发送

    private String server_id;//调度器标识--发生服务重启时用来进行任务恢复

    public String getServer_id() {
        return server_id;
    }

    public void setServer_id(String server_id) {
        this.server_id = server_id;
    }

    public String getIs_retryed() {
        return is_retryed;
    }

    public void setIs_retryed(String is_retryed) {
        this.is_retryed = is_retryed;
    }

    private String is_retryed="0";

    private String concurrency="0";//0串行,1并行(不检查状态)

    private String last_task_log_id;

    private String more_task;//多源任务 值：MORE_ETL,ETL,SQL
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
    private Timestamp cur_time;// 当前任务触发时间,

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Timestamp last_time;// 上次任务执行时间,
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Timestamp next_time;// 下次任务执行时间,
    private String expr;// quartz 表达式

    private String ip;//服务器地址,
    private String user;//用户名,
    private String password;//密码
    private String etl_task_id;
    private String etl_context;

    private String is_script;
    private String job_ids;
    private String jump_dep;
    private String jump_script;
    private String interval_time;
    private String alarm_enabled;//on 表示启用
    private String email_and_sms;
    private String alarm_account;

    private String time_out="86400";//超时时间 以秒为单位,默认一天

    private String process_time= JsonUtil.formatJsonString(new process_time_info());//json格式,只会记录每个流程的结束时间

    private String priority="5";//优先级
    private Timestamp quartz_time;//调度时间
    private String use_quartz_time;//是否使用quartz触发时间
    private String time_diff;
    private String jsmind_data;//json 形式,作业直接的关系
    private String run_jsmind_data;//json 形式实例作业依赖关系

    private String next_tasks;//逗号分隔
    private String pre_tasks;//逗号分隔

    private String schedule_source;//调度来源,1:例行,2:手动

    private String alarm_email="off";
    private String alarm_sms="off";
    private String alarm_zdh="off";

    private String notice_error="off";
    private String notice_finish="off";
    private String notice_timeout="off";

    /**
     * 归属组
     */
    private String dim_group;

    /**
     * 归属产品
     */
    private String product_code;

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

    public String getNext_tasks() {
        return next_tasks;
    }

    public void setNext_tasks(String next_tasks) {
        this.next_tasks = next_tasks;
    }

    public String getPre_tasks() {
        return pre_tasks;
    }

    public void setPre_tasks(String pre_tasks) {
        this.pre_tasks = pre_tasks;
    }

    public void setRun_jsmind_data(String run_jsmind_data) {
        this.run_jsmind_data = run_jsmind_data;
    }

    public String getJsmind_data() {
        return jsmind_data;
    }
    public String getRun_jsmind_data() {
        return run_jsmind_data;
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

    public String getUse_quartz_time() {
        return use_quartz_time;
    }

    public void setUse_quartz_time(String use_quartz_time) {
        this.use_quartz_time = use_quartz_time;
    }

    public Timestamp getQuartTime() {
        return quartz_time;
    }
    public void setQuartz_time(Timestamp quartz_time) {
        this.quartz_time = quartz_time;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }


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

    public Timestamp getRun_time() {
        return run_time;
    }

    public void setRun_time(Timestamp run_time) {
        this.run_time = run_time;
    }

    public String getMore_task() {
        return more_task;
    }

    public void setMore_task(String more_task) {
        this.more_task = more_task;
    }

    public String getJob_type() {
        return job_type;
    }

    public void setJob_type(String job_type) {
        this.job_type = job_type;
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

    public String getPlan_count() {
        return plan_count;
    }

    public void setPlan_count(String plan_count) {
        this.plan_count = plan_count;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
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

    public String getEtl_context() {
        return etl_context;
    }

    public void setEtl_context(String etl_context) {
        this.etl_context = etl_context;
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

    public String getConcurrency() {
//        if(use_quartz_time.equalsIgnoreCase("on")){
//            return "1";//并行
//        }
        return concurrency;
    }

    public void setConcurrency(String concurrency) {
        this.concurrency = concurrency;
    }

    public String getLast_task_log_id() {
        return last_task_log_id;
    }

    public void setLast_task_log_id(String last_task_log_id) {
        this.last_task_log_id = last_task_log_id;
    }

    public Timestamp getCur_time() {
        return cur_time;
    }

    public void setCur_time(Timestamp cur_time) {
        this.cur_time = cur_time;
    }

    public String getTime_out() {
        return time_out;
    }

    public void setTime_out(String time_out) {
        this.time_out = time_out;
    }

    public String getProcess_time() {
        return process_time;
    }

    public process_time_info getProcess_time2() {
        return JSON.parseObject(process_time,process_time_info.class);
    }

    public void setProcess_time(String process_time) {
        this.process_time = process_time;
    }

    public void setProcess_time(process_time_info process_time_info) {
        this.process_time = JsonUtil.formatJsonString(process_time_info);
    }

    public String getSchedule_source() {
        return schedule_source;
    }

    public void setSchedule_source(String schedule_source) {
        this.schedule_source = schedule_source;
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

    public String getProcess_msg() {
        //默认是1,开始调度是5,调整调度时间etl_date是7,检查调度次数是8,调度执行的任务命令失败是9,完成拼接信息是10,发送成功/失败是15/17,超过20表示在server端执行

        if(status.equalsIgnoreCase(JobStatus.ERROR.getValue())){
            return "采集失败";
        }else if(status.equalsIgnoreCase(JobStatus.KILLED.getValue())){
            return "已杀死";
        }
         switch (getProcess()){
             case "1":
                 return "未开始";
             case "5":
                 return "开始调度";
             case "6":
                 return "开始调整组任务调度时间";
             case "6.5":
                 return "完成调整组任务调度时间";
             case "7":
                 return "开始检查组任务依赖任务";
             case "7.5":
                 return "完成检查组任务依赖任务";
             case "8":
                 return "检查调度次数";
             case "9":
                 return "执行调度脚本";
             case "10":
                 return "组装ETL信息";
             case "15":
             case "17":
                 return "连接server";
             case "100":
                 return "采集完成";
             default:
                 return "server";

         }
    }
}
