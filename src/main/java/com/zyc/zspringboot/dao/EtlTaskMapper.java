package com.zyc.zspringboot.dao;

import com.zyc.notscan.BaseMapper;
import com.zyc.zspringboot.entity.DataSourcesInfo;
import com.zyc.zspringboot.entity.EtlTaskInfo;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;

/**
 * ClassName: DataSourcesMapper
 * @author zyc-admin
 * @date 2017年12月26日  
 * @Description: TODO  
 */
public interface EtlTaskMapper extends BaseMapper<EtlTaskInfo> {

    @Delete("delete from etl_task_info where id = #{ids_str}")
    public int deleteBatchById(@Param("ids_str") String ids_str);
}
