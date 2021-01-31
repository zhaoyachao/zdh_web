package com.zyc.zdh.dao;

import com.zyc.notscan.BaseMapper;
import com.zyc.zdh.entity.ResourceTreeInfo;
import com.zyc.zdh.entity.UserResourceInfo;
import com.zyc.zdh.entity.UserResourceInfo2;
import org.apache.ibatis.annotations.*;

import java.util.List;


public interface ResourceTreeMapper extends BaseMapper<ResourceTreeInfo> {

    @Update(value = "update resource_tree_info  set parent=#{parent} where id=#{id}")
    public int updateParentById(@Param("id") String id, @Param("parent") String parent);


    @Delete(value = "delete from user_resource_info  where user_id=#{user_id}")
    public int deleteById(@Param("user_id") String user_id);

    @Insert({
            "<script>",
            "INSERT INTO user_resource_info",
            "(user_id, resource_id)",
            "VALUES",
            "<foreach collection='list' item='user' index='index' separator =','>",
            "(#{user.user_id}, #{user.resource_id})",
            "</foreach >",
            "</script>"
    })
    public int updateUserResource(@Param("list") List<UserResourceInfo> list);

    @Select(value = "select * from user_resource_info where user_id=#{user_id}")
    public List<UserResourceInfo> selectByUserId(@Param("user_id") String user_id);

    @Select(value = "select a.user_id,a.resource_id,b.`text`,b.url,b.icon,b.order,b.level,b.parent from user_resource_info a,resource_tree_info b  where a.resource_id = b.id and user_id=#{user_id}")
    public List<UserResourceInfo2> selectResourceByUserId(@Param("user_id") String user_id);


}