package com.zyc.zdh.dao;

import com.zyc.notscan.base.BaseSelfHistoryMapper;
import com.zyc.zdh.entity.SelfHistory;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.sql.Timestamp;

public interface SelfHistoryMapper extends BaseSelfHistoryMapper<SelfHistory> {
    @Update(
            {
                    "<script>",
                    "update self_history set is_delete=1 ,update_time= #{update_time} where id in ",
                    "<foreach collection='ids' item='id' open='(' separator=',' close=')'>",
                    "#{id}",
                    "</foreach>",
                    "</script>"
            }
    )
    public int deleteBatchById(@Param("ids") String[] ids, @Param("update_time") Timestamp update_time);
}