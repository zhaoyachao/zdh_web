package com.zyc.zdh.dao;

import com.zyc.notscan.base.BaseServerTaskInstanceMappeer;
import com.zyc.zdh.entity.ServerTaskInstance;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface ServerTaskInstanceMappeer extends BaseServerTaskInstanceMappeer<ServerTaskInstance> {


    @Select(
            {
                    "<script>",
                    "select * from server_task_instance",
                    "<when test='templete_id!=null and templete_id !=\"\"'>",
                    "where templete_id = #{templete_id}",
                    "</when>",
                    "order by create_time desc",
                    "</script>"
            }
    )
    public List<ServerTaskInstance> selectByTempleteId(@Param("templete_id") String templete_id);

    @Delete(
            {
                    "<script>",
                    "delete from server_task_instance where id in",
                    "<foreach collection='ids' item='id' open='(' separator=',' close=')'>",
                    "#{id}",
                    "</foreach>",
                    "</script>"
            }
    )
    public int deleteById(@Param("ids") String[] ids);

    @Delete(
            {
                    "<script>",
                    "update server_task_instance set status =#{status} where id = #{id}",
                    "</script>"
            }
    )
    public int updateStatusById(@Param("id") String id,@Param("status") String status);

}