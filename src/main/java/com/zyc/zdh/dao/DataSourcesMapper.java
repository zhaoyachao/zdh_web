package com.zyc.zdh.dao;

import com.zyc.notscan.BaseMapper;
import com.zyc.zdh.entity.DataSourcesInfo;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * ClassName: DataSourcesMapper
 * @author zyc-admin
 * @date 2017年12月26日  
 * @Description: TODO  
 */
public interface DataSourcesMapper extends BaseMapper<DataSourcesInfo> {

    @Delete("delete from data_sources_info where id = #{ids_str}")
    public int deleteBatchById(@Param("ids_str") String ids_str);

    @Update(
            {
                "<script>",
                "update data_sources_info set is_delete=1 where id in ",
                "<foreach collection='ids' item='id' open='(' separator=',' close=')'>",
                "#{id}",
                "</foreach>",
                "</script>"
            }
    )
    public int deleteBatchById2(@Param("ids") String[] ids);

    @Select(value = "select sources_type from data_sources_type_info")
    public List<String> selectDataSourcesType();

    @Select({"<script>",
            "SELECT * FROM data_sources_info",
            "WHERE owner=#{owner}",
            "<when test='data_source_context!=null and data_source_context !=\"\"'>",
            "AND data_source_context like '%${data_source_context}%'",
            "</when>",
            "<when test='data_source_type!=null and data_source_type !=\"\"'>",
            "AND data_source_type = #{data_source_type}",
            "</when>",
            "<when test='url!=null and url !=\"\"'>",
            "AND url like '%${url}%' ",
            "</when>",
            "AND is_delete=0",
            "</script>"})
    public List<DataSourcesInfo> selectByParams(@Param("data_source_context") String data_source_context, @Param("data_source_type") String data_source_type,
                                                @Param("url") String url, @Param("owner") String owner);
}
