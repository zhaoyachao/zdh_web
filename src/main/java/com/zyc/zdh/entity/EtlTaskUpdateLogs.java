package com.zyc.zdh.entity;

import javax.persistence.Table;
import java.sql.Timestamp;

@Table
public class EtlTaskUpdateLogs {

    private String id;

    private String update_context;

    private Timestamp update_time;

    private String owner;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUpdate_context() {
        return update_context;
    }

    public void setUpdate_context(String update_context) {
        this.update_context = update_context;
    }

    public Timestamp getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(Timestamp update_time) {
        this.update_time = update_time;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }
}
