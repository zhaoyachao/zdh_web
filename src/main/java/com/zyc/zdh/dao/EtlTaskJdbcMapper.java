package com.zyc.zdh.dao;

import com.zyc.notscan.BaseMapper;
import com.zyc.zdh.entity.EtlTaskJdbcInfo;
import com.zyc.zdh.entity.SqlTaskInfo;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * ClassName: EtlTaskJdbcMapper
 * @author zyc-admin
 * @date 2021年11月27日
 * @Description: TODO  
 */
public interface EtlTaskJdbcMapper extends BaseMapper<EtlTaskJdbcInfo> {

    @Delete("delete from etl_task_jdbc_info where id = #{ids_str}")
    public int deleteBatchById(@Param("ids_str") String ids_str);

    @Select({
            "<script>",
            "select",
            "*",
            "from etl_task_jdbc_info",
            "where id in",
            "<foreach collection='ids' item='id' open='(' separator=',' close=')'>",
            "#{id}",
            "</foreach>",
            "</script>"
    })
    public List<SqlTaskInfo> selectByIds(@Param("ids") String[] ids);

    @Select({"<script>",
            "SELECT * FROM etl_task_jdbc_info",
            "WHERE owner=#{owner}",
            "<when test='etl_context!=null and etl_context !=\"\"'>",
            "AND ( etl_context like '%${etl_context}%'",
            "OR ID like '%${etl_context}%'",
            "OR data_sources_choose_output like '%${etl_context}%'",
            "OR data_source_type_output like '%${etl_context}%'",
            "OR data_sources_table_name_output like '%${etl_context}%'",
            "OR data_sources_file_name_output like '%${etl_context}%'",
            "OR data_sources_params_output like '%${etl_context}%'",
            "OR data_sources_clear_output like '%${etl_context}%'",
            "OR etl_sql like '%${etl_context}%' )",
            "</when>",
            "<when test='id!=null and id !=\"\"'>",
            "AND id = #{id}",
            "</when>",
            "</script>"})
    public List<EtlTaskJdbcInfo> selectByParams(@Param("owner") String owner, @Param("etl_context") String etl_context,
                                            @Param("id") String id);




}
