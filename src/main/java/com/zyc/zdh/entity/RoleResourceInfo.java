package com.zyc.zdh.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;

@Table
public class RoleResourceInfo extends BaseProductAuthInfo{

 	/**
 	* 主键
 	*/
 	@Id
	@Column
   	private String id;
 	/**
 	* 资源id
 	*/
   	private String resource_id;

	/**
	 * 角色code
	 */
	private String role_code;

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

	/**
	 * 产品code
	 */
	private String product_code;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

    public String getResource_id() {
		return resource_id;
	}

	public void setResource_id(String resource_id) {
		this.resource_id = resource_id;
	}

	public String getRole_code() {
		return role_code;
	}

	public void setRole_code(String role_code) {
		this.role_code = role_code;
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

	@Override
	public String getProduct_code() {
		return product_code;
	}

	@Override
	public void setProduct_code(String product_code) {
		this.product_code = product_code;
	}
}