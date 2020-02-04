package com.zyc.zspringboot.service;

import com.zyc.zspringboot.entity.DataSourcesInfo;
import com.zyc.zspringboot.netty.MsgInfo;

import java.util.List;

/**
 * ClassName: DataSourcesService
 *
 * @author zyc-admin
 * @date 2017年12月26日
 * @Description: TODO
 */
public interface DataSourcesService {

	public DataSourcesInfo selectById(String id);

	public int insert(DataSourcesInfo dataSourcesInfo);

	public int delete(long id);

	public int update(DataSourcesInfo dataSourcesInfo);

	public List<DataSourcesInfo> selectByExample(DataSourcesInfo dataSourcesInfo);

	public List<DataSourcesInfo> selectAll();

	public int deleteBatchById(Long[] ids);

	public List<DataSourcesInfo> select(DataSourcesInfo dataSourcesInfo);
}
