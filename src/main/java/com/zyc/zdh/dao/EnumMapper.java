package com.zyc.zdh.dao;

import com.zyc.notscan.BaseMapper;
import com.zyc.zdh.entity.EnumInfo;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;

public interface EnumMapper extends BaseMapper<EnumInfo> {
    @Update({
            "<script>",
            "update enum_info set is_delete=1 where id in ",
            "<foreach collection='ids' item='id' open='(' separator=',' close=')'>",
            "#{id}",
            "</foreach>",
            "</script>"
    }
    )
    public int deleteBatchByIds(@Param("ids") String[] ids);
}