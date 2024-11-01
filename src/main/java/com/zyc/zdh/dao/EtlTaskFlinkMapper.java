package com.zyc.zdh.dao;

import com.zyc.notscan.base.BaseEtlTaskFlinkMapper;
import com.zyc.zdh.entity.EtlTaskFlinkInfo;
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
public interface EtlTaskFlinkMapper extends BaseEtlTaskFlinkMapper<EtlTaskFlinkInfo> {

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
            "WHERE is_delete=0",
            "<when test='sql_context!=null and sql_context !=\"\"'>",
            "AND ( sql_context like #{sql_context}",
            "OR ID like #{sql_context}",
            "OR etl_sql like #{sql_context} )",
            "</when>",
            "<when test='id!=null and id !=\"\"'>",
            "AND id = #{id}",
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
    public List<EtlTaskFlinkInfo> selectByParams(@Param("owner") String owner, @Param("sql_context") String sql_context,
                                            @Param("id") String id,
                                                 @Param("product_code") String product_code, @Param("dim_group") String dim_group,
                                                 @Param("product_codes") List<String> product_codes,
                                                 @Param("dim_groups") List<String> dim_groups);




}
