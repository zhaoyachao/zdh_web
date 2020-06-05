package com.zyc.zdh.dao;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import com.zyc.zdh.entity.PageBase;
import com.zyc.zdh.entity.Role;

public interface RoleDao {

	@Select("select * from system_role where id=#{id}")
	@Results({
		@Result(column="id",property="id"),
		@Result(column="role_name",property="roleName"),
		@Result(column="role_type",property="roleType"),
		@Result(column="content",property="content")
	})
	public Role getRole(@Param("id") String id);
	
	@Select("select * from system_role order by ${sortColumn} ${sortDir}")
	@Results({
		@Result(column="id",property="id"),
		@Result(column="role_name",property="roleName"),
		@Result(column="role_type",property="roleType"),
		@Result(column="content",property="content")
	})
	public List<Role> findList(PageBase page);

	@Delete("delete from system_role where id=#{id}")
	public int delRole(@Param("id") String id);
}
