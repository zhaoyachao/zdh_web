package com.zyc.zdh.dao;

import com.zyc.notscan.BaseMapper;
import com.zyc.zdh.entity.PermissionUserInfo;
import org.apache.ibatis.annotations.*;

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
	@Results(value = {@Result(column="id",property="id"),
			@Result(column="user_name",property="userName"),
			//@Result(column="user_password",property="password"),
			@Result(column="email",property="email"),
			@Result(column="is_use_email",property="is_use_email"),
			@Result(column="phone",property="phone"),
			@Result(column="is_use_phone",property="is_use_phone"),
			@Result(column="user_group",property="user_group"),
			@Result(column="enable",property="enable")
	})
	public List<PermissionUserInfo> findAll(@Param("user_context") String user_context);

	@Update(
			{
					"<script>",
					"update account_info set enable=#{enable} where id in",
					"<foreach collection='ids' item='id' open='(' separator=',' close=')'>",
					"#{id}",
					"</foreach>",
					"</script>"
			}
	)
	public int updateEnable(@Param("ids") String[] ids, @Param("enable") String enable);

	@Select({
			"<script>",
			"select * from account_info where id in ",
			"<foreach collection='ids' item='id' open='(' separator=',' close=')'>",
			"#{id}",
			"</foreach>",
			"</script>"

	})
	public List<PermissionUserInfo> selectByKeys(@Param("ids") String[] ids);
}
