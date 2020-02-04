package com.zyc.zspringboot.service;

import com.zyc.zspringboot.entity.EtlTaskInfo;

import java.util.List;

/**
 * ClassName: DataSourcesService
 *
 * @author zyc-admin
 * @date 2017年12月26日
 * @Description: TODO
 */
public interface EtlTaskService {

	public EtlTaskInfo selectById(String id);

	public int insert(EtlTaskInfo etlTaskInfo);

	public int delete(long id);

	public int update(EtlTaskInfo etlTaskInfo);

	public List<EtlTaskInfo> selectByExample(EtlTaskInfo etlTaskInfo);

	public List<EtlTaskInfo> selectAll();

	public int deleteBatchById(Long[] ids);

	public List<EtlTaskInfo> select(EtlTaskInfo etlTaskInfo);
}
