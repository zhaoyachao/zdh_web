package com.zyc.zdh.dao;

import com.zyc.notscan.base.BaseTaskLogInstanceMapper;
import com.zyc.zdh.entity.EtlEcharts;
import com.zyc.zdh.entity.TaskLogInstance;
import com.zyc.zdh.entity.task_num_info;
import org.apache.ibatis.annotations.*;

import java.sql.Timestamp;
import java.util.List;


public interface TaskLogInstanceMapper extends BaseTaskLogInstanceMapper<TaskLogInstance> {


    /**
     * 根据id更新子任务状态
     * @param status
     * @param id
     * @return
     */
    @Update(value = "update task_log_instance set status=#{status},update_time=#{update_time} where id=#{id}")
    public int updateStatusById(@Param("status") String status,@Param("update_time") String update_time, @Param("id") String id);


    /**
     * 根据id更新子任务的线程id
     * @param thread_id
     * @param id
     * @return
     */
    @Update(value = "update task_log_instance set thread_id=#{thread_id} where id=#{id}")
    public int updateThreadById(@Param("thread_id") String thread_id, @Param("id") String id);

    /**
     * 杀死任务
     * 1如果任务是check_dep(检查依赖),wait_retry(等待重试),create, 或者 状态=dispatch, job_type=jdbc,group,shell,hdfs这些依赖检查任务时 直接 设置状态为killed, 其他都是kill(后续会自动杀死)
     * @param id
     * @return
     */
    @Update({
            "<script>",
            "update task_log_instance ",
            " set status= case when `status`='check_dep' or `status`='waite_retry' or `status`= 'create' or (`status`='dispatch' and job_type in ('JDBC','GROUP','SHELL','HDFS')) then 'killed' else 'kill' end",
            " where id=#{id} and (`status`='dispatch' or `status` ='etl' or `status`= 'check_dep' or `status`= 'wait_retry' or `status`= 'create')",
            "</script>",
    })
    public int updateStatusById2(@Param("id") String id);

    /**
     * 跳过任务
     * @param id
     * @return
     */
    @Update({
            "<script>",
            "update task_log_instance ",
            " set status= 'skip' ",
            " where id=#{id} ",
            "</script>",
    })
    public int updateSkipById(@Param("id") String id);

    // value = "update task_log_instance set status=#{status} where id=#{id} and status not in ('kill','killed')")
    /**
     * 更新状态避免杀死后继续更新
     * @param status
     * @param id
     * @return
     */
    @Update(
            {
                    "<script>",
                    "update task_log_instance set status=#{status}",
                    "<when test='process!=null and process !=\"\"'>",
                    ", process = #{process}",
                    "</when>",
                    ", update_time = current_timestamp()",
                    "where id=#{id} and status not in ('kill','killed')",
                    "</script>"
            }
    )
    public int updateStatusById4(@Param("status") String status,@Param("process")String process, @Param("id") String id);

    /**
     * 杀死任务组下子任务
     * 1如果任务是check_dep(检查依赖),wait_retry(等待重试),create, 或者 状态=dispatch, job_type=jdbc,group,shell,hdfs这些依赖检查任务时 直接 设置状态为killed, 其他都是kill(后续会自动杀死)
     * @param group_id
     * @return
     */
    @Update(value = "update task_log_instance set status= case when `status` in ('check_dep','wait_retry','check_dep_finish','create') or (`status`= 'dispatch' and job_type in ('JDBC','GROUP','SHELL','HDFS')) then 'killed' when `status` in ('error','finish','skip') then `status` else 'kill'  end where group_id=#{group_id} and (`status` != 'error' and `status` != 'killed')")
    public int updateStatusByGroupId(@Param("group_id") String group_id);

    @Update(value = "update task_log_instance set is_notice=#{is_notice} where id=#{id}")
    public int updateNoticeById(@Param("is_notice") String is_notice,@Param("id") String id);

    @Select({
            "<script>",
            "select * from (select task.etl_date,sum(case task.status when 'running' then 1 else 0 end) as \"running\",\n" +
                    "sum(case task.status when 'error' then 1 else 0 end) as \"error\",\n" +
                    "sum(case task.status when 'finish' then 1 else 0 end) as \"finish\"\n" +
                    "from task_log_instance task inner join (select id as group_instance_id, product_code, dim_group from task_group_log_instance) g on task.group_id=g.group_instance_id where ",
            "g.product_code in ",
            "<foreach collection='product_codes' item='product_code' open='(' separator=',' close=')'>",
            "#{product_code}",
            "</foreach>",
            "and g.dim_group in ",
            "<foreach collection='dim_groups' item='dim_group' open='(' separator=',' close=')'>",
            "#{dim_group}",
            "</foreach>",

    "and task.etl_date is not null group by task.etl_date,task.status order by task.etl_date desc limit 15) t order by etl_date asc",
            "</script>"
    }
    )
    @Results({@Result(column="etl_date",property="etl_date"),
            @Result(column="running",property="running"),
            @Result(column="error",property="error"),
            @Result(column="finish",property="finish")
    })
    public List<EtlEcharts> slectByOwner(@Param("owner") String owner, @Param("product_codes") List<String> product_codes,
                                         @Param("dim_groups") List<String> dim_groups);

