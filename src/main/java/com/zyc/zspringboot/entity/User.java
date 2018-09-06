package com.zyc.zspringboot.entity;

import java.io.Serializable;

public class User extends PageBase implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6409904097473655093L;
	private String userName;
	private String password;
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
	
	
}
