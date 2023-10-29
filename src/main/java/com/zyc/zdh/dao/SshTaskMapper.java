package com.zyc.zdh.dao;

import com.zyc.notscan.base.BaseSshTaskMapper;
import com.zyc.zdh.entity.SshTaskInfo;
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
public interface SshTaskMapper extends BaseSshTaskMapper<SshTaskInfo> {

    @Update(
            {
                    "<script>",
                    "update ssh_task_info set is_delete=1 ,update_time= #{update_time} where id in ",
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
            "from ssh_task_info",
            "where id in",
            "<foreach collection='ids' item='id' open='(' separator=',' close=')'>",
            "#{id}",
            "</foreach>",
            "and is_delete=0",
            "</script>"
    })
    public List<SshTaskInfo> selectByIds(@Param("ids") String[] ids);

    @Select({"<script>",
            "SELECT * FROM ssh_task_info",
            "WHERE is_delete=0",
            "<when test='ssh_context!=null and ssh_context !=\"\"'>",
            "AND ( ssh_context like #{ssh_context}",
            "OR ID like #{ssh_context}",
            "OR ssh_params_input like #{ssh_context}",
            "OR ssh_cmd like #{ssh_context}",
            "OR ssh_script_path like #{ssh_context}",
            "OR ssh_script_context like #{ssh_context}",
            "OR host like #{ssh_context}",
            "OR user_name like #{ssh_context} )",
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
    public List<SshTaskInfo> selectByParams(@Param("owner") String owner, @Param("ssh_context") String ssh_context,
                                            @Param("id") String id,
                                            @Param("product_code") String product_code, @Param("dim_group") String dim_group,
                                            @Param("product_codes") List<String> product_codes,
                                            @Param("dim_groups") List<String> dim_groups);




}
