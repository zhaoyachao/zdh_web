package com.zyc.zdh.dao;

import com.zyc.notscan.base.BaseJarTaskMapper;
import com.zyc.zdh.entity.JarTaskInfo;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * ClassName: DataSourcesMapper
 * @author zyc-admin
 * @date 2017年12月26日  
 * @Description: TODO  
 */
public interface JarTaskMapper extends BaseJarTaskMapper<JarTaskInfo> {

    @Delete({ "<script>",
            "delete from jar_task_info where id in ",
            "<foreach collection='ids' item='id' open='(' separator=',' close=')'>",
            "#{id}",
            "</foreach>",
            "</script>"
    }
    )
    public int deleteBatchById(@Param("ids") String[] ids);

    @Select({"<script>",
            "SELECT * FROM jar_task_info",
            "WHERE owner=#{owner}",
            "<when test='etl_context!=null and etl_context !=\"\"'>",
            "AND etl_context like '%${etl_context}%'",
            "</when>",
            "</script>"})
    public List<JarTaskInfo> selectByParams(@Param("owner") String owner, @Param("etl_context") String etl_context);




}
