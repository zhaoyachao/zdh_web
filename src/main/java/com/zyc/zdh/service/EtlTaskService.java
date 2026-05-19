package com.zyc.zdh.service;

import com.zyc.zdh.entity.EtlTaskInfo;
import com.zyc.zdh.entity.QuotaInfo;

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

	public List<EtlTaskInfo> selectByIds(String[] ids);

	public List<EtlTaskInfo> selectByParams(String owner,String etl_context,String file_name);

	public List<QuotaInfo> selectByColumn(String owner, String column_desc, String column_alias,String company,String section,String service);


}
