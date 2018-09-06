package com.zyc.zspringboot.entity;

import java.io.Serializable;

import com.zyc.zspringboot.annotation.SortMark;

public class Role extends PageBase implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -167810728020160686L;
	@SortMark(value=0,column="id")
	private String id;
	@SortMark(value=1,column="role_name")
	private String roleName;
	@SortMark(value=2,column = "role_type")
	private String roleType;
	@SortMark(value=3, column = "content")
	private String content;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getRoleType() {
		return roleType;
	}

	public void setRoleType(String roleType) {
		this.roleType = roleType;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	
	
	

}