    @Select({
            "<script>",
            "select etl_date,sum(case status when 'running' then 1 else 0 end) as \"running\",\n" +
            "sum(case status when 'error' then 1 else 0 end) as \"error\",\n" +
            "sum(case status when 'finish' then 1 else 0 end) as \"finish\"\n" +
            "from task_log_instance task inner join (select id as group_instance_id, product_code, dim_group from task_group_log_instance) g on task.group_id = g.group_instance_id where ",
            "product_code in ",
            "<foreach collection='product_codes' item='product_code' open='(' separator=',' close=')'>",
            "#{product_code}",
            "</foreach>",
            "and dim_group in ",
            "<foreach collection='dim_groups' item='dim_group' open='(' separator=',' close=')'>",
            "#{dim_group}",
            "</foreach>",
            "and etl_date=#{etl_date} group by etl_date,status",
            "</script>"
    })
    @Results({@Result(column="etl_date",property="etl_date"),
            @Result(column="running",property="running"),
            @Result(column="error",property="error"),
            @Result(column="finish",property="finish")
    })
    public List<EtlEcharts> slectByOwnerEtlDate(@Param("etl_date") String etl_date, @Param("product_codes") List<String> product_codes,
                                                @Param("dim_groups") List<String> dim_groups);

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
            "WHERE notice_timeout='on' and alarm_account is not null and alarm_account != '' and time_out is not null and time_out > '0' and timestampdiff(second,run_time,current_timestamp()) >= time_out",
            " and is_notice != 'true' and status in ('etl','dispatch') and run_time != '2000-01-01 00:00:00'",
            "</script>"})
    public List<TaskLogInstance> selectOverTime();

    /**
     * 超时完成任务,未进行通知
     * @return
     */
    @Select({"<script>",
            "SELECT * FROM task_log_instance",
            "WHERE ",
            " is_notice != 'true' and status ='finish'",
            "and timestampdiff(second,run_time,update_time) >= time_out",
            "and (alarm_email='on' or alarm_sms='on' or alarm_zdh='on')",
            "and notice_timeout='on'",
            "</script>"})
    public List<TaskLogInstance> selectOverTimeFinish();


    /**
     * 任务正常完成通知
     * @return
     */
    @Select({"<script>",
            "SELECT * FROM task_log_instance",
            "WHERE ",
            " is_notice != 'true' and status ='finish'",
            "and <![CDATA[ timestampdiff(second,run_time,update_time)  < time_out ]]>  ",
            "and (alarm_email='on' or alarm_sms='on' or alarm_zdh='on') ",
            "and notice_finish='on'",
            "</script>"})
    public List<TaskLogInstance> selectNoNoticeFinish();

