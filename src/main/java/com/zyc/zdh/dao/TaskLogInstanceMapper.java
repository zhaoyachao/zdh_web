package com.zyc.zdh.dao;

import com.zyc.notscan.BaseMapper;
import com.zyc.zdh.entity.EmailTaskLogs;
import com.zyc.zdh.entity.EtlEcharts;
import com.zyc.zdh.entity.TaskLogInstance;
import com.zyc.zdh.entity.task_num_info;
import org.apache.ibatis.annotations.*;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;


public interface TaskLogInstanceMapper extends BaseMapper<TaskLogInstance> {


    /**
     * 根据id更新子任务状态
     * @param status
     * @param id
     * @return
     */
    @Update(value = "update task_log_instance set status=#{status} where id=#{id}")
    public int updateStatusById(@Param("status") String status, @Param("id") String id);


    /**
     * 根据id更新子任务的线程id
     * @param thread_id
     * @param id
     * @return
     */
    @Update(value = "update task_log_instance set thread_id=#{thread_id} where id=#{id}")
    public int updateThreadById(@Param("thread_id") String thread_id, @Param("id") String id);

    @Update({
            "<script>",
            "update task_log_instance ",
            " set status= case when `status`='check_dep' or `status`='waite_retry' or (`status`='dispatch' and job_type in ('JDBC','GROUP')) then 'killed' else 'kill' end",
            " where id=#{id} and (status='dispatch' or status ='etl' or status= 'check_dep' or status= 'wait_retry')",
            "</script>",
    })
    public int updateStatusById2(@Param("id") String id);

