package com.zyc.zdh.entity;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Table
public class EveryDayNotice {

    @Id
    @Column
    private String id;

    private String msg;

    private String is_delete;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getIs_delete() {
        return is_delete;
    }

    public void setIs_delete(String is_delete) {
        this.is_delete = is_delete;
    }
}
