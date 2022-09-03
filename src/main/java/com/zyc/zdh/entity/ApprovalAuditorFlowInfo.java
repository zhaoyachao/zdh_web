package com.zyc.zdh.entity;

import java.sql.Timestamp;
import javax.persistence.*;

@Table(name = "approval_auditor_flow_info")
public class ApprovalAuditorFlowInfo {
    @Id
    private String id;

    /**
     * 审批流说明
     */
    private String flow_context;

    /**
     * 审批流code
     */
    private String flow_code;

    /**
     * 产品代码
     */
    private String product_code;

    /**
     * 账号
     */
    private String owner;

    /**
     * 是否删除,0:未删除,1:删除
     */
    private String is_delete;

    /**
     * 创建时间
     */
    private Timestamp create_time;

    /**
     * 更新时间
     */
    private Timestamp update_time;

    /**
     * 审批关系
     */
    private String jsmind_data;

    /**
     * @return id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * 获取审批流说明
     *
     * @return flow_context - 审批流说明
     */
    public String getFlow_context() {
        return flow_context;
    }

    /**
     * 设置审批流说明
     *
     * @param flow_context 审批流说明
     */
    public void setFlow_context(String flow_context) {
        this.flow_context = flow_context;
    }

    /**
     * 获取审批流code
     *
     * @return flow_code - 审批流code
     */
    public String getFlow_code() {
        return flow_code;
    }

    /**
     * 设置审批流code
     *
     * @param flow_code 审批流code
     */
    public void setFlow_code(String flow_code) {
        this.flow_code = flow_code;
    }

    /**
     * 获取产品代码
     *
     * @return product_code - 产品代码
     */
    public String getProduct_code() {
        return product_code;
    }

    /**
     * 设置产品代码
     *
     * @param product_code 产品代码
     */
    public void setProduct_code(String product_code) {
        this.product_code = product_code;
    }

    /**
     * 获取账号
     *
     * @return owner - 账号
     */
    public String getOwner() {
        return owner;
    }

    /**
     * 设置账号
     *
     * @param owner 账号
     */
    public void setOwner(String owner) {
        this.owner = owner;
    }

    /**
     * 获取是否删除,0:未删除,1:删除
     *
     * @return is_delete - 是否删除,0:未删除,1:删除
     */
    public String getIs_delete() {
        return is_delete;
    }

    /**
     * 设置是否删除,0:未删除,1:删除
     *
     * @param is_delete 是否删除,0:未删除,1:删除
     */
    public void setIs_delete(String is_delete) {
        this.is_delete = is_delete;
    }

    /**
     * 获取创建时间
     *
     * @return create_time - 创建时间
     */
    public Timestamp getCreate_time() {
        return create_time;
    }

    /**
     * 设置创建时间
     *
     * @param create_time 创建时间
     */
    public void setCreate_time(Timestamp create_time) {
        this.create_time = create_time;
    }

    /**
     * 获取更新时间
     *
     * @return update_time - 更新时间
     */
    public Timestamp getUpdate_time() {
        return update_time;
    }

    /**
     * 设置更新时间
     *
     * @param update_time 更新时间
     */
    public void setUpdate_time(Timestamp update_time) {
        this.update_time = update_time;
    }

    /**
     * 获取审批关系
     *
     * @return jsmind_data - 审批关系
     */
    public String getJsmind_data() {
        return jsmind_data;
    }

    /**
     * 设置审批关系
     *
     * @param jsmind_data 审批关系
     */
    public void setJsmind_data(String jsmind_data) {
        this.jsmind_data = jsmind_data;
    }
}