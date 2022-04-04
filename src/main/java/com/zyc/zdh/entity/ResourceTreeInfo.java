package com.zyc.zdh.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;
import java.sql.Timestamp;

@Table
public class ResourceTreeInfo{

 	/**
 	* 主键
 	*/
 	@Id
	@Column
   	private String id;
 	/**
 	* 上级id
 	*/
   	private String parent="#";
 	/**
 	* 资源名称
 	*/
   	private String text;

 	/**
 	* 资源图标
 	*/
   	private String icon="non";
 	/**
 	* 层级（1-系统,2-模块, 3-菜单，4-按钮）
 	*/
   	private String level;

   	private String url="";

 	/**
 	* 资源描述
 	*/
   	private String resource_desc;

 	/**
 	* 排序
 	*/
 	@Column(name = "`order`")
   	private String order="1";
 	/**
 	* 是否启用
 	*/
   	private String is_enable;
 	/**
 	* 创建人id
 	*/
   	private String owner;
 	/**
 	* 创建时间
 	*/
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	@Column(updatable = false)
   	private Timestamp create_time;
 	/**
 	* 修改时间
 	*/
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
   	private Timestamp update_time;

	private String resource_type;//1:目录,2:页面,3:方法,4:接口

	private String notice_title;//提示语

	private String event_code;

	private String product_code;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getParent() {
		return parent;
	}

	public void setParent(String parent) {
		this.parent = parent;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public String getResource_desc() {
		return resource_desc;
	}

	public void setResource_desc(String resource_desc) {
		this.resource_desc = resource_desc;
	}

	public int getOrderN() {
		if(order==null || order.trim().equals("")){
			return 1;
		}
		return new Integer(order);
	}

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}

	public String getIs_enable() {
		return is_enable;
	}

	public void setIs_enable(String is_enable) {
		this.is_enable = is_enable;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public Timestamp getCreate_time() {
		return create_time;
	}

	public void setCreate_time(Timestamp create_time) {
		this.create_time = create_time;
	}

	public Timestamp getUpdate_time() {
		return update_time;
	}

	public void setUpdate_time(Timestamp update_time) {
		this.update_time = update_time;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getResource_type() {
		return resource_type;
	}

	public void setResource_type(String resource_type) {
		this.resource_type = resource_type;
	}

	public String getNotice_title() {
		return notice_title;
	}

	public void setNotice_title(String notice_title) {
		this.notice_title = notice_title;
	}

	public String getEvent_code() {
		return event_code;
	}

	public void setEvent_code(String event_code) {
		this.event_code = event_code;
	}

	public String getProduct_code() {
		return product_code;
	}

	public void setProduct_code(String product_code) {
		this.product_code = product_code;
	}
}