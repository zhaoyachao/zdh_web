package com.zyc.zdh.dao;

import com.zyc.notscan.base.BaseEtlTaskBatchMapper;
import com.zyc.zdh.entity.EtlTaskBatchInfo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.sql.Timestamp;

public interface EtlTaskBatchMapper extends BaseEtlTaskBatchMapper<EtlTaskBatchInfo> {
    @Update(
            {
                    "<script>",
                    "update etl_task_batch_info set is_delete=1 ,update_time= #{update_time} where id in ",
                    "<foreach collection='ids' item='id' open='(' separator=',' close=')'>",
                    "#{id}",
                    "</foreach>",
                    "</script>"
            }
    )
    public int deleteBatchById(@Param("ids") String[] ids, @Param("update_time") Timestamp update_time);

}