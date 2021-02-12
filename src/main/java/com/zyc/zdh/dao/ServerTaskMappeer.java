package com.zyc.zdh.dao;

import com.zyc.notscan.BaseMapper;
import com.zyc.zdh.entity.ServerTaskInfo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface ServerTaskMappeer extends BaseMapper<ServerTaskInfo> {

    @Select(
            {
                    "<script>",
                    "select * from server_task_info where 1=1",
                    "<when test='id!=null and id !=\"\"'>",
                    "AND id = #{id}",
                    "</when>",
                    "<when test='context!=null and context !=\"\"'>",
                    "AND (build_task like  '%${context}%' ",
                    " OR git_url like  '%${context}%' ",
                    " OR build_type like  '%${context}%' ",
                    " OR build_ip like  '%${context}%' ",
                    " OR remote_ip like  '%${context}%' )",
                    "</when>",
                    "</script>"
            }
    )
    public List<ServerTaskInfo> selectServer(@Param("id") String id, @Param("context") String context);

}