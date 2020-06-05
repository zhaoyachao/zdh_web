package com.zyc.zdh.dao;

import com.zyc.notscan.BaseMapper;
import com.zyc.zdh.entity.EmailTaskLogs;
import com.zyc.zdh.entity.EtlEcharts;
import com.zyc.zdh.entity.Quality;
import com.zyc.zdh.entity.TaskLogs;
import org.apache.ibatis.annotations.*;

import java.sql.Timestamp;
import java.util.List;


public interface QualityMapper extends BaseMapper<Quality> {

    @Select(
            {"<script>",
             "select a.*,b.job_context,c.etl_context from quality a left join quartz_job_info b on a.owner=#{owner} and " +
                     "b.owner=#{owner} and a.dispatch_task_id=b.job_id " +
                     "left join etl_task_info c on c.owner=#{owner} and a.etl_task_id=c.id where a.owner=#{owner}",
            "<when test='status!=null and status !=\"\"'>",
            "AND a.status = #{status}",
            "</when>",
            "<when test='job_context!=null and job_context !=\"\"'>",
            "AND b.job_context like  '%${job_context}%'",
            "</when>",
            "<when test='etl_context!=null and etl_context !=\"\"'>",
            "AND c.etl_context like  '%${etl_context}%'",
            "</when>",
            "</script>",
            })
    public List<Quality> selectByOwner(@Param("owner") String owner,@Param("job_context")String job_context,@Param("etl_context")String etl_context,@Param("status")String status);

}