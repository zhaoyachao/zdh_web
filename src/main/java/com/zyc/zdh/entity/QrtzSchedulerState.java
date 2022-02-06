package com.zyc.zdh.entity;

import com.zyc.zdh.util.Const;

import javax.persistence.*;

@Table(name = "QRTZ_SCHEDULER_STATE")
public class QrtzSchedulerState {
    @Id
    @Column(name = "SCHED_NAME")
    private String sched_name;

    @Id
    @Column(name = "INSTANCE_NAME")
    private String instance_name;

    @Column(name = "LAST_CHECKIN_TIME")
    private Long last_checkin_time;

    @Column(name = "CHECKIN_INTERVAL")
    private Long checkin_interval;

    @Column(name = "STATUS")
    private String status;

    @Transient
    private String executor_status;
    /**
     * @return SCHED_NAME
     */
    public String getSched_name() {
        return sched_name;
    }

    /**
     * @param sched_name
     */
    public void setSched_name(String sched_name) {
        this.sched_name = sched_name;
    }

    /**
     * @return INSTANCE_NAME
     */
    public String getInstance_name() {
        return instance_name;
    }

    /**
     * @param instance_name
     */
    public void setInstance_name(String instance_name) {
        this.instance_name = instance_name;
    }

    public Long getLast_checkin_time() {
        return last_checkin_time;
    }

    public void setLast_checkin_time(Long last_checkin_time) {
        this.last_checkin_time = last_checkin_time;
    }

    public Long getCheckin_interval() {
        return checkin_interval;
    }

    public void setCheckin_interval(Long checkin_interval) {
        this.checkin_interval = checkin_interval;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getExecutor_status() {
        return (System.currentTimeMillis()-last_checkin_time) < (2*checkin_interval) && status.equalsIgnoreCase("online")?Const.ON:Const.OFF;
    }

    public void setExecutor_status(String executor_status) {
        this.executor_status = executor_status;
    }
}