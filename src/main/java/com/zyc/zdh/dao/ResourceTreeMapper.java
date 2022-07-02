package com.zyc.zdh.dao;

import com.zyc.notscan.BaseMapper;
import com.zyc.zdh.entity.ResourceTreeInfo;
import com.zyc.zdh.entity.RoleResourceInfo;
import com.zyc.zdh.entity.UserResourceInfo2;
import org.apache.ibatis.annotations.*;

import java.util.List;


public interface ResourceTreeMapper extends BaseMapper<ResourceTreeInfo> {

    @Update(value = "update resource_tree_info  set parent=#{parent},level=#{level} where id=#{id}")
    public int updateParentById(@Param("id") String id, @Param("parent") String parent, @Param("level") String level);


    @Delete(value = "delete from role_resource_info  where role_id=#{role_id}")
    public int deleteByRoleId(@Param("role_id") String role_id);

    @Insert({
            "<script>",
            "INSERT INTO role_resource_info",
            "(role_id, resource_id,create_time,update_time)",
            "VALUES",
            "<foreach collection='list' item='user' index='index' separator =','>",
            "(#{user.role_id}, #{user.resource_id}, #{user.create_time},#{user.update_time})",
            "</foreach >",
            "</script>"
    })
    public int updateUserResource(@Param("list") List<RoleResourceInfo> list);

    @Select(value = "select * from role_resource_info where role_id=#{role_id}")
    public List<RoleResourceInfo> selectByRoleId(@Param("role_id") String role_id);


    @Select(value = "" +
            "select distinct  a.user_account as user_id,c.resource_id,d.`text`,d.url,d.icon,d.order,d.level,d.parent,d.resource_type ,d.notice_title,d.event_code \n" +
            "from \n" +
            "permission_user_info a,\n" +
            "role_info b,\n" +
            "role_resource_info c,\n" +
            "resource_tree_info d  \n" +
            "where \n" +
            "find_in_set(b.id, a.roles) and b.id=c.role_id and c.resource_id = d.id and a.user_account=#{user_account} and b.enable='true'"
    )
    public List<UserResourceInfo2> selectResourceByUserAccount(@Param("user_account") String user_account);

    @Select(value = "" +
            "select distinct b.code as user_id, c.resource_id,d.`text`,d.url,d.icon,d.order,d.level,d.parent,d.resource_type ,d.notice_title,d.event_code \n" +
            "from \n" +
            "role_info b,\n" +
            "role_resource_info c,\n" +
            "resource_tree_info d  \n" +
            "where \n" +
            "b.code=#{role_code} and b.id=c.role_id and c.resource_id = d.id  and b.enable='true'"
    )
    public List<UserResourceInfo2> selectResourceByRoleCode(@Param("role_code") String role_code);

}