package com.zyc.zdh.dao;

import com.zyc.notscan.BaseMapper;
import com.zyc.zdh.entity.EtlTaskFlinkInfo;
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
public interface EtlTaskFlinkMapper extends BaseMapper<EtlTaskFlinkInfo> {

    @Update(
            {
                    "<script>",
                    "update etl_task_flink_info set is_delete=1 ,update_time= #{update_time} where id in ",
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
            "from etl_task_flink_info",
            "where id in",
            "<foreach collection='ids' item='id' open='(' separator=',' close=')'>",
            "#{id}",
            "</foreach>",
            "and is_delete=0",
            "</script>"
    })
    public List<EtlTaskFlinkInfo> selectByIds(@Param("ids") String[] ids);

    @Select({"<script>",
            "SELECT * FROM etl_task_flink_info",
            "WHERE owner=#{owner}",
            "<when test='sql_context!=null and sql_context !=\"\"'>",
            "AND ( sql_context like '%${sql_context}%'",
            "OR ID like '%${sql_context}%'",
            "OR etl_sql like '%${sql_context}%' )",
            "</when>",
            "<when test='id!=null and id !=\"\"'>",
            "AND id = #{id}",
            "</when>",
            "and is_delete=0",
            "</script>"})
    public List<EtlTaskFlinkInfo> selectByParams(@Param("owner") String owner, @Param("sql_context") String sql_context,
                                            @Param("id") String id);




}
