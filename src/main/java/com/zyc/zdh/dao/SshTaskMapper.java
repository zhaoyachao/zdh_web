package com.zyc.zdh.dao;

import com.zyc.notscan.BaseMapper;
import com.zyc.zdh.entity.SqlTaskInfo;
import com.zyc.zdh.entity.SshTaskInfo;
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
public interface SshTaskMapper extends BaseMapper<SshTaskInfo> {

    @Delete("delete from ssh_task_info where id = #{ids_str}")
    public int deleteBatchById(@Param("ids_str") String ids_str);

    @Select({
            "<script>",
            "select",
            "*",
            "from ssh_task_info",
            "where id in",
            "<foreach collection='ids' item='id' open='(' separator=',' close=')'>",
            "#{id}",
            "</foreach>",
            "</script>"
    })
    public List<SshTaskInfo> selectByIds(@Param("ids") String[] ids);

    @Select({"<script>",
            "SELECT * FROM ssh_task_info",
            "WHERE owner=#{owner}",
            "<when test='ssh_context!=null and ssh_context !=\"\"'>",
            "AND ( ssh_context like '%${ssh_context}%'",
            "OR ID like '%${ssh_context}%'",
            "OR ssh_params_input like '%${ssh_context}%'",
            "OR ssh_cmd like '%${ssh_context}%'",
            "OR ssh_script_path like '%${ssh_context}%'",
            "OR ssh_script_context like '%${ssh_context}%'",
            "OR host like '%${ssh_context}%'",
            "OR user_name like '%${ssh_context}%' )",
            "</when>",
            "<when test='id!=null and id !=\"\"'>",
            "AND id = #{id}",
            "</when>",
            "</script>"})
    public List<SshTaskInfo> selectByParams(@Param("owner") String owner, @Param("ssh_context") String ssh_context,
                                            @Param("id") String id);




}
