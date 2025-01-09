package com.zyc.zdh.dao;

import com.zyc.notscan.base.BaseResourceTreeMapper;
import com.zyc.zdh.entity.ResourceTreeInfo;
import com.zyc.zdh.entity.RoleResourceInfo;
import com.zyc.zdh.entity.UserResourceInfo2;
import org.apache.ibatis.annotations.*;

import java.util.List;


public interface ResourceTreeMapper extends BaseResourceTreeMapper<ResourceTreeInfo> {

    @Update(value = "update resource_tree_info  set parent=#{parent},level=#{level} where id=#{id}")
    public int updateParentById(@Param("id") String id, @Param("parent") String parent, @Param("level") String level);


    @Delete(value = "delete from role_resource_info  where role_code=#{role_code} and product_code=#{product_code}")
    public int deleteByRoleCode(@Param("role_code") String role_code, @Param("product_code") String product_code);

    @Insert({
            "<script>",
            "INSERT INTO role_resource_info",
            "(resource_id,create_time,update_time,role_code, product_code)",
            "VALUES",
            "<foreach collection='list' item='user' index='index' separator =','>",
            "(#{user.resource_id}, #{user.create_time},#{user.update_time},#{user.role_code},#{user.product_code})",
            "</foreach >",
            "</script>"
    })
    public int updateUserResource(@Param("list") List<RoleResourceInfo> list);


    @Select(value = "select * from role_resource_info where role_code=#{role_code} and product_code=#{product_code}")
    public List<RoleResourceInfo> selectByRoleCode(@Param("role_code") String role_code, @Param("product_code") String product_code);


    @Select(value = "" +
            "select distinct  a.user_account as user_id,c.resource_id,d.`text`,d.url,d.icon,d.order,d.level,d.parent,d.resource_type ,d.notice_title,d.event_code \n" +
            "from \n" +
            "permission_user_info a,\n" +
            "role_info b,\n" +
            "role_resource_info c,\n" +
            "resource_tree_info d  \n" +
            "where \n" +
            "a.product_code=#{product_code} \n"+
            "and a.product_code=b.product_code \n"+
            "and a.product_code=c.product_code \n"+
            "and a.product_code=d.product_code \n"+
            "and find_in_set(b.code, a.roles) and b.code=c.role_code and c.resource_id = d.id and a.user_account=#{user_account} and b.enable='true'"
    )
    public List<UserResourceInfo2> selectResourceByUserAccount(@Param("user_account") String user_account, @Param("product_code") String product_code);

    @Select(value = "" +
            "select distinct b.code as user_id, c.resource_id,d.`text`,d.url,d.icon,d.order,d.level,d.parent,d.resource_type ,d.notice_title,d.event_code \n" +
            "from \n" +
            "role_info b,\n" +
            "role_resource_info c,\n" +
            "resource_tree_info d  \n" +
            "where \n" +
            "a.product_code=#{product_code} \n"+
            "and a.product_code=b.product_code \n"+
            "and a.product_code=c.product_code \n"+
            "and b.code=#{role_code} and b.code=c.role_code and c.resource_id = d.id  and b.enable='true'"
    )
    public List<UserResourceInfo2> selectResourceByRoleCode(@Param("role_code") String role_code, @Param("product_code") String product_code);

}