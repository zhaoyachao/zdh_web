package com.zyc.zdh.service;

import java.util.List;
import java.util.Map;

public interface SecondaryDataSourceService {
    /**
     * 获取第二数据源的表信息
     */
    public List<Map<String, Object>> getTablesFromSecondary();

    /**
     * 获取第二数据源的数据库版本
     */
    public String getSecondaryDatabaseVersion();

    /**
     * 获取第二数据源的表详细信息
     */
    public List<Map<String, Object>> getTableInfoFromSecondary();
}
