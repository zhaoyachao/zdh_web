package com.zyc.zdh.service.impl;

import com.zyc.zdh.dao.DataSourcesMapper;
import com.zyc.zdh.entity.DataSourcesInfo;
import com.zyc.zdh.service.DataSourcesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 4.7.16及之后版本废弃,不在使用
 */
@Deprecated
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
        return dataSourcesMapper.insertSelective(dataSourcesInfo);
    }

    @Override
    public int delete(long id) {
        return dataSourcesMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int update(DataSourcesInfo dataSourcesInfo) {
        return dataSourcesMapper.updateByPrimaryKeySelective(dataSourcesInfo);
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
        return 0;
    }


    @Override
    public List<DataSourcesInfo> select(DataSourcesInfo dataSourcesInfo) {
        return dataSourcesMapper.select(dataSourcesInfo);
    }

    @Override
    public List<String> selectDataSourcesType() {
        return dataSourcesMapper.selectDataSourcesType();
    }

    @Override
    public List<DataSourcesInfo> selectByParams(String data_source_context, String data_source_type, String url, String owner) {
        return null;
    }

}
