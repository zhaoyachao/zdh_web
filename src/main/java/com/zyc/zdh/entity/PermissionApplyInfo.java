package com.zyc.zdh.entity;

import com.zyc.zdh.util.Const;

import javax.persistence.*;
import java.sql.Timestamp;

@Table(name = "permission_apply_info")
public class PermissionApplyInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY,generator = "JDBC")
    private String id;

    /**
     * 产品code
     */
    private String product_code;

    /**
     * 申请类型
     */
    private String apply_type;

    /**
     * 申请对象标识
     */
    private String apply_code;

    /**
     * 申请原因
     */
    private String reason;

    /**
     * 状态,0:未处理,1:处理中,2:不通过,3:通过,4:撤销
     */
    private String status;

    /**
     * 流程标识id
     */
    private String flow_id;

    /**
     * 拥有者
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

    @Transient
    private String status_name;

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
     * 获取产品code
     *
     * @return product_code - 产品code
     */
    public String getProduct_code() {
        return product_code;
    }

    /**
     * 设置产品code
     *
     * @param product_code 产品code
     */
    public void setProduct_code(String product_code) {
        this.product_code = product_code;
    }

    /**
     * 获取申请类型
     *
     * @return apply_type - 申请类型
     */
    public String getApply_type() {
        return apply_type;
    }

    /**
     * 设置申请类型
     *
     * @param apply_type 申请类型
     */
    public void setApply_type(String apply_type) {
        this.apply_type = apply_type;
    }

    /**
     * 获取申请对象标识
     *
     * @return apply_code - 申请对象标识
     */
    public String getApply_code() {
        return apply_code;
    }

    /**
     * 设置申请对象标识
     *
     * @param apply_code 申请对象标识
     */
    public void setApply_code(String apply_code) {
        this.apply_code = apply_code;
    }

    /**
     * 获取申请原因
     *
     * @return reason - 申请原因
     */
    public String getReason() {
        return reason;
    }

    /**
     * 设置申请原因
     *
     * @param reason 申请原因
     */
    public void setReason(String reason) {
        this.reason = reason;
    }

    /**
     * 获取状态,0:未处理,1:处理中,2:不通过,3:通过,4:撤销
     *
     * @return status - 状态,0:未处理,1:处理中,2:不通过,3:通过,4:撤销
     */
    public String getStatus() {
        return status;
    }

    /**
     * 设置状态,0:未处理,1:处理中,2:不通过,3:通过,4:撤销
     *
     * @param status 状态,0:未处理,1:处理中,2:不通过,3:通过,4:撤销
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * 获取流程标识id
     *
     * @return flow_id - 流程标识id
     */
    public String getFlow_id() {
        return flow_id;
    }

    /**
     * 设置流程标识id
     *
     * @param flow_id 流程标识id
     */
    public void setFlow_id(String flow_id) {
        this.flow_id = flow_id;
    }

    /**
     * 获取拥有者
     *
     * @return owner - 拥有者
     */
    public String getOwner() {
        return owner;
    }

    /**
     * 设置拥有者
     *
     * @param owner 拥有者
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

    public String getStatus_name() {
        return Const.getEnumName("PERMISSION_APPLY", getStatus());
    }
}