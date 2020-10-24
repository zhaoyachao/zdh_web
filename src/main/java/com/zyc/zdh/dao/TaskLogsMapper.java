//package com.zyc.zdh.dao;
//
//import com.zyc.notscan.BaseMapper;
//import com.zyc.zdh.entity.EmailTaskLogs;
//import com.zyc.zdh.entity.EtlEcharts;
//import com.zyc.zdh.entity.QuartzJobInfo;
//import com.zyc.zdh.entity.TaskLogs;
//import org.apache.ibatis.annotations.*;
//
//import java.sql.Timestamp;
//import java.util.List;
//
//
//public interface TaskLogsMapper extends BaseMapper<TaskLogs> {
//
//    @Update(value = "update task_logs set status=#{status} where job_id=#{job_id} and etl_date=#{etl_date}")
//    public int updateByTask(@Param("status") String status,@Param("job_id") String job_id,@Param("etl_date") String etl_date);
//
//    @Update(value = "update task_logs set status=#{status} where id=#{id}")
//    public int updateStatusById(@Param("status") String status,@Param("id") String id);
//
//    @Update(value = "update task_logs set status=#{status} where id=#{id} and status='running'")
//    public int updateStatusById2(@Param("status") String status,@Param("id") String id);
//
//
//    @Select(value = "select * from (select etl_date,sum(case status when 'running' then 1 else 0 end) as \"running\",\n" +
//            "sum(case status when 'error' then 1 else 0 end) as \"error\",\n" +
//            "sum(case status when 'finish' then 1 else 0 end) as \"finish\"\n" +
//            "from task_logs where owner=#{owner} and etl_date is not null group by etl_date,status order by etl_date desc limit 15) t order by etl_date asc")
//    @Results({@Result(column="etl_date",property="etl_date"),
//            @Result(column="running",property="running"),
//            @Result(column="error",property="error"),
//            @Result(column="finish",property="finish")
//    })
//    public List<EtlEcharts> slectByOwner(@Param("owner") String owner);
//
//    @Select(value = "select etl_date,sum(case status when 'running' then 1 else 0 end) as \"running\",\n" +
//            "sum(case status when 'error' then 1 else 0 end) as \"error\",\n" +
//            "sum(case status when 'finish' then 1 else 0 end) as \"finish\"\n" +
//            "from task_logs where owner=#{owner} and etl_date=#{etl_date} group by etl_date,status")
//    @Results({@Result(column="etl_date",property="etl_date"),
//            @Result(column="running",property="running"),
//            @Result(column="error",property="error"),
//            @Result(column="finish",property="finish")
//    })
//    public List<EtlEcharts> slectByOwnerEtlDate(@Param("owner") String owner,@Param("etl_date") String etl_date);
//
//    @Select({"<script>",
//            "SELECT * FROM task_logs",
//            "WHERE owner=#{owner}",
//            "<when test='start_time!=null'>",
//            "<![CDATA[ AND update_time >= #{start_time} ]]>",
//            "</when>",
//            "<when test='end_time!=null'>",
//            "<![CDATA[ AND update_time <= #{end_time} ]]>",
//            "</when>",
//            "<when test='status!=null and status !=\"\"'>",
//            "AND status = #{status}",
//            "</when>",
//            "order by start_time desc",
//            "</script>"})
//    public List<TaskLogs> selectByTaskLogs(@Param("owner") String owner, @Param("start_time") Timestamp start_time,
//                                           @Param("end_time") Timestamp end_time,@Param("status") String status);
//
//
//    @Select("select a.*,b.email,b.user_name as userName,b.phone,b.is_use_email,b.is_use_phone from task_logs a,account_info b where a.status='error' and a.is_notice='false' and a.owner=b.id")
//    public List<EmailTaskLogs> selectByStatus();
//
//    @Select("select * from task_logs where owner=#{owner} and etl_date=#{etl_date} and job_id=#{job_id}")
//    public List<TaskLogs> selectByIdEtlDate(@Param("owner") String owner,@Param("job_id") String job_id,@Param("etl_date") String etl_date);
//
//    @Select("select * from task_logs where status =#{status}")
//    public List<TaskLogs> selectThreadByStatus(@Param("status") String status);
//
//
//    @Select("select * from task_logs where status =#{status} and retry_time is not null and current_timestamp() >= retry_time")
//    public List<TaskLogs> selectThreadByStatus2(@Param("status") String status);
//
//    @Select("select * from task_logs where status =#{status}")
//    public List<TaskLogs> selectThreadByStatus3(@Param("status") String status);
//
//    @Update(value = "update task_logs set status=#{status} where id=#{id} and status='running' and process > #{process}")
//    public int updateStatusById3(@Param("status") String status,@Param("process") String process,@Param("id") String id);
//
//    @Update({
//            "<script>",
//            "update task_logs ",
//            "set job_id = #{job_id}",
//            "<when test='job_context!=null and job_context !=\"\"'>",
//            ", job_context = #{job_context}",
//            "</when>",
//            "<when test='etl_date!=null and etl_date !=\"\"'>",
//            ", etl_date = #{etl_date}",
//            "</when>",
//            "<when test='status!=null and status !=\"\"'>",
//            ", status = #{status}",
//            "</when>",
//            "<when test='start_time!=null'>",
//            ", start_time = #{start_time}",
//            "</when>",
//            "<when test='update_time!=null'>",
//            ", update_time = #{update_time}",
//            "</when>",
//            "<when test='owner!=null and owner !=\"\"'>",
//            ", owner = #{owner}",
//            "</when>",
//            "<when test='is_notice!=null and is_notice !=\"\"'>",
//            ", is_notice = #{is_notice}",
//            "</when>",
//            "<when test='process!=null and process !=\"\"'>",
//            ", process = #{process}",
//            "</when>",
//            "<when test='thread_id!=null and thread_id !=\"\"'>",
//            ", thread_id = #{thread_id}",
//            "</when>",
//            "<when test='retry_time!=null'>",
//            ", retry_time = #{retry_time}",
//            "</when>",
//            "<when test='executor!=null and executor !=\"\"'>",
//            ", executor = #{executor}",
//            "</when>",
//            "<when test='etl_info!=null and etl_info !=\"\"'>",
//            ", etl_info = #{etl_info}",
//            "</when>",
//            "<when test='url!=null and url !=\"\"'>",
//            ", url = #{url}",
//            "</when>",
//            "<when test='application_id!=null and application_id !=\"\"'>",
//            ", application_id = #{application_id}",
//            "</when>",
//            "<when test='history_server!=null and history_server !=\"\"'>",
//            ", history_server = #{history_server}",
//            "</when>",
//            "<when test='master!=null and master !=\"\"'>",
//            ", master = #{master}",
//            "</when>",
//            "where id= #{id} <![CDATA[ AND process <= #{process} ]]> and server_ack='0'",
//            "</script>"
//    })
//    public int updateTaskLogsById3(TaskLogs taskLogs);
//}