package com.zyc.zdh.dao;

import com.zyc.notscan.base.BaseStrategyGroupLogMapper;
import com.zyc.zdh.entity.StrategyGroupLog;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface StrategyGroupLogMapper extends BaseStrategyGroupLogMapper<StrategyGroupLog> {

    @Select(
            {
                    "<script>",
                    "select max(log_version) as log_version from strategy_group_log where log_object_id=#{log_object_id} and log_type = #{log_type}",
                    "</script>"
            }
    )
    public Integer selectMaxLogVersion(@Param("log_type") String log_type, @Param("log_object_id") String log_object_id);
}