package com.zyc.zdh.dao;

import com.zyc.notscan.base.BaseStrategyInstanceMapper;
import com.zyc.zdh.entity.StrategyInstance;
import com.zyc.zdh.entity.task_num_info;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface StrategyInstanceMapper extends BaseStrategyInstanceMapper<StrategyInstance> {
    @Select({
            "<script>",
            "select * from strategy_instance where group_instance_id = #{group_instance_id}",
            "<when test='status!=null and status !=\"\"'>",
            "AND status = #{status}",
            "</when>",
            "</script>"
    })
    public List<StrategyInstance> selectByGroupInstanceId(@Param("group_instance_id") String group_instance_id, @Param("status") String status);


    /**
     * 获取所有可以执行的子任务
     * @param status
     * @return
     */
    @Select(
            {
                    "<script>",
                    "select si.* from strategy_instance si,strategy_group_instance sgi where si.status in",
                    "<foreach collection='status' item='st' open='(' separator=',' close=')'>",
                    "#{st}",
                    "</foreach>",
                    " and sgi.status = 'sub_task_dispatch'",
                    " and si.group_instance_id=sgi.id",
                    " and sgi.group_type = #{group_type}",
                    "</script>"
            }
    )
    public List<StrategyInstance> selectThreadByStatus1(@Param("status") String[] status, @Param("group_type") String group_type);

    /**
     * 获取上游任务并且状态是kill,error,killed
     * @param ids
     * @return
     */
    @Select(
            {
                    "<script>",
                    "select * from strategy_instance si where si.id in",
                    "<foreach collection='ids' item='id' open='(' separator=',' close=')'>",
                    "#{id}",
                    "</foreach>",
                    "and status in ('kill','killed','error')",
                    "</script>"
            }
    )
    public List<StrategyInstance> selectByIds(@Param("ids") String[] ids);


    /**
     * 获取上游任务
     * @param ids
     * @return
     */
    @Select(
            {
                    "<script>",
                    "select * from strategy_instance si where si.id in",
                    "<foreach collection='ids' item='id' open='(' separator=',' close=')'>",
                    "#{id}",
                    "</foreach>",
                    "</script>"
            }
    )
    public List<StrategyInstance> selectAllByIds(@Param("ids") String[] ids);

    /**
     * 获取上游成功或者跳过状态任务信息
     * @param ids
     * @return
     */
    @Select(
            {
                    "<script>",
                    "select * from strategy_instance si where si.id in",
                    "<foreach collection='ids' item='id' open='(' separator=',' close=')'>",
                    "#{id}",
                    "</foreach>",
                    "and status in ('finish','skip')",
                    "</script>"
            }
    )
    public List<StrategyInstance> selectByFinishIds(@Param("ids") String[] ids);

    /**
     * 获取上游执行完成的任务
     * 状态包含 'finish','skip','error', 'killed'
     * @param ids
     * @return
     */
    @Select(
            {
                    "<script>",
                    "select * from strategy_instance si where si.id in",
                    "<foreach collection='ids' item='id' open='(' separator=',' close=')'>",
                    "#{id}",
                    "</foreach>",
                    "and status in ('finish','skip','error', 'killed')",
                    "</script>"
            }
    )
    public List<StrategyInstance> selectAllFinished(@Param("ids") String[] ids);

    @Select(
            {
                    "<script>",
                    "select status,count(1) as num from strategy_instance si where si.group_instance_id in",
                    "<foreach collection='ids' item='id' open='(' separator=',' close=')'>",
                    "#{id}",
                    "</foreach>",
                    " group by status",
                    "</script>"
            }
    )
    public List<task_num_info> selectStatusByIds(@Param("ids") String[] ids);

    @Update(
            {
                    "<script>",
                    "update strategy_instance set status=#{status} where id in",
                    "<foreach collection='ids' item='id' open='(' separator=',' close=')'>",
                    "#{id}",
                    "</foreach>",
                    "</script>"
            }
    )
    public int updateStatusByIds(@Param("ids") String[] ids, @Param("status") String status);


    @Update(
            {
                    "<script>",
                    "update strategy_instance set status=#{status} where group_instance_id =",
                    "#{group_instance_id}",
                    " and status not in ('etl','error','skip','check_dep_finish','finish')",
                    "</script>"
            }
    )
    public int updateStatusKilledByGroupInstanceId(@Param("group_instance_id") String group_instance_id, @Param("status") String status);

    @Update(
            {
                    "<script>",
                    "update strategy_instance set status=#{status} where group_instance_id =",
                    "#{group_instance_id}",
                    " and status in ('etl','error','skip','check_dep_finish')",
                    "</script>"
            }
    )
    public int updateStatusKillByGroupInstanceId(@Param("group_instance_id") String group_instance_id, @Param("status") String status);

    @Update(
            {
                    "<script>",
                    "update strategy_instance set status = case when status in ('etl','error','skip','check_dep_finish') then 'kill' else 'killed' end  where id in",
                    "<foreach collection='ids' item='id' open='(' separator=',' close=')'>",
                    "#{id}",
                    "</foreach>",
                    "</script>"
            }
    )
    public int updateStatusKillByIds(@Param("ids") String[] ids);

    @Update(
            {
                    "<script>",
                    "update strategy_instance set status=#{status} where group_instance_id =",
                    "#{group_instance_id}",
                    "</script>"
            }
    )
    public int updateStatusByGroupInstanceId(@Param("group_instance_id") String group_instance_id, @Param("status") String status);


    @Update(value = "update strategy_instance set is_notice=#{is_notice} where id=#{id}")
    public int updateNoticeById(@Param("is_notice") String is_notice,@Param("id") String id);
}