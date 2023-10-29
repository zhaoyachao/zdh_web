package com.zyc.zdh.dao;

import com.zyc.notscan.base.BaseDataSourcesMapper;
import com.zyc.zdh.entity.DataSourcesInfo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.sql.Timestamp;
import java.util.List;

public interface DataSourcesMapper extends BaseDataSourcesMapper<DataSourcesInfo> {

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
            "and product_code in ",
            "<foreach collection='product_codes' item='product_code' open='(' separator=',' close=')'>",
            "#{product_code}",
            "</foreach>",
            "and dim_group in ",
            "<foreach collection='dim_groups' item='dim_group' open='(' separator=',' close=')'>",
            "#{dim_group}",
            "</foreach>",
            "<when test='product_code!=null and product_code !=\"\"'>",
            "AND product_code = #{product_code}",
            "</when>",
            "<when test='dim_group!=null and dim_group !=\"\"'>",
            "AND dim_group = #{dim_group}",
            "</when>",
            "</script>"})
    public List<DataSourcesInfo> selectByParams(@Param("owner") String owner,@Param("tag_group_code") String[] tag_group_code,
                                                @Param("product_code") String product_code, @Param("dim_group") String dim_group,
                                                @Param("product_codes") List<String> product_codes,
                                                @Param("dim_groups") List<String> dim_groups);

    @Select({"<script>",
            "SELECT * FROM data_sources_info",
            "WHERE is_delete=0",
            "<when test='data_source_type!=null and data_source_type !=\"\"'>",
            "AND data_source_type =  #{data_source_type}",
            "</when>",
            "<when test='data_source_context!=null and data_source_context !=\"\"'>",
            "AND data_source_context like  #{data_source_context}",
            "</when>",
            "<when test='url!=null and url !=\"\"'>",
            "AND url like  #{url}",
            "</when>",
            "and product_code in ",
            "<foreach collection='product_codes' item='product_code' open='(' separator=',' close=')'>",
            "#{product_code}",
            "</foreach>",
            "and dim_group in ",
            "<foreach collection='dim_groups' item='dim_group' open='(' separator=',' close=')'>",
            "#{dim_group}",
            "</foreach>",
            "<when test='product_code!=null and product_code !=\"\"'>",
            "AND product_code = #{product_code}",
            "</when>",
            "<when test='dim_group!=null and dim_group !=\"\"'>",
            "AND dim_group = #{dim_group}",
            "</when>",
            "</script>"})
    public List<DataSourcesInfo> selectByParams2(@Param("owner") String owner,@Param("tag_group_code") String[] tag_group_code,
                                                 @Param("url") String url,@Param("data_source_context") String data_source_context,
                                                 @Param("data_source_type") String data_source_type,
                                                 @Param("product_code") String product_code, @Param("dim_group") String dim_group,
                                                 @Param("product_codes") List<String> product_codes,
                                                 @Param("dim_groups") List<String> dim_groups);
}