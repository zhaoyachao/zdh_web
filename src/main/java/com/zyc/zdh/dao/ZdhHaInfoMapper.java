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



    @Select(value="select * from zdh_ha_info where zdh_status=#{status} and timestampdiff(SECOND,current_timestamp(),update_time) >= 60")
    @Results({@Result(column="id",property="id"),
            @Result(column="zhd_instance",property="zhd_instance"),
            @Result(column="zhd_url",property="zhd_url"),
            @Result(column="zhd_host",property="zhd_host"),
            @Result(column="zhd_port",property="zhd_port"),
            @Result(column="web_port",property="web_port"),
            @Result(column="zdh_status",property="zdh_status")
    })
    public List<ZdhHaInfo> selectByStatus(@Param("status") String status);

}
