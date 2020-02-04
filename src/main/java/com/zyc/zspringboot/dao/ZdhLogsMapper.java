package com.zyc.zspringboot.dao;

import com.zyc.notscan.BaseMapper;
import com.zyc.zspringboot.entity.ZdhLogs;
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

    @Delete("delete from zhd_logs where etl_task_id = #{ids_str}")
    public int deleteBatchById(@Param("ids_str") String ids_str);

    @Select(value="select * from zhd_logs where etl_task_id=#{etl_task_id} and log_time >=#{start_time}  and log_time <=#{end_time}")
    @Results({@Result(column="etl_task_id",property="etl_task_id"),
            @Result(column="log_time",property="log_time"),
            @Result(column="msg",property="msg")
    })
    public List<ZdhLogs> selectByTime(@Param("etl_task_id") String etl_task_id, @Param("start_time") Timestamp start_time, @Param("end_time") Timestamp end_time);
}
