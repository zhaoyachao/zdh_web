package com.zyc.zdh.dao;

import com.zyc.notscan.BaseMapper;
import com.zyc.zdh.entity.EtlTaskInfo;
import com.zyc.zdh.entity.QuotaInfo;
import com.zyc.zdh.entity.SqlTaskInfo;
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
public interface SqlTaskMapper extends BaseMapper<SqlTaskInfo> {

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
    public List<SqlTaskInfo> selectByIds(@Param("ids") String[] ids);

    @Select({"<script>",
            "SELECT * FROM sql_task_info",
            "WHERE owner=#{owner}",
            "<when test='sql_context!=null and sql_context !=\"\"'>",
            "AND sql_context like '%${sql_context}%'",
            "</when>",
            "<when test='id!=null and id !=\"\"'>",
            "AND id = #{id}",
            "</when>",
            "</script>"})
    public List<SqlTaskInfo> selectByParams(@Param("owner") String owner, @Param("sql_context") String sql_context,
                                            @Param("id") String id);




}
