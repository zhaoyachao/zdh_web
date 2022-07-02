package com.zyc.zdh.dao;

import com.zyc.notscan.BaseMapper;
import com.zyc.zdh.entity.RoleInfo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface RoleDao extends BaseMapper<RoleInfo> {

    @Select({
            "<script>",
            "select * from role_info where 1=1",
            "<when test='role_context!=null and role_context !=\"\"'>",
            "and code like #{role_context}",
            "or name like #{role_context}",
            "</when>",
            "<when test='enable!=null and enable !=\"\"'>",
            "and enable = #{enable}",
            "</when>",
            "<when test='product_code!=null and product_code !=\"\"'>",
            "and product_code = #{product_code}",
            "</when>",
            "</script>"

    })
    public List<RoleInfo> selectByContext(@Param("role_context") String role_context,@Param("enable") String enable,@Param("product_code") String product_code);

    @Update(
            {
                    "<script>",
                    "update role_info set enable=#{enable} where id in",
                    "<foreach collection='ids' item='id' open='(' separator=',' close=')'>",
                    "#{id}",
                    "</foreach>",
                    "</script>"
            }
    )
    public int updateEnable(@Param("ids") String[] ids, @Param("enable") String enable);

}
