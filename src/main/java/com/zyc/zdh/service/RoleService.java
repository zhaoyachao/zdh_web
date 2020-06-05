package com.zyc.zdh.service;

import java.util.List;

import com.zyc.zdh.entity.PageBase;
import com.zyc.zdh.entity.Role;

public interface RoleService {

	public Role getRole(String id);
	
	public String list(String aoData);
	
	public List<Role> findList(PageBase page);

	public int delRole(String id);
}
