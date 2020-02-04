package com.zyc.zspringboot.service.impl;

import com.zyc.zspringboot.dao.EtlTaskMapper;
import com.zyc.zspringboot.entity.DataSourcesInfo;
import com.zyc.zspringboot.entity.EtlTaskInfo;
import com.zyc.zspringboot.service.EtlTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EtlTaskServiceImpl implements EtlTaskService {

    @Autowired
    EtlTaskMapper etlTaskMapper;


    @Override
    public EtlTaskInfo selectById(String id) {
        EtlTaskInfo etlTaskInfo=new EtlTaskInfo();
        etlTaskInfo.setId(id);
        return etlTaskMapper.selectByPrimaryKey(etlTaskInfo);
    }

    @Override
    public int insert(EtlTaskInfo etlTaskInfo) {
        return etlTaskMapper.insert(etlTaskInfo);
    }

    @Override
    public int delete(long id) {
        return etlTaskMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int update(EtlTaskInfo etlTaskInfo) {
        return etlTaskMapper.updateByPrimaryKey(etlTaskInfo);
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
        String ids_str = "";
        for (Long id : ids) {
            etlTaskMapper.deleteBatchById(id.toString());
        }

        return 0;
    }

    @Override
    public List<EtlTaskInfo> select(EtlTaskInfo etlTaskInfo) {
        return etlTaskMapper.select(etlTaskInfo);
    }
}
