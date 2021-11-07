package com.zyc.zdh.entity;

import java.io.Serializable;

import com.zyc.zdh.annotation.SortMark;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name="role_info")
public class RoleInfo extends PageBase implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -167810728020160686L;
	@SortMark(value=0,column="id")
	@Id
	@Column
	private String id;
	@SortMark(value=1,column="role_name")
	private String cole;
	@SortMark(value=2,column = "role_type")
	private String name;


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCole() {
		return cole;
	}

	public void setCole(String cole) {
		this.cole = cole;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
