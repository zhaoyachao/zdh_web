package com.zyc.zdh.dao;

import com.zyc.notscan.base.BasePermissionMapper;
import com.zyc.zdh.entity.PermissionUserInfo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;


/**
 * ClassName: AccountDao   
 * @author zyc-admin
 * @date 2018年2月6日  
 * @Description: TODO  
 */
public interface PermissionMapper extends BasePermissionMapper<PermissionUserInfo> {

	@Update(
			{
					"<script>",
					"update permission_user_info set enable=#{enable} where id in",
					"<foreach collection='ids' item='id' open='(' separator=',' close=')'>",
					"#{id}",
					"</foreach>",
					"</script>"
			}
	)
	public int updateEnable(@Param("ids") String[] ids, @Param("enable") String enable);


	@Update(
			{
					"<script>",
					"update permission_user_info set roles=#{roles} where product_code =",
					"<foreach collection='ids' item='id' open='(' separator=',' close=')'>",
					"#{id}",
					"</foreach>",
					"</script>"
			}
	)
	public int updateRoles(@Param("product_code") String product_code,@Param("user_account") String user_account, @Param("roles") String roles);

	@Update(
			{
					"<script>",
					"update permission_user_info set user_group=#{user_group} where id in",
					"<foreach collection='ids' item='id' open='(' separator=',' close=')'>",
					"#{id}",
					"</foreach>",
					"</script>"
			}
	)
	public int updateUserGroup(@Param("ids") String[] ids, @Param("user_group") String user_group);

	@Update(
			{
					"<script>",
					"update permission_user_info set tag_group_code=#{tag_group_code} where id in",
					"<foreach collection='ids' item='id' open='(' separator=',' close=')'>",
					"#{id}",
					"</foreach>",
					"</script>"
			}
	)
	public int updateDataTagGroup(@Param("ids") String[] ids, @Param("tag_group_code") String tag_group_code);

	@Select({
			"<script>",
			"select user_account from permission_user_info where enable='true' and product_code = #{product_code}",
			"<when test='user_account!=null and user_account !=\"\"'>",
			"and user_account = #{user_account}",
			"</when>",
			"</script>"
	})
	public List<String> selectAccountByProduct(@Param("product_code") String product_code, @Param("user_account") String user_account);

}
