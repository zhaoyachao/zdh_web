package com.zyc.zdh.entity;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.sql.Timestamp;

@Table
public class ApprovalAuditorInfo {

    @Id
    @Column
    private String id;

    /**
     * 产品代码
     */
    private String product_code;//所属产品code
    /**
     * 审批code
     */
    private String code;//'审批流程code'
    /**
     * 审批环节
     */
    private String level;
    /**
     * 审批人账号
     */
    private String auditor_id;//'审批人账号',
    /**
     * 审批人用户组
     */
    private String auditor_group;//'审批人所在组',
    /**
     * 创建时间
     */
    private Timestamp create_time;//'创建时间',
    /**
     * 审批说明
     */
    private String auditor_context;//环节说明

    private String auditor_rule;

    @Transient
    private String code_name;

    @Transient
    private String level_name;

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

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getAuditor_id() {
        return auditor_id;
    }

    public void setAuditor_id(String auditor_id) {
        this.auditor_id = auditor_id;
    }

    public String getAuditor_group() {
        return auditor_group;
    }

    public void setAuditor_group(String auditor_group) {
        this.auditor_group = auditor_group;
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

    public String getLevel_name() {
        return "第"+getLevel()+"环节";
    }

    public String getProduct_code() {
        return product_code;
    }

    public void setProduct_code(String product_code) {
        this.product_code = product_code;
    }

    public String getAuditor_context() {
        return auditor_context;
    }

    public void setAuditor_context(String auditor_context) {
        this.auditor_context = auditor_context;
    }

    public String getAuditor_rule() {
        return auditor_rule;
    }

    public void setAuditor_rule(String auditor_rule) {
        this.auditor_rule = auditor_rule;
    }
}
