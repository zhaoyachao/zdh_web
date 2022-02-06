package com.zyc.zdh.entity;

import java.io.Serializable;

public class User extends PageBase implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6409904097473655093L;
	private String id;
	private String userName;
	private String password;
	private String email;
	private String is_use_email;
	private String phone;
	private String is_use_phone;

	private String user_group;

	private String enable;

	private String roles;

	private String signature;

	private String tag_group_code;

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
}
