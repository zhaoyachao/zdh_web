package com.zyc.zdh.dao;

import com.zyc.notscan.base.BaseJarFileMapper;
import com.zyc.zdh.entity.JarFileInfo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;


public interface JarFileMapper extends BaseJarFileMapper<JarFileInfo> {

    @Select({"<script>",
            "SELECT * FROM jar_file_info",
            "WHERE owner=#{owner}",
            "and id in",
            "<foreach collection='ids' item='id' open='(' separator=',' close=')'>",
            "#{id}",
            "</foreach>",
            "</script>"})
    public List<JarFileInfo> selectByParams(@Param("owner") String owner, @Param("ids") String[] ids);

    @Select({"<script>",
            "SELECT * FROM jar_file_info",
            "WHERE owner=#{owner}",
            "and jar_etl_id in",
            "<foreach collection='ids' item='id' open='(' separator=',' close=')'>",
            "#{id}",
            "</foreach>",
            "</script>"})
    public List<JarFileInfo> selectByParams2(@Param("owner") String owner, @Param("ids") String[] ids);

}