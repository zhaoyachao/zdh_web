package com.zyc.zdh.entity;

import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "account_info")
public class AccountInfo {
    @Id
    private Integer id;

    /**
     * 用户名
     */
    private String user_name;

    /**
     * 密码
     */
    private String user_password;

    /**
     * 电子邮箱
     */
    private String email;

    /**
     * 是否开启邮箱告警
     */
    private String is_use_email;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 是否开启手机告警
     */
    private String is_use_phone;

    /**
     * 是否启用true/false
     */
    private String enable;

    /**
     * 用户所在组
     */
    private String user_group;

    /**
     * 签名
     */
    private String signature;

    /**
     * 角色列表
     */
    private String roles;

    /**
     * @return id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取用户名
     *
     * @return user_name - 用户名
     */
    public String getUser_name() {
        return user_name;
    }

    /**
     * 设置用户名
     *
     * @param user_name 用户名
     */
    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    /**
     * 获取密码
     *
     * @return user_password - 密码
     */
    public String getUser_password() {
        return user_password;
    }

    /**
     * 设置密码
     *
     * @param user_password 密码
     */
    public void setUser_password(String user_password) {
        this.user_password = user_password;
    }

    /**
     * 获取电子邮箱
     *
     * @return email - 电子邮箱
     */
    public String getEmail() {
        return email;
    }

    /**
     * 设置电子邮箱
     *
     * @param email 电子邮箱
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * 获取是否开启邮箱告警
     *
     * @return is_use_email - 是否开启邮箱告警
     */
    public String getIs_use_email() {
        return is_use_email;
    }

    /**
     * 设置是否开启邮箱告警
     *
     * @param is_use_email 是否开启邮箱告警
     */
    public void setIs_use_email(String is_use_email) {
        this.is_use_email = is_use_email;
    }

    /**
     * 获取手机号
     *
     * @return phone - 手机号
     */
    public String getPhone() {
        return phone;
    }

    /**
     * 设置手机号
     *
     * @param phone 手机号
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * 获取是否开启手机告警
     *
     * @return is_use_phone - 是否开启手机告警
     */
    public String getIs_use_phone() {
        return is_use_phone;
    }

    /**
     * 设置是否开启手机告警
     *
     * @param is_use_phone 是否开启手机告警
     */
    public void setIs_use_phone(String is_use_phone) {
        this.is_use_phone = is_use_phone;
    }

    /**
     * 获取是否启用true/false
     *
     * @return enable - 是否启用true/false
     */
    public String getEnable() {
        return enable;
    }

    /**
     * 设置是否启用true/false
     *
     * @param enable 是否启用true/false
     */
    public void setEnable(String enable) {
        this.enable = enable;
    }

    /**
     * 获取用户所在组
     *
     * @return user_group - 用户所在组
     */
    public String getUser_group() {
        return user_group;
    }

    /**
     * 设置用户所在组
     *
     * @param user_group 用户所在组
     */
    public void setUser_group(String user_group) {
        this.user_group = user_group;
    }

    /**
     * 获取签名
     *
     * @return signature - 签名
     */
    public String getSignature() {
        return signature;
    }

    /**
     * 设置签名
     *
     * @param signature 签名
     */
    public void setSignature(String signature) {
        this.signature = signature;
    }

    /**
     * 获取角色列表
     *
     * @return roles - 角色列表
     */
    public String getRoles() {
        return roles;
    }

    /**
     * 设置角色列表
     *
     * @param roles 角色列表
     */
    public void setRoles(String roles) {
        this.roles = roles;
    }
}