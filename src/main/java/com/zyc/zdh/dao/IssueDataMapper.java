package com.zyc.zdh.dao;

import com.zyc.notscan.BaseMapper;
import com.zyc.zdh.entity.EtlTaskInfo;
import com.zyc.zdh.entity.IssueDataInfo;
import com.zyc.zdh.entity.QuotaInfo;
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
public interface IssueDataMapper extends BaseMapper<IssueDataInfo> {

    @Delete("delete from etl_task_info where id = #{ids_str}")
    public int deleteBatchById(@Param("ids_str") String ids_str);

    @Select({
            "<script>",
            "select",
            "*",
            "from etl_task_info",
            "where id in",
            "<foreach collection='ids' item='id' open='(' separator=',' close=')'>",
            "#{id}",
            "</foreach>",
            "</script>"
    })
    public List<EtlTaskInfo> selectByIds(@Param("ids") String[] ids);

    @Select({"<script>",
            "SELECT * FROM issue_data_info",
            "WHERE 1=1",
            "<when test='issue_context!=null and issue_context !=\"\"'>",
            "AND ( issue_context like '%${issue_context}%'",
            "OR data_sources_table_name_input like '%${issue_context}%')",
            "</when>",
            "</script>"})
    public List<IssueDataInfo> selectByParams(@Param("issue_context") String issue_context);




}
