package com.zyc.zspringboot.shiro;

import java.io.Serializable;

public class ShiroUser implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6300514783554300575L;

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
