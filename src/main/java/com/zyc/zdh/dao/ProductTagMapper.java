package com.zyc.zdh.dao;

import com.zyc.notscan.BaseMapper;
import com.zyc.zdh.entity.ProductTagInfo;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;

import java.sql.Timestamp;

public interface ProductTagMapper extends BaseMapper<ProductTagInfo> {
    @Update(
            {
                    "<script>",
                    "update product_tag_info set is_delete=1 ,update_time= #{update_time} where id in ",
                    "<foreach collection='ids' item='id' open='(' separator=',' close=')'>",
                    "#{id}",
                    "</foreach>",
                    "</script>"
            }
    )
    public int deleteBatchById(@Param("ids") String[] ids, @Param("update_time") Timestamp update_time);
}