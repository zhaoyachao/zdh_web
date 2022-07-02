package com.zyc.notscan;


import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import tk.mybatis.mapper.common.Mapper;

import java.sql.Timestamp;

/**
 * ClassName: BaseMapper   
 * @author zyc-admin
 * @date 2017年12月25日  
 * @Description: mybatis 统一mapper 接口
 * <br>注意此接口存在泛型，不能被spring和mybatis 扫描到
 */
public interface BaseMapper<T> extends Mapper<T> {
    /**
     * 通用逻辑删除
     * @param table_name
     * @param ids
     * @param update_time
     * @return
     */
    @Update({
            "<script>",
            "update ${table_name} set is_delete=1 , update_time = #{update_time} where id in ",
            "<foreach collection='ids' item='id' open='(' separator=',' close=')'>",
            "#{id}",
            "</foreach>",
            "</script>"
    }
    )
    public int deleteLogicByIds(@Param("table_name") String table_name, @Param("ids") String[] ids, @Param("update_time") Timestamp update_time);
}