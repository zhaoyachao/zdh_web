package com.zyc.zdh.entity;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.sql.Timestamp;

@Table
public class UserGroupInfo extends BaseProductAuthInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6409904097473655093L;

	/**
	 * 主键ID
	 */
	@Id
	@Column
	private String id;

	@Column
	private String group_code;
	/**
	 * 用户组名
	 */
	@Column
	private String name;
	/**
	 * 是否启用 true/false
	 */
	@Column
	private String enable;

	/**
	 * 创建时间
	 */
	private Timestamp create_time;

	/**
	 * 更新时间
	 */
	private Timestamp update_time;

	/**
	 * 产品代码
	 */
	private String product_code;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getGroup_code() {
		return group_code;
	}

	public void setGroup_code(String group_code) {
		this.group_code = group_code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEnable() {
		return enable;
	}

	public void setEnable(String enable) {
		this.enable = enable;
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

	public String getProduct_code() {
		return product_code;
	}

	public void setProduct_code(String product_code) {
		this.product_code = product_code;
	}
}
