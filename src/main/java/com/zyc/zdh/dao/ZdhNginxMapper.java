package com.zyc.zdh.dao;

import com.zyc.notscan.base.BaseZdhNginxMapper;
import com.zyc.zdh.entity.ZdhNginx;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * ClassName: DataSourcesMapper
 * @author zyc-admin
 * @date 2017年12月26日  
 * @Description: TODO  
 */
public interface ZdhNginxMapper extends BaseZdhNginxMapper<ZdhNginx> {

    @Select(value = "select * from zdh_nginx where owner=#{owner} ")
    public ZdhNginx selectByOwner(@Param("owner") String owner);

}
