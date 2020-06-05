package com.zyc.zdh.disruptor.queue;

import java.io.Serializable;

/**
 * ClassName: ParamSupporter   
 * @author zyc-admin
 * @date 2018年1月5日  
 * @Description: TODO  
 */
public class ParamSupporter implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2068538496894538074L;

	private String id;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
}
