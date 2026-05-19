package com.zyc.zdh.dao;

import com.zyc.notscan.base.BaseEnumMapper;
import com.zyc.zdh.entity.EnumInfo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

public interface EnumMapper extends BaseEnumMapper<EnumInfo> {
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