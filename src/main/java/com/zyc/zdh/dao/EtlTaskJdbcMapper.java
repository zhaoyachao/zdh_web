package com.zyc.zdh.dao;

import com.zyc.notscan.base.BaseEtlTaskJdbcMapper;
import com.zyc.zdh.entity.EtlTaskJdbcInfo;
import com.zyc.zdh.entity.SqlTaskInfo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.sql.Timestamp;
import java.util.List;

/**
 * ClassName: EtlTaskJdbcMapper
 * @author zyc-admin
 * @date 2021年11月27日
 * @Description: TODO  
 */
public interface EtlTaskJdbcMapper extends BaseEtlTaskJdbcMapper<EtlTaskJdbcInfo> {

    @Update(
            {
                    "<script>",
                    "update etl_task_jdbc_info set is_delete=1 ,update_time= #{update_time} where id in ",
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
            "from etl_task_jdbc_info",
            "where id in",
            "<foreach collection='ids' item='id' open='(' separator=',' close=')'>",
            "#{id}",
            "</foreach>",
            "and is_delete=0",
            "</script>"
    })
    public List<SqlTaskInfo> selectByIds(@Param("ids") String[] ids);

    @Select({"<script>",
            "SELECT * FROM etl_task_jdbc_info",
            "WHERE is_delete=0",
            "<when test='etl_context!=null and etl_context !=\"\"'>",
            "AND ( etl_context like #{etl_context}",
            "OR ID like #{etl_context}",
            "OR data_sources_choose_output like #{etl_context}",
            "OR data_source_type_output like #{etl_context}",
            "OR data_sources_table_name_output like #{etl_context}",
            "OR data_sources_file_name_output like #{etl_context}",
            "OR data_sources_params_output like #{etl_context}",
            "OR data_sources_clear_output like #{etl_context}",
            "OR etl_sql like #{etl_context} )",
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
    public List<EtlTaskJdbcInfo> selectByParams(@Param("owner") String owner, @Param("etl_context") String etl_context,
                                            @Param("id") String id,
                                                @Param("product_code") String product_code, @Param("dim_group") String dim_group,
                                                @Param("product_codes") List<String> product_codes,
                                                @Param("dim_groups") List<String> dim_groups);




}
