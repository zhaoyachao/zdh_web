package com.zyc.zdh.dao;

import com.zyc.notscan.BaseMapper;
import com.zyc.zdh.entity.DataSourcesInfo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.sql.Timestamp;
import java.util.List;

public interface DataSourcesMapper extends BaseMapper<DataSourcesInfo> {

    @Update(
            {
                    "<script>",
                    "update data_sources_info set is_delete=1 ,update_time= #{update_time} where id in ",
                    "<foreach collection='ids' item='id' open='(' separator=',' close=')'>",
                    "#{id}",
                    "</foreach>",
                    "</script>"
            }
    )
    public int deleteBatchById(@Param("ids") String[] ids, @Param("update_time") Timestamp update_time);

    @Select(value = "select distinct sources_type from data_sources_type_info")
    public List<String> selectDataSourcesType();


    @Select({"<script>",
            "SELECT * FROM data_sources_info",
            "WHERE is_delete=0",
            "<when test='owner!=null and owner !=\"\"'>",
            "AND owner =  #{owner}",
            "</when>",
            "<when test='tag_group_code!=null and tag_group_code.length>0'>",
            " or (",
            "<foreach collection='tag_group_code' item='group_code' separator='or'>",
            "FIND_IN_SET('${group_code}',tag_group_code)",
            "</foreach >",
            " and is_delete=0 )",
            "</when>",
            "</script>"})
    public List<DataSourcesInfo> selectByParams(@Param("owner") String owner,@Param("tag_group_code") String[] tag_group_code);

    @Select({"<script>",
            "SELECT * FROM data_sources_info",
            "WHERE is_delete=0",
            "and ( ",
            "<when test='owner!=null and owner !=\"\"'>",
            "owner =  #{owner}",
            "</when>",
            "<when test='tag_group_code!=null and tag_group_code.length>0'>",
            " or (",
            "<foreach collection='tag_group_code' item='group_code' separator='or'>",
            "FIND_IN_SET('${group_code}',tag_group_code)",
            "</foreach >",
            " and is_delete=0 )",
            "</when>",
            " )",
            "<when test='data_source_type!=null and data_source_type !=\"\"'>",
            "AND data_source_type =  #{data_source_type}",
            "</when>",
            "<when test='data_source_context!=null and data_source_context !=\"\"'>",
            "AND data_source_context like  #{data_source_context}",
            "</when>",
            "<when test='url!=null and url !=\"\"'>",
            "AND url like  #{url}",
            "</when>",
            "</script>"})
    public List<DataSourcesInfo> selectByParams2(@Param("owner") String owner,@Param("tag_group_code") String[] tag_group_code, @Param("url") String url,@Param("data_source_context") String data_source_context,@Param("data_source_type") String data_source_type);
}