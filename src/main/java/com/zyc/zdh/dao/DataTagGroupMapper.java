package com.zyc.zdh.dao;

import com.zyc.notscan.base.BaseDataTagGroupMapper;
import com.zyc.zdh.entity.DataTagGroupInfo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.sql.Timestamp;

public interface DataTagGroupMapper extends BaseDataTagGroupMapper<DataTagGroupInfo> {
    @Update(
            {
                    "<script>",
                    "update data_tag_group_info set is_delete=1 ,update_time= #{update_time} where id in ",
                    "<foreach collection='ids' item='id' open='(' separator=',' close=')'>",
                    "#{id}",
                    "</foreach>",
                    "</script>"
            }
    )
    public int deleteBatchById(@Param("ids") String[] ids, @Param("update_time") Timestamp update_time);
}