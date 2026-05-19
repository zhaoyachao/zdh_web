package com.zyc.zdh.entity;

import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;

@Table(name = "permission_bigdata_info")
public class PermissionBigdataInfo {
    @Id
    private String id;

    private String user_account;

    /**
     * 产品code
     */
    private String product_code;

    /**
     * 产品类型
     */
    private String product_type;

    /**
     * 资源类型,文件,目录,表
     */
    private String resource_type;

    /**
     * 用户组是否作为当前路径管理层
     */
    private String resource_manage_group;

    /**
     * 权限规则
     */
    private String permission_rule;

    /**
     * 路径
     */
    private String path;

    /**
     * 用户组
     */
    private String user_group_code;

    /**
     * 权限层级
     */
    private String permission_depth_level;

    /**
     * 拥有者
     */
    private String owner;

    /**
     * 创建时间
     */
    private Timestamp create_time;

    /**
     * 更新时间
     */
    private Timestamp update_time;

    /**
     * 是否删除,0:未删除,1:删除
     */
    private String is_delete;

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

    public String getUser_account() {
        return user_account;
    }

    public void setUser_account(String user_account) {
        this.user_account = user_account;
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
     * 获取产品类型
     *
     * @return product_type - 产品类型
     */
    public String getProduct_type() {
        return product_type;
    }

    /**
     * 设置产品类型
     *
     * @param product_type 产品类型
     */
    public void setProduct_type(String product_type) {
        this.product_type = product_type;
    }

    /**
     * 获取资源类型,文件,目录,表
     *
     * @return resource_type - 资源类型,文件,目录,表
     */
    public String getResource_type() {
        return resource_type;
    }

    /**
     * 设置资源类型,文件,目录,表
     *
     * @param resource_type 资源类型,文件,目录,表
     */
    public void setResource_type(String resource_type) {
        this.resource_type = resource_type;
    }

    /**
     * 获取用户组是否作为当前路径管理层
     *
     * @return resource_manage_group - 用户组是否作为当前路径管理层
     */
    public String getResource_manage_group() {
        return resource_manage_group;
    }

    /**
     * 设置用户组是否作为当前路径管理层
     *
     * @param resource_manage_group 用户组是否作为当前路径管理层
     */
    public void setResource_manage_group(String resource_manage_group) {
        this.resource_manage_group = resource_manage_group;
    }

    /**
     * 获取权限规则
     *
     * @return permission_rule - 权限规则
     */
    public String getPermission_rule() {
        return permission_rule;
    }

    /**
     * 设置权限规则
     *
     * @param permission_rule 权限规则
     */
    public void setPermission_rule(String permission_rule) {
        this.permission_rule = permission_rule;
    }

    /**
     * 获取路径
     *
     * @return path - 路径
     */
    public String getPath() {
        return path;
    }

    /**
     * 设置路径
     *
     * @param path 路径
     */
    public void setPath(String path) {
        this.path = path;
    }

    /**
     * 获取用户组
     *
     * @return user_group_code - 用户组
     */
    public String getUser_group_code() {
        return user_group_code;
    }

    /**
     * 设置用户组
     *
     * @param user_group_code 用户组
     */
    public void setUser_group_code(String user_group_code) {
        this.user_group_code = user_group_code;
    }

    /**
     * 获取权限层级
     *
     * @return permission_depth_level - 权限层级
     */
    public String getPermission_depth_level() {
        return permission_depth_level;
    }

    /**
     * 设置权限层级
     *
     * @param permission_depth_level 权限层级
     */
    public void setPermission_depth_level(String permission_depth_level) {
        this.permission_depth_level = permission_depth_level;
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
}