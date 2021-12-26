package com.zyc.zdh.dao;

import com.zyc.notscan.BaseMapper;
import com.zyc.zdh.entity.EtlTaskInfo;
import com.zyc.zdh.entity.QuotaInfo;
import org.apache.ibatis.annotations.Delete;
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
public interface EtlTaskMapper extends BaseMapper<EtlTaskInfo> {


    @Update(
            {
                    "<script>",
                    "update etl_task_info set is_delete=1 ,update_time= #{update_time} where id in ",
                    "<foreach collection='ids' item='id' open='(' separator=',' close=')'>",
                    "#{id}",
                    "</foreach>",
                    "</script>"
            }
    )
    public int deleteBatchById(@Param("ids") String[] ids, @Param("update_time") Timestamp update_time);

    @Select({
            "<script>",
            "select",
            "*",
            "from etl_task_info",
            "where id in",
            "<foreach collection='ids' item='id' open='(' separator=',' close=')'>",
            "#{id}",
            "</foreach>",
            " and is_delete=0",
            "</script>"
    })
    public List<EtlTaskInfo> selectByIds(@Param("ids") String[] ids);

    @Select({"<script>",
            "SELECT * FROM etl_task_info",
            "WHERE owner=#{owner}",
            "<when test='etl_context!=null and etl_context !=\"\"'>",
            "AND (etl_context like '%${etl_context}%'",
            "OR ID like '%${etl_context}%'",
            "OR data_sources_choose_input like '%${etl_context}%'",
            "OR data_source_type_input like '%${etl_context}%'",
            "OR data_sources_params_input like '%${etl_context}%'",
            "OR data_sources_filter_input like '%${etl_context}%'",
            "OR data_sources_choose_output like '%${etl_context}%'",
            "OR data_source_type_output like '%${etl_context}%'",
            "OR data_sources_params_output like '%${etl_context}%'",
            "OR data_sources_clear_output like '%${etl_context}%'",
            "OR column_datas like '%${etl_context}%' )",
            "</when>",
            "<when test='file_name!=null and file_name !=\"\"'>",
            "AND ( data_sources_table_name_input like '%${file_name}%'",
            "OR data_sources_file_name_input like '%${file_name}%'",
            "OR data_sources_file_name_output like '%${file_name}%'",
            "OR data_sources_table_name_output like '%${file_name}%' )",
            "</when>",
            " and is_delete=0",
            "</script>"})
    public List<EtlTaskInfo> selectByParams(@Param("owner") String owner,@Param("etl_context") String etl_context,
                                            @Param("file_name") String file_name);

    @Select({"<script>",
            "SELECT id,etl_context,data_sources_choose_output,data_source_type_output,create_time,case data_sources_table_name_output when '' then data_sources_file_name_output else data_sources_table_name_output end as path FROM etl_task_info",
            "WHERE owner=#{owner}",
            "<when test='column_desc!=null and column_desc !=\"\"'>",
            "AND column_datas like '%${column_desc}%'",
            "</when>",
            "<when test='column_alias!=null and column_alias !=\"\"'>",
            "AND column_datas like '%${column_alias}%'",
            "</when>",
            "<when test='company!=null and company !=\"\"'>",
            "AND company like '%${company}%'",
            "</when>",
            "<when test='section!=null and section !=\"\"'>",
            "AND section like '%${section}%'",
            "</when>",
            "<when test='service!=null and service !=\"\"'>",
            "AND service like '%${service}%'",
            "</when>",
            " and is_delete=0",
            "</script>"})
    public List<QuotaInfo> selectByColumn(@Param("owner") String owner, @Param("column_desc") String column_desc,
                                          @Param("column_alias") String column_alias,@Param("company")String company,
                                          @Param("section") String section,@Param("service")String service);


}
