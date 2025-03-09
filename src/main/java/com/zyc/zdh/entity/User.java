package com.zyc.zdh.entity;

import java.io.Serializable;
import java.util.List;

public class User extends BaseProductAuthInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6409904097473655093L;
	/**
	 * id
	 */
	private String id;
	/**
	 * 用户账号
	 */
	private String userName;
	/**
	 * 密码
	 */
	private String password;
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
	 * 是否开启手机on/off
	 */
	private String is_use_phone;

	/**
	 * 是否启用true/false
	 */
	private String enable;

	// 下方字段,来自permission_user_info表,通过权限控制
	/**
	 * 用户组
	 */
	private String user_group;

	/**
	 * 签名
	 */
	private String signature;

	/**
	 * 产品
	 */
	private String product_code;

	private List<String> roles;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
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

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public String getProduct_code() {
		return product_code;
	}

	public void setProduct_code(String product_code) {
		this.product_code = product_code;
	}

	public List<String> getRoles() {
		return roles;
	}

	public void setRoles(List<String> roles) {
		this.roles = roles;
	}
}
