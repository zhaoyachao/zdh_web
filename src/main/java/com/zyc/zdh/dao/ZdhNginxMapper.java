package com.zyc.zdh.dao;

import com.zyc.notscan.BaseMapper;
import com.zyc.zdh.entity.ZdhLogs;
import com.zyc.zdh.entity.ZdhNginx;
import org.apache.ibatis.annotations.*;

import java.sql.Timestamp;
import java.util.List;

/**
 * ClassName: DataSourcesMapper
 * @author zyc-admin
 * @date 2017年12月26日  
 * @Description: TODO  
 */
public interface ZdhNginxMapper extends BaseMapper<ZdhNginx> {

    @Select(value = "select * from zdh_nginx where owner=#{owner} ")
    public ZdhNginx selectByOwner(@Param("owner") String owner);

}
