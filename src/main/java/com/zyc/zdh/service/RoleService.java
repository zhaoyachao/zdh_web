package com.zyc.zdh.service;

import java.util.List;

import com.zyc.zdh.entity.PageBase;
import com.zyc.zdh.entity.RoleInfo;
import com.zyc.zdh.entity.RoleInfo;

public interface RoleService {

	public RoleInfo getRole(String id);
	
	public String list(String aoData);
	
	public List<RoleInfo> findList(PageBase page);

	public int delRole(String id);
}
