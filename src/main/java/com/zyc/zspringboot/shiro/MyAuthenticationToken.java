package com.zyc.zspringboot.shiro;

import org.apache.shiro.authc.UsernamePasswordToken;
/**
 * 用来存储验证码等其他信息
 * @author Administrator
 *
 */
public class MyAuthenticationToken extends  UsernamePasswordToken {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2681757716434500100L;
	
	private String captcha;
	private String ipAddr;
	
	public MyAuthenticationToken(String username,String password,boolean rememberMe,String host,String captcha,String ipAddr) {        
		super(username, password, rememberMe, host);
	    this.captcha = captcha;
	    this.ipAddr=ipAddr;
	}
	
	public String getCaptcha() {
		return captcha;
	}

	public void setCaptcha(String captcha) {
		this.captcha = captcha;
	}

	public String getIpAddr() {
		return ipAddr;
	}

	public void setIpAddr(String ipAddr) {
		this.ipAddr = ipAddr;
	}

}
