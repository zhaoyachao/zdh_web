package com.zyc.zdh.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zyc.zdh.annotation.SortMark;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.sql.Timestamp;

@Table(name="role_info")
public class RoleInfo extends PageBase implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -167810728020160686L;
	/**
	 * ID
	 */
	@SortMark(value=0,column="id")
	@Id
	@Column
	private String id;
	/**
	 * 角色code
	 */
	@SortMark(value=1,column="role_name")
	private String code;
	/**
	 * 角色名称
	 */
	@SortMark(value=2,column = "role_type")
	private String name;
	/**
	 * 是否启用true/false
	 */
	private String enable;

	/**
	 * 创建时间
	 */
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	private Timestamp create_time;
	/**
	 * 修改时间
	 */
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	private Timestamp update_time;

	private String product_code;


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}


	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public String getEnable() {
		return enable;
	}

	public void setEnable(String enable) {
		this.enable = enable;
	}

	public String getProduct_code() {
		return product_code;
	}

	public void setProduct_code(String product_code) {
		this.product_code = product_code;
	}
}
