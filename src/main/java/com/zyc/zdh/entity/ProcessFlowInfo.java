package com.zyc.zdh.entity;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.sql.Timestamp;

@Table
public class ProcessFlowInfo {
    //申请id
    @Id
    @Column
    private String id;

    /**
     * 流程ID
     */
    private String flow_id;

    /**
     * 事件code
     */
    private String event_code;

    /**
     *配置code
     */
    private String config_code;

    /**
     * 流程名称
     */
    //流程名称
    private String context;

    /**
     * 审批人账号
     */
    //审批人id
    private String auditor_id;

    /**
     * 是否可见
     */
    private String is_show;

    /**
     * 上游审批ID
     */
    private String pre_id;

    /**
     * 拥有者
     */
    private String owner;

    /**
     * 创建时间
     */
    private Timestamp create_time;

    /**
     * 状态,0:未审批,1:审批完成,2:不通过,3:撤销
     */
    private String status;

    /**
     * 是否最后一的审批0:否,1:最后一个审批节点
     */
    private String is_end; //0:否,1:最后一个审批节点

    /**
     * 审批环节
     */
    //审批节点
    private String level;

    /**
     * 外部事件ID
     */
    private String event_id;

    /**
     * 产品code
     */
    private String product_code;


    @Transient
    private String by_person_name;

    /**
     * 外部系统处理标志,0:未处理,1:已处理,2:处理失败
     */
    //外部系统使用审批流,外部事件处理标识
    private String other_handle;

    private String agent_user;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFlow_id() {
        return flow_id;
    }

    public void setFlow_id(String flow_id) {
        this.flow_id = flow_id;
    }

    public String getEvent_code() {
        return event_code;
    }

    public void setEvent_code(String event_code) {
        this.event_code = event_code;
    }

    public String getConfig_code() {
        return config_code;
    }

    public void setConfig_code(String config_code) {
        this.config_code = config_code;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public String getAuditor_id() {
        return auditor_id;
    }

    public void setAuditor_id(String auditor_id) {
        this.auditor_id = auditor_id;
    }

    public String getIs_show() {
        return is_show;
    }

    public void setIs_show(String is_show) {
        this.is_show = is_show;
    }

    public String getPre_id() {
        return pre_id;
    }

    public void setPre_id(String pre_id) {
        this.pre_id = pre_id;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public Timestamp getCreate_time() {
        return create_time;
    }

    public void setCreate_time(Timestamp create_time) {
        this.create_time = create_time;
    }

    public String getBy_person_name() {
        return by_person_name;
    }

    public void setBy_person_name(String by_person_name) {
        this.by_person_name = by_person_name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getIs_end() {
        return is_end;
    }

    public void setIs_end(String is_end) {
        this.is_end = is_end;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getEvent_id() {
        return event_id;
    }

    public void setEvent_id(String event_id) {
        this.event_id = event_id;
    }

    public String getOther_handle() {
        return other_handle;
    }

    public void setOther_handle(String other_handle) {
        this.other_handle = other_handle;
    }

    public String getAgent_user() {
        return agent_user;
    }

    public void setAgent_user(String agent_user) {
        this.agent_user = agent_user;
    }

    public String getProduct_code() {
        return product_code;
    }

    public void setProduct_code(String product_code) {
        this.product_code = product_code;
    }
}
