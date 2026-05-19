package com.zyc.zdh.entity;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;

@Table
public class ApprovalConfigInfo extends BaseProductAuthInfo{
    //申请id
    @Id
    @Column
    private String id;
    /**
     * 审批流程code
     */
    private String code;
    /**
     * 审批流程名称
     */
    private String code_name ;
    /**
     * 0 单人审批；1 多人审批。单人审批，意思是同一级审批只要有审批人审批后，其他人默认审批。多人审批，必须是同一级所以人审批，才进行下一步审批节点
     */
    private String type;
    /**
     * 创建时间
     */
    private Timestamp create_time ;
    /**
     *拥有者
     */
    private String employee_id ;

    /**
     * 产品code
     */
    private String product_code;

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

    public Timestamp getCreate_time() {
        return create_time;
    }

    public void setCreate_time(Timestamp create_time) {
        this.create_time = create_time;
    }

    public String getEmployee_id() {
        return employee_id;
    }

    public void setEmployee_id(String employee_id) {
        this.employee_id = employee_id;
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
