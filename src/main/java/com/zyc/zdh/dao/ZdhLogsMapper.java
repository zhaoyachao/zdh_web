package com.zyc.zdh.dao;

import com.zyc.notscan.BaseMapper;
import com.zyc.zdh.entity.ZdhLogs;
import org.apache.ibatis.annotations.*;

import java.sql.Timestamp;
import java.util.List;

/**
 * ClassName: DataSourcesMapper
 * @author zyc-admin
 * @date 2017年12月26日  
 * @Description: TODO  
 */
public interface ZdhLogsMapper extends BaseMapper<ZdhLogs> {

    @Delete("delete from zdh_logs where job_id = #{ids_str}")
    public int deleteBatchById(@Param("ids_str") String ids_str);

    @Select(value =
            "select * from zdh_logs where job_id=#{job_id} and log_time >=#{start_time}  and log_time <=#{end_time} and level in (${levels}) ")
    @Results({@Result(column="job_id",property="job_id"),
            @Result(column="log_time",property="log_time"),
            @Result(column="msg",property="msg")
    })
    public List<ZdhLogs> selectByTime(@Param("job_id") String etl_task_id, @Param("start_time") Timestamp start_time, @Param("end_time") Timestamp end_time,@Param("levels") String levels);

    @Delete("delete from zdh_logs where job_id = #{job_id} and log_time >=#{start_time}  and log_time <=#{end_time}")
    public int deleteByTime(@Param("job_id") String job_id, @Param("start_time") Timestamp start_time, @Param("end_time") Timestamp end_time);

}
