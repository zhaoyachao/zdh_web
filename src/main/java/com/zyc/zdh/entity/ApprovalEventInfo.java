package com.zyc.zdh.entity;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.sql.Timestamp;

@Table
public class ApprovalEventInfo {

    @Id
    @Column
    private String id;

    private String code;//'审批流程code'
    private String event_code;//事件code,
    private String event_context;//事件说明,
    private Timestamp create_time;//'创建时间',

    @Transient
    private String code_name;

    @Transient
    private String type;

    @Transient
    private String auditor_group_name;

    @Transient
    private String user_name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getEvent_code() {
        return event_code;
    }

    public void setEvent_code(String event_code) {
        this.event_code = event_code;
    }

    public String getEvent_context() {
        return event_context;
    }

    public void setEvent_context(String event_context) {
        this.event_context = event_context;
    }

    public Timestamp getCreate_time() {
        return create_time;
    }

    public void setCreate_time(Timestamp create_time) {
        this.create_time = create_time;
    }

    public String getCode_name() {
        return code_name;
    }

    public void setCode_name(String code_name) {
        this.code_name = code_name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAuditor_group_name() {
        return auditor_group_name;
    }

    public void setAuditor_group_name(String auditor_group_name) {
        this.auditor_group_name = auditor_group_name;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }
}
