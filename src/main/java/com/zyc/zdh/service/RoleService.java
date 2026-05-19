package com.zyc.zdh.service;

import com.zyc.zdh.entity.PageBase;
import com.zyc.zdh.entity.RoleInfo;

import java.util.List;

public interface RoleService {

	public RoleInfo getRole(String id);
	
	public String list(String aoData);
	
	public List<RoleInfo> findList(PageBase page);

	public int delRole(String id);
}
