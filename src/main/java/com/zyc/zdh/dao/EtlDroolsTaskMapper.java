package com.zyc.zdh.dao;

import com.zyc.notscan.base.BaseEtlDroolsTaskMapper;
import com.zyc.zdh.entity.EtlDroolsTaskInfo;
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
public interface EtlDroolsTaskMapper extends BaseEtlDroolsTaskMapper<EtlDroolsTaskInfo> {

    @Delete("delete from etl_drools_task_info where id = #{ids_str}")
    public int deleteBatchById(@Param("ids_str") String ids_str);

    @Select({"<script>",
            "SELECT * FROM etl_drools_task_info",
            "WHERE owner=#{owner}",
            "<when test='etl_context!=null and etl_context !=\"\"'>",
            "AND ( etl_context like #{etl_context}",
            "OR ID like #{etl_context}",
            "OR etl_drools like #{etl_context}",
            "OR data_sources_filter_input like #{etl_context}",
            "OR more_task like #{etl_context}",
            "OR drop_tmp_tables like #{etl_context}",
            "OR data_sources_choose_output like #{etl_context}",
            "OR data_source_type_output like #{etl_context}",
            "OR data_sources_table_name_output like #{etl_context}",
            "OR data_sources_file_name_output like #{etl_context}",
            "OR data_sources_params_output like #{etl_context}",
            "OR data_sources_clear_output like #{etl_context} )",
            "</when>",
            "<when test='file_name!=null and file_name !=\"\"'>",
            "AND ( data_sources_file_name_output like #{file_name}",
            "OR data_sources_table_name_output like #{file_name} )",
            "</when>",
            "</script>"})
    public List<EtlDroolsTaskInfo> selectByParams(@Param("owner") String owner, @Param("etl_context") String etl_context,
                                                @Param("file_name") String file_name);
}
