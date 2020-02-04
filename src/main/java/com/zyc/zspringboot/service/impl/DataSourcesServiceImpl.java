package com.zyc.zspringboot.service.impl;

import com.zyc.zspringboot.dao.DataSourcesMapper;
import com.zyc.zspringboot.entity.DataSourcesInfo;
import com.zyc.zspringboot.service.DataSourcesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DataSourcesServiceImpl implements DataSourcesService {

    @Autowired
    DataSourcesMapper dataSourcesMapper;


    @Override
    public DataSourcesInfo selectById(String id) {
        DataSourcesInfo dataSourcesInfo=new DataSourcesInfo();
        dataSourcesInfo.setId(id);
        return dataSourcesMapper.selectByPrimaryKey(dataSourcesInfo);
    }

    @Override
    public int insert(DataSourcesInfo dataSourcesInfo) {
        return dataSourcesMapper.insert(dataSourcesInfo);
    }

    @Override
    public int delete(long id) {
        return dataSourcesMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int update(DataSourcesInfo dataSourcesInfo) {
        return dataSourcesMapper.updateByPrimaryKey(dataSourcesInfo);
    }

    @Override
    public List<DataSourcesInfo> selectByExample(DataSourcesInfo dataSourcesInfo) {
        return dataSourcesMapper.selectByExample(dataSourcesInfo);
    }

    @Override
    public List<DataSourcesInfo> selectAll() {
        return dataSourcesMapper.selectAll();
    }

    @Override
    public int deleteBatchById(Long[] ids) {
        String ids_str = "";
        for (Long id : ids) {
            dataSourcesMapper.deleteBatchById(id.toString());
        }

        return 0;
    }

    @Override
    public List<DataSourcesInfo> select(DataSourcesInfo dataSourcesInfo) {
        return dataSourcesMapper.select(dataSourcesInfo);
    }
}