//    @Select("select a.*,b.email,b.user_name as userName,b.phone,b.is_use_email,b.is_use_phone from task_log_instance a,account_info b where a.notice_error = 'on' and a.status='error' and a.is_notice !='true' and a.owner=b.id and  TIMESTAMPDIFF(day,a.run_time,NOW()) < 2  ")
//    public List<EmailTaskLogs> selectByStatus();

    @Select({"<script>",
            "SELECT * FROM task_log_instance",
            "WHERE ",
            " is_notice != 'true' and status = #{status}",
            "and <![CDATA[ timestampdiff(second,run_time,update_time)  < time_out ]]>  ",
            "and (alarm_email='on' or alarm_sms='on' or alarm_zdh='on') ",
            "and (notice_error ='on' or notice_finish='on' or notice_timeout='on') ",
            "</script>"})
    public List<TaskLogInstance> selectByStatus(@Param("status")String status);

    @Select("select * from task_log_instance where etl_date=#{etl_date} and job_id=#{job_id}")
    public List<TaskLogInstance> selectByIdEtlDate(@Param("job_id") String job_id, @Param("etl_date") String etl_date);

    @Select("select * from task_log_instance where status =#{status}")
    public List<TaskLogInstance> selectThreadByStatus(@Param("status") String status);

    /**
     * 获取所有可以执行的子任务
     * 可优化sql 提高效率
     * @param status
     * @return
     */
    @Select(
            {
            "<script>",
            "select tli.* from task_log_instance tli inner join task_group_log_instance tgli",
            " on tli.group_id=tgli.id",
            "and tgli.status = 'sub_task_dispatch'",
            "where tli.status in",
            "<foreach collection='status' item='st' open='(' separator=',' close=')'>",
            "#{st}",
            "</foreach>",
            " and tli.job_type not in ",
            "<foreach collection='job_types' item='job_type' open='(' separator=',' close=')'>",
            "#{job_type}",
            "</foreach>",
            " order by tli.priority desc, tli.id asc",
            //" and tli.job_type not in ('group','jdbc','hdfs')",
            "</script>"
            }
    )
    public List<TaskLogInstance> selectThreadByStatus1(@Param("status") String[] status, @Param("job_types") String[] job_types);

    /**
     * 获取所有可以执行的JDBC子任务
     * @param status
     * @return
     */
    @Select(
            {
                    "<script>",
                    "select tli.* from task_log_instance tli inner join task_group_log_instance tgli",
                    " on tli.group_id=tgli.id",
                    " and tgli.status = 'sub_task_dispatch'",
                    "where tli.status in",
                    "<foreach collection='status' item='st' open='(' separator=',' close=')'>",
                    "#{st}",
                    "</foreach>",
                    " and tli.job_type in ",
                    "<foreach collection='job_types' item='job_type' open='(' separator=',' close=')'>",
                    "#{job_type}",
                    "</foreach>",
                    " order by tli.priority desc, tli.id asc",
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

    @Select("select * from task_log_instance where id=#{id}")
    public TaskLogInstance selectById(@Param("id") String id);

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

    /**
     * 获取上游任务并且状态是kill,error,killed
     * @param ids
     * @return
     */
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


    /**
     * 获取上游任务信息
     * @param ids
     * @return
     */
    @Select(
            {
                    "<script>",
                    "select * from task_log_instance tli where tli.id in",
                    "<foreach collection='ids' item='id' open='(' separator=',' close=')'>",
                    "#{id}",
                    "</foreach>",
                    "</script>"
            }
    )
    public List<TaskLogInstance> selectByIds2(@Param("ids") String[] ids);


    /**
     * 获取上游成功或者跳过状态任务信息
     * @param ids
     * @return
     */
    @Select(
            {
                    "<script>",
                    "select * from task_log_instance tli where tli.id in",
                    "<foreach collection='ids' item='id' open='(' separator=',' close=')'>",
                    "#{id}",
                    "</foreach>",
                    "and status in ('finish','skip')",
                    "</script>"
            }
    )
    public List<TaskLogInstance> selectByFinishIds(@Param("ids") String[] ids);

    /**
     * 获取上游成功,跳过,失败状态任务信息
     * @param ids
     * @return
     */
    @Select(
            {
                    "<script>",
                    "select * from task_log_instance tli where tli.id in",
                    "<foreach collection='ids' item='id' open='(' separator=',' close=')'>",
                    "#{id}",
                    "</foreach>",
                    "and status in ('finish','skip','error')",
                    "</script>"
            }
    )
    public List<TaskLogInstance> selectByEndIds(@Param("ids") String[] ids);

    /**
     * 获取以杀死的任务
     * @param ids
     * @return
     */
    @Select(
            {
                    "<script>",
                    "select * from task_log_instance tli where tli.id in",
                    "<foreach collection='ids' item='id' open='(' separator=',' close=')'>",
                    "#{id}",
                    "</foreach>",
                    "and status in ('killed')",
                    "</script>"
            }
    )
    public List<TaskLogInstance> selectByKilledIds(@Param("ids") String[] ids);

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

    @Select(
            {
                    "<script>",
                    "select * from task_log_instance tli where 1=1 ",
                    "<when test='tli.id!=null and tli.id !=\"\"'>",
                    "AND id = #{tli.id}",
                    "</when>",
                    "<when test='tli.job_id!=null and tli.job_id !=\"\"'>",
                    "AND job_id = #{tli.job_id}",
                    "</when>",
                    "<when test='tli.job_context!=null and tli.job_context !=\"\"'>",
                    "AND job_context = #{tli.job_context}",
                    "</when>",
                    "<when test='tli.group_id!=null and tli.group_id !=\"\"'>",
                    "AND group_id = #{tli.group_id}",
                    "</when>",
                    "<when test='tli.group_context!=null and tli.group_context !=\"\"'>",
                    "AND group_context = #{tli.group_context}",
                    "</when>",
                    "<when test='tli.etl_date!=null and tli.etl_date !=\"\"'>",
                    "AND etl_date = #{tli.etl_date}",
                    "</when>",
                    "<when test='tli.status!=null and tli.status !=\"\"'>",
                    "AND status = #{tli.status}",
                    "</when>",
                    "<when test='tli.more_task!=null and tli.more_task !=\"\"'>",
                    "AND more_task = #{tli.more_task}",
                    "</when>",
                    "<when test='tli.job_type!=null and tli.job_type !=\"\"'>",
                    "AND job_type = #{tli.job_type}",
                    "</when>",
                    "</script>"
            }
    )
    public List<TaskLogInstance> selectByTaskLogInstance(@Param("tli") TaskLogInstance tli);

    @Update(value = "update task_log_instance set end_time=#{end_time} where id=#{id}")
    public int updateEndTimeById(@Param("end_time") Timestamp end_time, @Param("id") String id);

    /**
     * 获取正在运行的任务实例
     * @param more_task
     * @return
     */
    @Select(
            {
                    "<script>",
                    "select * from task_log_instance tli where more_task=#{more_task}",
                    "and status='etl'",
                    "</script>"
            }
    )
    public List<TaskLogInstance> selectByMoreTask(@Param("more_task") String more_task);


}