package com.zyc.zdh.shiro;

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

	public static final String captcha_key = "zdh_captcha";

	private String captcha;
	private String ipAddr;
	private String session_captcha;
	private String product_code;
	
	public MyAuthenticationToken(String username,String password,boolean rememberMe,String host,String captcha,String ipAddr,String session_captcha, String product_code) {
		super(username, password, rememberMe, host);
	    this.captcha = captcha;
	    this.ipAddr=ipAddr;
	    this.session_captcha=session_captcha;
	    this.product_code=product_code;
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

	public String getSession_captcha() {
		return session_captcha;
	}

	public void setSession_captcha(String session_captcha) {
		this.session_captcha = session_captcha;
	}

	public String getProduct_code() {
		return product_code;
	}

	public void setProduct_code(String product_code) {
		this.product_code = product_code;
	}
}
