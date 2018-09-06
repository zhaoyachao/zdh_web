package com.zyc.zspringboot.entity;

import java.io.Serializable;

/**
 * ClassName: ResultInfo   
 * @author zyc-admin
 * @date 2018年2月5日  
 * @Description: TODO  
 */
public class ResultInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3807874205300200606L;
	
	private Object result;
	
	private String status;
	
	private String message;

	public Object getResult() {
		return result;
	}

	public void setResult(Object result) {
		this.result = result;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
}
