package com.zyc.zdh.service.impl;

import com.zyc.zdh.dao.EtlTaskMapper;
import com.zyc.zdh.entity.EtlTaskInfo;
import com.zyc.zdh.entity.QuotaInfo;
import com.zyc.zdh.service.EtlTaskService;
import org.apache.commons.beanutils.ConvertUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Service
public class EtlTaskServiceImpl implements EtlTaskService {

    @Autowired
    private EtlTaskMapper etlTaskMapper;


    @Override
    public EtlTaskInfo selectById(String id) {
        EtlTaskInfo etlTaskInfo=new EtlTaskInfo();
        etlTaskInfo.setId(id);
        return etlTaskMapper.selectByPrimaryKey(etlTaskInfo);
    }

    @Override
    public int insert(EtlTaskInfo etlTaskInfo) {
        return etlTaskMapper.insertSelective(etlTaskInfo);
    }

    @Override
    public int delete(long id) {
        return etlTaskMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int update(EtlTaskInfo etlTaskInfo) {
        return etlTaskMapper.updateByPrimaryKeySelective(etlTaskInfo);
    }

    @Override
    public List<EtlTaskInfo> selectByExample(EtlTaskInfo etlTaskInfo) {
        return null;
    }

    @Override
    public List<EtlTaskInfo> selectAll() {
        return etlTaskMapper.selectAll();
    }

    @Override
    public int deleteBatchById(Long[] ids) {
        String[]  ids2 = (String[])ConvertUtils.convert(ids, String[].class);
        etlTaskMapper.deleteBatchById(ids2, new Timestamp(System.currentTimeMillis()));
        return 0;
    }

    @Override
    public List<EtlTaskInfo> select(EtlTaskInfo etlTaskInfo) {
        return etlTaskMapper.select(etlTaskInfo);
    }

    @Override
    public List<EtlTaskInfo> selectByIds(String[] ids) {
        return etlTaskMapper.selectByIds(ids);
    }

    @Override
    public List<EtlTaskInfo> selectByParams(String owner, String etl_context,String file_name) {
        return null;
    }

    @Override
    public List<QuotaInfo> selectByColumn(String owner, String column_desc, String column_alias,String company,String section,String service) {
        return etlTaskMapper.selectByColumn(owner,column_desc,column_alias,company,section,service);
    }
}
