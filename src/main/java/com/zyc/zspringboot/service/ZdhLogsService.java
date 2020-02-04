package com.zyc.zspringboot.service;

import com.zyc.zspringboot.entity.EtlTaskInfo;
import com.zyc.zspringboot.entity.ZdhLogs;

import java.sql.Timestamp;
import java.util.List;

/**
 * ClassName: DataSourcesService
 *
 * @author zyc-admin
 * @date 2017年12月26日
 * @Description: TODO
 */
public interface ZdhLogsService {

	public ZdhLogs selectById(String id);

	public int insert(ZdhLogs zdhLogs);

	public int delete(long id);

	public int update(ZdhLogs zdhLogs);

	public List<ZdhLogs> selectByExample(ZdhLogs zdhLogs);

	public List<ZdhLogs> selectAll();

	public int deleteBatchById(Long[] ids);

	public List<ZdhLogs> select(ZdhLogs zdhLogs);

	public List<ZdhLogs> selectByTime(String etl_task_id, Timestamp start_time,Timestamp end_time);
}
