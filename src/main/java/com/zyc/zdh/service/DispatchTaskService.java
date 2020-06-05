package com.zyc.zdh.service;

import com.zyc.zdh.entity.DispatchTaskInfo;

import java.util.List;

/**
 * ClassName: DataSourcesService
 *
 * @author zyc-admin
 * @date 2017年12月26日
 * @Description: TODO
 */
public interface DispatchTaskService {

	public DispatchTaskInfo selectById(String id);

	public int insert(DispatchTaskInfo dispatchTaskInfo);

	public int delete(long id);

	public int update(DispatchTaskInfo dispatchTaskInfo);

	public List<DispatchTaskInfo> selectByExample(DispatchTaskInfo dispatchTaskInfo);

	public List<DispatchTaskInfo> selectAll();

	public int deleteBatchById(Long[] ids);

	public List<DispatchTaskInfo> select(DispatchTaskInfo dispatchTaskInfo);
}
