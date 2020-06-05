package com.zyc.zdh.dao;

import com.zyc.notscan.BaseMapper;
import com.zyc.zdh.entity.DispatchTaskInfo;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;

/**
 * ClassName: DataSourcesMapper
 * @author zyc-admin
 * @date 2017年12月26日  
 * @Description: TODO  
 */
public interface DispatchTaskMapper extends BaseMapper<DispatchTaskInfo> {

    @Delete("delete from dispatch_task_info where id = #{ids_str}")
    public int deleteBatchById(@Param("ids_str") String ids_str);
}
