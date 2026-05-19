package com.zyc.zdh.entity;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;

@Table
public class ApplyInfo extends BaseProductAndDimGroupAuthInfo{
    //申请id
    @Id
    @Column
    private String id;

    //审批单说明
    private String apply_context;

    //发布的数据信息id
    private String issue_id;

    //审批人id
    private String approve_id;

    //状态
    private String status;

    private Timestamp create_time;

    private Timestamp update_time;

    private String owner;

    private String reason;

    private String is_notice="false";

    /**
     * 归属组
     */
    private String dim_group;

    /**
     * 归属产品
     */
    private String product_code;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getApply_context() {
        return apply_context;
    }

    public void setApply_context(String apply_context) {
        this.apply_context = apply_context;
    }

    public String getIssue_id() {
        return issue_id;
    }

    public void setIssue_id(String issue_id) {
        this.issue_id = issue_id;
    }

    public String getApprove_id() {
        return approve_id;
    }

    public void setApprove_id(String approve_id) {
        this.approve_id = approve_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Timestamp getCreate_time() {
        return create_time;
    }

    public void setCreate_time(Timestamp create_time) {
        this.create_time = create_time;
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

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getIs_notice() {
        return is_notice;
    }

    public void setIs_notice(String is_notice) {
        this.is_notice = is_notice;
    }

    @Override
    public String getDim_group() {
        return dim_group;
    }

    @Override
    public void setDim_group(String dim_group) {
        this.dim_group = dim_group;
    }

    @Override
    public String getProduct_code() {
        return product_code;
    }

    @Override
    public void setProduct_code(String product_code) {
        this.product_code = product_code;
    }
}
