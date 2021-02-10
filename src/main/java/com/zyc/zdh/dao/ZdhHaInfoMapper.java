package com.zyc.zdh.dao;

import com.zyc.notscan.BaseMapper;
import com.zyc.zdh.entity.ZdhHaInfo;
import com.zyc.zdh.entity.ZdhLogs;
import org.apache.ibatis.annotations.*;

import java.sql.Timestamp;
import java.util.List;

/**
 * ClassName: ZdhHaInfoMapper
 * @author zyc-admin
 * @date 2017年12月26日  
 * @Description: TODO  
 */
public interface ZdhHaInfoMapper extends BaseMapper<ZdhHaInfo> {



    @Select(value="select * from zdh_ha_info where zdh_status=#{status} and timestampdiff(SECOND,current_timestamp(),update_time) <= 60 and online='1'")
    @Results({@Result(column="id",property="id"),
            @Result(column="zhd_instance",property="zhd_instance"),
            @Result(column="zhd_url",property="zhd_url"),
            @Result(column="zhd_host",property="zhd_host"),
            @Result(column="zhd_port",property="zhd_port"),
            @Result(column="web_port",property="web_port"),
            @Result(column="zdh_status",property="zdh_status")
    })
    public List<ZdhHaInfo> selectByStatus(@Param("status") String status);

    @Select(
            {
                    "<script>",
                    "select * from zdh_ha_info where 1=1",
                    "<when test='online!=null and online !=\"\"'>",
                    "AND online = #{online}",
                    "</when>",
                    "<when test='context!=null and context !=\"\"'>",
                    "AND (zhd_instance like  '%${context}%' ",
                    " OR zhd_url like  '%${context}%' ",
                    " OR zhd_host like  '%${context}%' ",
                    " OR zhd_port like  '%${context}%' ",
                    " OR web_port like  '%${context}%' ",
                    " OR zdh_status like  '%${context}%' ",
                    " OR online like  '%${context}%' )",
                    "</when>",
                    "</script>"
            }
           )
    @Results({@Result(column="id",property="id"),
            @Result(column="zhd_instance",property="zhd_instance"),
            @Result(column="zhd_url",property="zhd_url"),
            @Result(column="zhd_host",property="zhd_host"),
            @Result(column="zhd_port",property="zhd_port"),
            @Result(column="web_port",property="web_port"),
            @Result(column="zdh_status",property="zdh_status"),
            @Result(column="online",property="online")
    })
    public List<ZdhHaInfo> selectServer(@Param("online") String status,@Param("context") String context);

    @Update("update zdh_ha_info set online=#{online} where id=#{id}")
    public List<ZdhHaInfo> updateOnline(@Param("online") String online,@Param("id") String id);


}
