package com.zyc.zdh.entity;

import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.sql.Timestamp;
import java.util.Map;

@Table(name = "permission_usergroup_dimension_value_info")
public class PermissionUserGroupDimensionValueInfo {
    @Id
    private String id;

    /**
     * 用户组code
     */
    private String group_code;

    /**
     * 维度code
     */
    private String dim_code;

    /**
     * 维度值code
     */
    private String dim_value_code;

    /**
     * 产品code
     */
    private String product_code;

    /**
     * 账号
     */
    private String owner;

    /**
     * 启用状态,1:启用,2:未启用
     */
    private String enable;

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
     * 扩展信息
     */
    private String ext;

    @Transient
    private Map<String, String> extMap;

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
     * 获取用户组code
     *
     * @return group_code - 用户组code
     */
    public String getGroup_code() {
        return group_code;
    }

    /**
     * 设置用户组code
     *
     * @param group_code 用户组code
     */
    public void setGroup_code(String group_code) {
        this.group_code = group_code;
    }

    /**
     * 获取维度code
     *
     * @return dim_code - 维度code
     */
    public String getDim_code() {
        return dim_code;
    }

    /**
     * 设置维度code
     *
     * @param dim_code 维度code
     */
    public void setDim_code(String dim_code) {
        this.dim_code = dim_code;
    }

    /**
     * 获取维度值code
     *
     * @return dim_value_code - 维度值code
     */
    public String getDim_value_code() {
        return dim_value_code;
    }

    /**
     * 设置维度值code
     *
     * @param dim_value_code 维度值code
     */
    public void setDim_value_code(String dim_value_code) {
        this.dim_value_code = dim_value_code;
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
     * 获取启用状态,1:启用,2:未启用
     *
     * @return enable - 启用状态,1:启用,2:未启用
     */
    public String getEnable() {
        return enable;
    }

    /**
     * 设置启用状态,1:启用,2:未启用
     *
     * @param enable 启用状态,1:启用,2:未启用
     */
    public void setEnable(String enable) {
        this.enable = enable;
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

    public String getExt() {
        return ext;
    }

    public void setExt(String ext) {
        this.ext = ext;
    }

    public Map<String, String> getExtMap() {
        if(!StringUtils.isEmpty(ext)){
            return JSON.parseObject(ext, Map.class);
        }
        return extMap;
    }

    public void setExtMap(Map<String, String> extMap) {
        this.extMap = extMap;
    }
}