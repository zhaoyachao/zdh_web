package com.zyc.zdh.entity;

import java.io.Serializable;
import java.sql.Timestamp;

public class ZdhLogs  extends PageBase implements Serializable {

    private String job_id;

    private Timestamp log_time;

    private String msg;

    private String level;


    public String getJob_id() {
        return job_id;
    }

    public void setJob_id(String job_id) {
        this.job_id = job_id;
    }

    public Timestamp getLog_time() {
        return log_time;
    }

    public void setLog_time(Timestamp log_time) {
        this.log_time = log_time;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }
}
