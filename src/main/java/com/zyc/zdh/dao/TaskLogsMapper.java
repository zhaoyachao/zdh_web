package com.zyc.zdh.dao;

import com.zyc.notscan.BaseMapper;
import com.zyc.zdh.entity.EmailTaskLogs;
import com.zyc.zdh.entity.EtlEcharts;
import com.zyc.zdh.entity.QuartzJobInfo;
import com.zyc.zdh.entity.TaskLogs;
import org.apache.ibatis.annotations.*;

import java.sql.Timestamp;
import java.util.List;


public interface TaskLogsMapper extends BaseMapper<TaskLogs> {

    @Update(value = "update task_logs set status=#{status} where job_id=#{job_id} and etl_date=#{etl_date}")
    public int updateByTask(@Param("status") String status,@Param("job_id") String job_id,@Param("etl_date") String etl_date);

    @Select(value = "select etl_date,sum(case status when 'running' then 1 else 0 end) as \"running\",\n" +
            "sum(case status when 'error' then 1 else 0 end) as \"error\",\n" +
            "sum(case status when 'finish' then 1 else 0 end) as \"finish\"\n" +
            "from task_logs where owner=#{owner} and etl_date is not null group by etl_date,status order by etl_date")
    @Results({@Result(column="etl_date",property="etl_date"),
            @Result(column="running",property="running"),
            @Result(column="error",property="error"),
            @Result(column="finish",property="finish")
    })
    public List<EtlEcharts> slectByOwner(@Param("owner") String owner);

    @Select(value = "select etl_date,sum(case status when 'running' then 1 else 0 end) as \"running\",\n" +
            "sum(case status when 'error' then 1 else 0 end) as \"error\",\n" +
            "sum(case status when 'finish' then 1 else 0 end) as \"finish\"\n" +
            "from task_logs where owner=#{owner} and etl_date=#{etl_date} group by etl_date,status")
    @Results({@Result(column="etl_date",property="etl_date"),
            @Result(column="running",property="running"),
            @Result(column="error",property="error"),
            @Result(column="finish",property="finish")
    })
    public List<EtlEcharts> slectByOwnerEtlDate(@Param("owner") String owner,@Param("etl_date") String etl_date);

    @Select({"<script>",
            "SELECT * FROM task_logs",
            "WHERE owner=#{owner}",
            "<when test='start_time!=null'>",
            "<![CDATA[ AND update_time >= #{start_time} ]]>",
            "</when>",
            "<when test='end_time!=null'>",
            "<![CDATA[ AND update_time <= #{end_time} ]]>",
            "</when>",
            "<when test='status!=null and status !=\"\"'>",
            "AND status = #{status}",
            "</when>",
            "</script>"})
    public List<TaskLogs> selectByTaskLogs(@Param("owner") String owner, @Param("start_time") Timestamp start_time,
                                           @Param("end_time") Timestamp end_time,@Param("status") String status);


    @Select("select a.*,b.email,b.user_name as userName,b.phone,b.is_use_email,b.is_use_phone from task_logs a,account_info b where a.status='error' and a.is_notice='false' and (b.is_use_email='on' or b.is_use_phone='on') and a.owner=b.id")
    public List<EmailTaskLogs> selectByStatus();

    @Select("select * from task_logs where owner=#{owner} and etl_date=#{etl_date} and job_id=#{job_id}")
    public List<TaskLogs> selectByIdEtlDate(@Param("owner") String owner,@Param("job_id") String job_id,@Param("etl_date") String etl_date);
}