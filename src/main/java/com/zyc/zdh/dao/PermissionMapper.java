package com.zyc.zdh.dao;

import com.zyc.notscan.BaseMapper;
import com.zyc.zdh.entity.PermissionUserInfo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import java.util.List;


/**
 * ClassName: AccountDao   
 * @author zyc-admin
 * @date 2018年2月6日  
 * @Description: TODO  
 */
public interface PermissionMapper extends BaseMapper<PermissionUserInfo> {


	@Select({
			"<script>",
			"select * from account_info ",
			"<when test='user_context!=null and user_context !=\"\"'>",
			"where user_name like '%${user_context}%'",
			"or email like '%${user_context}%'",
			"or phone like '%${user_context}%'",
			"</when>",
			"</script>"

	})
	@Results({@Result(column="id",property="id"),
			@Result(column="user_name",property="userName"),
			//@Result(column="user_password",property="password"),
			@Result(column="email",property="email"),
			@Result(column="is_use_email",property="is_use_email"),
			@Result(column="phone",property="phone"),
			@Result(column="is_use_phone",property="is_use_phone")
	})
	public List<PermissionUserInfo> findAll(@Param("user_context") String user_context);

}
