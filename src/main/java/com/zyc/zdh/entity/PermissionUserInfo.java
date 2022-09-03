package com.zyc.zdh.entity;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Table(name = "permission_user_info")
public class PermissionUserInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6409904097473655093L;
	/**
	 * ID
	 */
	@Id
	@Column
	private String id;
	/**
	 * 用户账号
	 */
	private String user_account;
	/**
	 * 用户名
	 */
	private String user_name;
	/**
	 * 密码
	 */
	@Column(name="user_password")
	private String user_password;
	/**
	 * 邮箱
	 */
	private String email;
	/**
	 * 是否开启邮箱on/off
	 */
	private String is_use_email;
	/**
	 * 手机号
	 */
	private String phone;
	/**
	 * 是否开启手机
	 */
	private String is_use_phone;

	/**
	 * 用户组
	 */
	private String user_group;

	/**
	 * 是否启用true/false
	 */
	private String enable;

	/**
	 * 角色ID,多个逗号分割
	 */
	private String roles;

	/**
	 * 签名
	 */
	private String signature;

	/**
	 * 数据标识组
	 */
	private String tag_group_code;

	/**
	 * 产品代码
	 */
	private String product_code;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUser_account() {
		return user_account;
	}

	public void setUser_account(String user_account) {
		this.user_account = user_account;
	}

	public String getUser_name() {
		return user_name;
	}

	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}

	public String getUser_password() {
		return user_password;
	}

	public void setUser_password(String user_password) {
		this.user_password = user_password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getIs_use_email() {
		return is_use_email;
	}

	public void setIs_use_email(String is_use_email) {
		this.is_use_email = is_use_email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getIs_use_phone() {
		return is_use_phone;
	}

	public void setIs_use_phone(String is_use_phone) {
		this.is_use_phone = is_use_phone;
	}

	public String getUser_group() {
		return user_group;
	}

	public void setUser_group(String user_group) {
		this.user_group = user_group;
	}

	public String getEnable() {
		return enable;
	}

	public void setEnable(String enable) {
		this.enable = enable;
	}

	public String getRoles() {
		return roles;
	}

	public void setRoles(String roles) {
		this.roles = roles;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public String getTag_group_code() {
		return tag_group_code;
	}

	public void setTag_group_code(String tag_group_code) {
		this.tag_group_code = tag_group_code;
	}

	public String getProduct_code() {
		return product_code;
	}

	public void setProduct_code(String product_code) {
		this.product_code = product_code;
	}
}