    @Update(value = "update task_log_instance set status= case when `status` in ('check_dep','wait_retry','check_dep_finish','create') then 'killed' when `status` in ('error','finish') then `status` else 'kill'  end where group_id=#{group_id} and (status != 'error' and status != 'killed')")
    public int updateStatusByGroupId(@Param("group_id") String group_id);

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
            "WHERE owner=#{owner} and group_id=#{group_id}",
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
                                                   @Param("end_time") Timestamp end_time, @Param("status") String status,@Param("group_id") String group_id);


    /**
     * 获取超时任务
     * @return
     */
    @Select({"<script>",
            "SELECT * FROM task_log_instance",
            "WHERE alarm_enabled='on' and alarm_account is not null and alarm_account != '' and time_out is not null and time_out > '0' and timestampdiff(second,run_time,current_timestamp()) >= time_out",
            " and is_notice != 'alarm' and status in ('etl','dispatch') ",
            "</script>"})
    public List<TaskLogInstance> selectOverTime();

    @Select({"<script>",
            "SELECT * FROM task_log_instance",
            "WHERE ",
            " is_notice = 'alarm' and status ='finish'",
            "</script>"})
    public List<TaskLogInstance> selectOverTimeFinish();

    @Select("select a.*,b.email,b.user_name as userName,b.phone,b.is_use_email,b.is_use_phone from task_log_instance a,account_info b where a.status='error' and a.is_notice !='true' and a.owner=b.id")
    public List<EmailTaskLogs> selectByStatus();

    @Select("select * from task_log_instance where etl_date=#{etl_date} and job_id=#{job_id}")
    public List<TaskLogInstance> selectByIdEtlDate(@Param("job_id") String job_id, @Param("etl_date") String etl_date);

    @Select("select * from task_log_instance where status =#{status}")
    public List<TaskLogInstance> selectThreadByStatus(@Param("status") String status);

    /**
     * 获取所有可以执行的子任务
     * @param status
     * @return
     */
    @Select(
            {
            "<script>",
            "select tli.* from task_log_instance tli,task_group_log_instance tgli where tli.status in",
            "<foreach collection='status' item='st' open='(' separator=',' close=')'>",
            "#{st}",
            "</foreach>",
            " and tgli.status = 'sub_task_dispatch'",
            " and tli.group_id=tgli.id",
            "</script>"
            }
    )
    public List<TaskLogInstance> selectThreadByStatus1(@Param("status") String[] status);

    /**
     * 获取所有可以执行的JDBC子任务
     * @param status
     * @return
     */
    @Select(
            {
                    "<script>",
                    "select tli.* from task_log_instance tli,task_group_log_instance tgli where tli.status in",
                    "<foreach collection='status' item='st' open='(' separator=',' close=')'>",
                    "#{st}",
                    "</foreach>",
                    " and tgli.status = 'sub_task_dispatch'",
                    " and tli.group_id=tgli.id",
                    " and tli.job_type in ",
                    "<foreach collection='job_types' item='job_type' open='(' separator=',' close=')'>",
                    "#{job_type}",
                    "</foreach>",
                    "</script>"
            }
    )
    public List<TaskLogInstance> selectTaskByJobType(@Param("status") String[] status,@Param("job_types") String[] job_types);


    @Select("select * from task_log_instance where status =#{status} and retry_time is not null and current_timestamp() >= retry_time")
    public List<TaskLogInstance> selectThreadByStatus2(@Param("status") String status);

    @Select("select * from task_log_instance where status in ('etl','dispatch')")
    public List<TaskLogInstance> selectThreadByStatus3();

    @Select("select * from task_log_instance where group_id = ${group_id}")
    public List<TaskLogInstance> selectByGroupId(@Param("group_id") String group_id);

    @Select("select * from task_log_instance where status in ('finish','skip') and id=#{id}")
    public TaskLogInstance selectByIdStatus(@Param("id") String id);

    @Update(value = "update task_log_instance set status=#{status} where id=#{id} and status='running' and process > #{process}")
    public int updateStatusById3(@Param("status") String status, @Param("process") String process, @Param("id") String id);


    @Select("select sum(num) as num from (" +
            "(select count(1) as num from ssh_task_info)" +
            "UNION ALL " +
            "(select count(1) as num from sql_task_info)" +
            "UNION ALL "+
            "(select count(1) as num from etl_task_info) " +
            "union all " +
            "(select count(1) as num from etl_more_task_info) " +
            "union all " +
            "(select count(1) as num from etl_drools_task_info)" +
            ") a")
    //@Select("select count(1) as num from sssh_task_info ")
    @Results({@Result(column="num",property="num")
    })
    public int allTaskNum();

    @Select(
            "(select count(1) as num from quartz_job_info)"
           )
    //@Select("select count(1) as num from sssh_task_info ")
    @Results({@Result(column="num",property="num")
    })
    public int allDispatchNum();

    @Select(
            "(select count(1) as num from quartz_job_info where status in ('running','pause') and job_type not in ('email','retry','check'))"
    )
    //@Select("select count(1) as num from sssh_task_info ")
    @Results({@Result(column="num",property="num")
    })
    public int allDispatchRunNum();

    @Select(
            "(select count(1) as num from task_log_instance where status='finish' and date_format(run_time,'%Y-%m-%d') = date_format(current_timestamp(),'%Y-%m-%d'))"
    )
    //@Select("select count(1) as num from sssh_task_info ")
    @Results({@Result(column="num",property="num")
    })
    public int successNum();

    @Select(
            "(select count(1) as num from task_log_instance where status in ('error','kill','killed') and date_format(run_time,'%Y-%m-%d') = date_format(current_timestamp(),'%Y-%m-%d'))"
    )
    //@Select("select count(1) as num from sssh_task_info ")
    @Results({@Result(column="num",property="num")
    })
    public int errorNum();

    @Select(
            "(select count(1) as num from task_log_instance where is_notice in ('alarm','true') and date_format(run_time,'%Y-%m-%d') = date_format(current_timestamp(),'%Y-%m-%d'))"
    )
    //@Select("select count(1) as num from sssh_task_info ")
    @Results({@Result(column="num",property="num")
    })
    public int alarmNum();

    @Results({@Result(column="num",property="num"),
            @Result(column="status",property="status"),
    })
    @Select(
            {
                    "<script>",
                    "select status,count(1) as num from task_log_instance tli where tli.id in",
                    "<foreach collection='ids' item='id' open='(' separator=',' close=')'>",
                    "#{id}",
                    "</foreach>",
                    " group by status",
                    "</script>"
            }
    )
    public List<task_num_info> selectByIds(@Param("ids") String[] ids);

    @Select(
            {
                    "<script>",
                    "select * from task_log_instance tli where tli.id in",
                    "<foreach collection='ids' item='id' open='(' separator=',' close=')'>",
                    "#{id}",
                    "</foreach>",
                    "and status in ('kill','killed','error')",
                    "</script>"
            }
    )
    public List<TaskLogInstance> selectTliByIds(@Param("ids") String[] ids);

    @Delete(
            {
                    "<script>",
                    "delete from task_log_instance where id in",
                    "<foreach collection='ids' item='id' open='(' separator=',' close=')'>",
                    "#{id}",
                    "</foreach>",
                    "</script>"
            }
    )
    public int deleteByIds(@Param("ids") String[] ids);

}