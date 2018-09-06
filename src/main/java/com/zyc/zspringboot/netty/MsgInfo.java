package com.zyc.zspringboot.netty;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;


/***   
 * ClassName: MsgInfo   
 * @author zyc-admin
 * @date 2017年12月26日  
 * @Description: 信息实体类  
 */
@Table(name="msg_info")
public class MsgInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8076304830379671365L;

	@Id
	@Column(name="msg_id")
	private long msgId;
	
	@Column(name="msg")
	private String msg;
	
	@Column(name="form_user",nullable=true)
	private String formUser;
	
	@Column(name="to_user",nullable=true)
	private String toUser;
	
	@Column(name="msg_group")
	private String group;
	
	@Column(name="state")
	private int state;
	
	@Column(name="msg_type")
	private String type;
	
	@Column(name="time",insertable=false,updatable=false)
	private Date time;

	public long getMsgId() {
		return msgId;
	}

	public void setMsgId(long msgId) {
		this.msgId = msgId;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getFormUser() {
		return formUser;
	}

	public void setFormUser(String formUser) {
		this.formUser = formUser;
	}

	public String getToUser() {
		return toUser;
	}

	public void setToUser(String toUser) {
		this.toUser = toUser;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}
	
	
}
