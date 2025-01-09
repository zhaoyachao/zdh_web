package com.zyc.zdh.dao;

import com.zyc.notscan.base.BaseTaskGroupLogInstanceMapper;
import com.zyc.zdh.entity.TaskGroupLogInstance;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.sql.Timestamp;
import java.util.List;


public interface TaskGroupLogInstanceMapper extends BaseTaskGroupLogInstanceMapper<TaskGroupLogInstance> {


    /**
     * 根据id 更新要杀死的任务状态
     * @param id
     * @return
     */
    @Update(value = "update task_group_log_instance  set status= case when `status` in ('check_dep','wait_retry','check_dep_finish','create','dispatch') or (`status`='dispatch' and job_type in ('JDBC','GROUP')) then 'killed' when `status` in ('error','finish','skip') then `status` else 'kill'  end  where id=#{id} and (status != 'error' and status != 'killed')")
    public int updateStatusById2(@Param("id") String id);


    @Select({"<script>",
            "SELECT * FROM task_group_log_instance",
            "WHERE job_id=#{job_id}",
            "<when test='start_time!=null'>",
            "<![CDATA[ AND update_time >= #{start_time} ]]>",
            "</when>",
            "<when test='end_time!=null'>",
            "<![CDATA[ AND update_time <= #{end_time} ]]>",
            "</when>",
            "<when test='status!=null and status !=\"\"'>",
            "AND status = #{status}",
            "</when>",
            "and product_code in ",
            "<foreach collection='product_codes' item='product_code' open='(' separator=',' close=')'>",
            "#{product_code}",
            "</foreach>",
            "and dim_group in ",
            "<foreach collection='dim_groups' item='dim_group' open='(' separator=',' close=')'>",
            "#{dim_group}",
            "</foreach>",
            "order by run_time desc",
            "</script>"})
    public List<TaskGroupLogInstance> selectByTaskLogs2(@Param("start_time") Timestamp start_time,
                                                   @Param("end_time") Timestamp end_time, @Param("status") String status,
                                                        @Param("job_id") String job_id, @Param("product_codes") List<String> product_codes,
                                                        @Param("dim_groups") List<String> dim_groups);

    @Select({"<script>",
            "SELECT * FROM task_group_log_instance",
            "WHERE owner=#{owner} ",
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
    public List<TaskGroupLogInstance> selectByTaskLogs3(@Param("owner") String owner, @Param("start_time") Timestamp start_time,
                                                        @Param("end_time") Timestamp end_time, @Param("status") String status);

    @Select({"<script>",
            "SELECT * FROM task_group_log_instance",
            "WHERE ",
            "product_code in ",
            "<foreach collection='product_codes' item='product_code' open='(' separator=',' close=')'>",
            "#{product_code}",
            "</foreach>",
            "and dim_group in ",
            "<foreach collection='dim_groups' item='dim_group' open='(' separator=',' close=')'>",
            "#{dim_group}",
            "</foreach>",
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
            "limit ${offset}, ${limit}",
            "</script>"})
    public List<TaskGroupLogInstance> selectByTaskLogs4(@Param("start_time") Timestamp start_time,
                                                        @Param("end_time") Timestamp end_time, @Param("status") String status,
                                                        @Param("limit") int limit,  @Param("offset")int offset,
                                                        @Param("product_codes") List<String> product_codes,
                                                        @Param("dim_groups") List<String> dim_groups);

    @Select({"<script>",
            "SELECT count(1) as num FROM task_group_log_instance",
            "WHERE ",
            "product_code in ",
            "<foreach collection='product_codes' item='product_code' open='(' separator=',' close=')'>",
            "#{product_code}",
            "</foreach>",
            "and dim_group in ",
            "<foreach collection='dim_groups' item='dim_group' open='(' separator=',' close=')'>",
            "#{dim_group}",
            "</foreach>",
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
    public int selectCountByTaskLogs4(@Param("start_time") Timestamp start_time,
                                                        @Param("end_time") Timestamp end_time, @Param("status") String status,
                                      @Param("product_codes") List<String> product_codes,
                                      @Param("dim_groups") List<String> dim_groups);


    @Select("select * from task_group_log_instance where etl_date=#{etl_date} and job_id=#{job_id} and status in ('finish','skip')")
    public List<TaskGroupLogInstance> selectByIdEtlDate(@Param("job_id") String job_id, @Param("etl_date") String etl_date);

    @Select(
            {
            "<script>",
            "select * from task_group_log_instance where status in",
            "<foreach collection='status' item='st' open='(' separator=',' close=')'>",
            "#{st}",
            "</foreach>",
            " order by priority desc, id asc",
            "</script>"
            }
    )
    public List<TaskGroupLogInstance> selectTaskGroupByStatus(@Param("status") String[] status);


    /**
     * 根据id更新任务组状态和进度
     * @param status
     * @param process
     * @param id
     * @return
     */
    @Update(value = "update task_group_log_instance set status=#{status} ,process = #{process}, update_time=#{update_time} where id=#{id}")
    public int updateStatusById3(@Param("status") String status, @Param("process") String process,@Param("update_time") String update_time, @Param("id") String id);


    /**
     * 任务组及子任务创建完成改变状态为create
     * @param ids
     * @return
     */
    @Update(
            {
                    "<script>",
                    "update task_group_log_instance tgli inner join task_log_instance tli on tgli.id in " ,
                    "<foreach collection='ids' item='id' open='(' separator=',' close=')'>",
                    "#{id}",
                    "</foreach>",
                    "and tgli.status='non' and tli.status='non' and tgli.id=tli.group_id  set tgli.status='create',tli.status='create'",
                    "</script>"
            }
    )
    public int updateStatus2Create(@Param("ids") String[] ids);

    /**
     * 根据id和状态查询任务组
     * @param ids
     * @param status
     * @return
     */
    @Select(
            {
                    "<script>",
                    "select * from task_group_log_instance where id in ",
                    "<foreach collection='ids' item='id' open='(' separator=',' close=')'>",
                    "#{id}",
                    "</foreach>",
                    "<when test='status!=null and status !=\"\"'>",
                    "and status=#{status}",
                    "</when>",
                    "</script>"
            }
    )
    public List<TaskGroupLogInstance> selectByIds(@Param("ids") String[] ids,@Param("status") String status);

    /**
     * 批量删除任务组
     * @param ids
     * @return
     */
    @Delete(
            {
                    "<script>",
                    "delete from task_group_log_instance where id in",
                    "<foreach collection='ids' item='id' open='(' separator=',' close=')'>",
                    "#{id}",
                    "</foreach>",
                    "</script>"
            }
    )
    public int deleteByIds(@Param("ids") String[] ids);

}