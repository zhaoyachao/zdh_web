package com.zyc.zdh.service.impl;

import com.zyc.zdh.dao.secondary.SecondaryDataMapper;
import com.zyc.zdh.service.SecondaryDataSourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 第二数据源使用示例 Service
 */
@Service
public class SecondaryDataSourceServiceImpl implements SecondaryDataSourceService {

    @Autowired
    private SecondaryDataMapper secondaryDataMapper;

    /**
     * 获取第二数据源的表信息
     */
    @Override
    public List<Map<String, Object>> getTablesFromSecondary() {
        return secondaryDataMapper.showTables();
    }

    /**
     * 获取第二数据源的数据库版本
     */
    @Override
    public String getSecondaryDatabaseVersion() {
        return secondaryDataMapper.getDatabaseVersion();
    }

    /**
     * 获取第二数据源的表详细信息
     */
    @Override
    public List<Map<String, Object>> getTableInfoFromSecondary() {
        return secondaryDataMapper.getTableInfo();
    }
}
