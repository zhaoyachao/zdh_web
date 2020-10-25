package com.zyc.zdh.dao;

import com.zyc.notscan.BaseMapper;
import com.zyc.zdh.entity.EmailTaskLogs;
import com.zyc.zdh.entity.EtlEcharts;
import com.zyc.zdh.entity.TaskLogInstance;
import org.apache.ibatis.annotations.*;

import java.sql.Timestamp;
import java.util.List;


public interface TaskLogInstanceMapper extends BaseMapper<TaskLogInstance> {


    @Update(value = "update task_log_instance set status=#{status} where id=#{id}")
    public int updateStatusById(@Param("status") String status, @Param("id") String id);

    @Update(value = "update task_log_instance set thread_id=#{thread_id} where id=#{id}")
    public int updateThreadById(@Param("thread_id") String thread_id, @Param("id") String id);

    @Update(value = "update task_log_instance set status=#{status} where id=#{id} and (status='dispatch' or status ='etl')")
    public int updateStatusById2(@Param("status") String status,@Param("id") String id);

    @Update(value = "update task_log_instance set is_notice=#{is_notice} where id=#{id}")
    public int updateNoticeById(@Param("is_notice") String is_notice,@Param("id") String id);

    @Select(value = "select * from (select etl_date,sum(case status when 'running' then 1 else 0 end) as \"running\",\n" +
            "sum(case status when 'error' then 1 else 0 end) as \"error\",\n" +
            "sum(case status when 'finish' then 1 else 0 end) as \"finish\"\n" +
            "from task_log_instance where owner=#{owner} and etl_date is not null group by etl_date,status order by etl_date desc limit 15) t order by etl_date asc")
    @Results({@Result(column="etl_date",property="etl_date"),
            @Result(column="running",property="running"),
            @Result(column="error",property="error"),
            @Result(column="finish",property="finish")
    })
    public List<EtlEcharts> slectByOwner(@Param("owner") String owner);

    @Select(value = "select etl_date,sum(case status when 'running' then 1 else 0 end) as \"running\",\n" +
            "sum(case status when 'error' then 1 else 0 end) as \"error\",\n" +
            "sum(case status when 'finish' then 1 else 0 end) as \"finish\"\n" +
            "from task_log_instance where owner=#{owner} and etl_date=#{etl_date} group by etl_date,status")
    @Results({@Result(column="etl_date",property="etl_date"),
            @Result(column="running",property="running"),
            @Result(column="error",property="error"),
            @Result(column="finish",property="finish")
    })
    public List<EtlEcharts> slectByOwnerEtlDate(@Param("owner") String owner, @Param("etl_date") String etl_date);

    @Select({"<script>",
            "SELECT * FROM task_log_instance",
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
            "order by run_time desc",
            "</script>"})
    public List<TaskLogInstance> selectByTaskLogs(@Param("owner") String owner, @Param("start_time") Timestamp start_time,
                                                  @Param("end_time") Timestamp end_time, @Param("status") String status);

    @Select({"<script>",
            "SELECT * FROM task_log_instance",
            "WHERE owner=#{owner} and job_id=#{job_id}",
            "<when test='start_time!=null'>",
            "<![CDATA[ AND update_time >= #{start_time} ]]>",
            "</when>",
            "<when test='end_time!=null'>",
            "<![CDATA[ AND update_time <= #{end_time} ]]>",
            "</when>",
            "<when test='status!=null and status !=\"\"'>",
            "AND status = #{status}",
            "</when>",
            "order by run_time desc",
            "</script>"})
    public List<TaskLogInstance> selectByTaskLogs2(@Param("owner") String owner, @Param("start_time") Timestamp start_time,
                                                   @Param("end_time") Timestamp end_time, @Param("status") String status,@Param("job_id") String job_id);


    /**
     * 获取超时任务
     * @return
     */
    @Select({"<script>",
            "SELECT * FROM task_log_instance",
            "WHERE alarm_enabled='on' and alarm_account is not null and alarm_account != '' and time_out != null and time_out > '0' and timestampdiff(second,run_time,current_timestamp()) >= time_out",
            " and is_notice != 'alarm' and status in ('etl','dispatch') ",
            "</script>"})
    public List<TaskLogInstance> selectOverTime();

    @Select("select a.*,b.email,b.user_name as userName,b.phone,b.is_use_email,b.is_use_phone from task_log_instance a,account_info b where a.status='error' and a.is_notice !='true' and a.owner=b.id")
    public List<EmailTaskLogs> selectByStatus();

    @Select("select * from task_log_instance where owner=#{owner} and etl_date=#{etl_date} and job_id=#{job_id}")
    public List<TaskLogInstance> selectByIdEtlDate(@Param("owner") String owner, @Param("job_id") String job_id, @Param("etl_date") String etl_date);

    @Select("select * from task_log_instance where status =#{status}")
    public List<TaskLogInstance> selectThreadByStatus(@Param("status") String status);


    @Select("select * from task_log_instance where status =#{status} and retry_time is not null and current_timestamp() >= retry_time")
    public List<TaskLogInstance> selectThreadByStatus2(@Param("status") String status);

    @Select("select * from task_log_instance where status =#{status}")
    public List<TaskLogInstance> selectThreadByStatus3(@Param("status") String status);

    @Update(value = "update task_log_instance set status=#{status} where id=#{id} and status='running' and process > #{process}")
    public int updateStatusById3(@Param("status") String status, @Param("process") String process, @Param("id") String id);

    @Update({
            "<script>",
            "update task_logs ",
            "set job_id = #{job_id}",
            "<when test='job_context!=null and job_context !=\"\"'>",
            ", job_context = #{job_context}",
            "</when>",
            "<when test='etl_date!=null and etl_date !=\"\"'>",
            ", etl_date = #{etl_date}",
            "</when>",
            "<when test='status!=null and status !=\"\"'>",
            ", status = #{status}",
            "</when>",
            "<when test='start_time!=null'>",
            ", start_time = #{start_time}",
            "</when>",
            "<when test='update_time!=null'>",
            ", update_time = #{update_time}",
            "</when>",
            "<when test='owner!=null and owner !=\"\"'>",
            ", owner = #{owner}",
            "</when>",
            "<when test='is_notice!=null and is_notice !=\"\"'>",
            ", is_notice = #{is_notice}",
            "</when>",
            "<when test='process!=null and process !=\"\"'>",
            ", process = #{process}",
            "</when>",
            "<when test='thread_id!=null and thread_id !=\"\"'>",
            ", thread_id = #{thread_id}",
            "</when>",
            "<when test='retry_time!=null'>",
            ", retry_time = #{retry_time}",
            "</when>",
            "<when test='executor!=null and executor !=\"\"'>",
            ", executor = #{executor}",
            "</when>",
            "<when test='etl_info!=null and etl_info !=\"\"'>",
            ", etl_info = #{etl_info}",
            "</when>",
            "<when test='url!=null and url !=\"\"'>",
            ", url = #{url}",
            "</when>",
            "<when test='application_id!=null and application_id !=\"\"'>",
            ", application_id = #{application_id}",
            "</when>",
            "<when test='history_server!=null and history_server !=\"\"'>",
            ", history_server = #{history_server}",
            "</when>",
            "<when test='master!=null and master !=\"\"'>",
            ", master = #{master}",
            "</when>",
            "where id= #{id} <![CDATA[ AND process <= #{process} ]]> and server_ack='0'",
            "</script>"
    })
    public int updateTaskLogsById3(TaskLogInstance tli);
}