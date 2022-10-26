package com.zyc.zdh.dao;

import com.zyc.notscan.base.BaseStrategyGroupInstanceMapper;
import com.zyc.zdh.entity.StrategyGroupInstance;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface StrategyGroupInstanceMapper extends BaseStrategyGroupInstanceMapper<StrategyGroupInstance> {

    @Update(
            {
                    "<script>",
                    "update strategy_group_instance sgi inner join strategy_instance si on sgi.id in " ,
                    "<foreach collection='ids' item='id' open='(' separator=',' close=')'>",
                    "#{id}",
                    "</foreach>",
                    "and sgi.status='non' and si.status='non' and sgi.id=si.group_instance_id  set sgi.status='create',si.status='create'",
                    "</script>"
            }
    )
    public int updateStatus2Create(@Param("ids") String[] ids);

    @Select(
            {
                    "<script>",
                    "select * from strategy_group_instance where status in",
                    "<foreach collection='status' item='st' open='(' separator=',' close=')'>",
                    "#{st}",
                    "</foreach>",
                    " order by priority desc",
                    "</script>"
            }
    )
    public List<StrategyGroupInstance> selectTaskGroupByStatus(@Param("status") String[] status);

    /**
     * 根据id更新任务组状态和进度
     * @param status
     * @param id
     * @return
     */
    @Update(value = "update strategy_group_instance set status=#{status}, update_time=#{update_time} where id=#{id}")
    public int updateStatusById3(@Param("status") String status,@Param("update_time") String update_time, @Param("id") String id);
}