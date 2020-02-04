package com.zyc.zspringboot.entity;

import java.io.Serializable;
import java.sql.Timestamp;

public class ZdhLogs  extends PageBase implements Serializable {

    private String etl_task_id;

    private Timestamp log_time;

    private String msg;


    public String getEtl_task_id() {
        return etl_task_id;
    }

    public void setEtl_task_id(String etl_task_id) {
        this.etl_task_id = etl_task_id;
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
}
