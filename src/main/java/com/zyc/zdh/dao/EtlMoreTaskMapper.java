package com.zyc.zdh.dao;

import com.zyc.notscan.base.BaseEtlMoreTaskMapper;
import com.zyc.zdh.entity.EtlMoreTaskInfo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.sql.Timestamp;
import java.util.List;

/**
 * ClassName: DataSourcesMapper
 * @author zyc-admin
 * @date 2017年12月26日  
 * @Description: TODO  
 */
public interface EtlMoreTaskMapper extends BaseEtlMoreTaskMapper<EtlMoreTaskInfo> {

    @Update(
            {
                    "<script>",
                    "update etl_more_task_info set is_delete=1 ,update_time= #{update_time} where id in ",
                    "<foreach collection='ids' item='id' open='(' separator=',' close=')'>",
                    "#{id}",
                    "</foreach>",
                    "</script>"
            }
    )
    public int deleteBatchById(@Param("ids") String[] ids, @Param("update_time") Timestamp update_time);

    @Select({"<script>",
            "SELECT * FROM etl_more_task_info",
            "WHERE owner=#{owner}",
            "<when test='etl_context!=null and etl_context !=\"\"'>",
            "AND (etl_context like #{etl_context}",
            "OR ID like #{etl_context}",
            "OR etl_ids like #{etl_context}",
            "OR etl_sql like #{etl_context}",
            "OR data_sources_choose_output like #{etl_context}",
            "OR data_source_type_output like #{etl_context}",
            "OR data_sources_params_output like #{etl_context}",
            "OR data_sources_clear_output like #{etl_context} )",
            "</when>",
            "<when test='file_name!=null and file_name !=\"\"'>",
            "AND ( data_sources_file_name_output like #{file_name}",
            "OR data_sources_table_name_output like #{file_name} )",
            "</when>",
            " and is_delete=0",
            "</script>"})
    public List<EtlMoreTaskInfo> selectByParams(@Param("owner") String owner, @Param("etl_context") String etl_context,
                                                @Param("file_name") String file_name);
}
