package com.zyc.zdh.dao.secondary;

import com.zyc.notscan.secondary.SecondaryBaseTestMapper;
import com.zyc.zdh.entity.secondary.TestInfo;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * 第二数据源 Mapper 示例
 * 此 Mapper 将使用 dataSource2 数据源
 */
public interface SecondaryDataMapper extends SecondaryBaseTestMapper<TestInfo> {

    /**
     * 查询第二数据源的表信息
     */
    @Select("SHOW TABLES")
    List<Map<String, Object>> showTables();

    /**
     * 查询第二数据源的数据库版本
     */
    @Select("SELECT VERSION()")
    String getDatabaseVersion();

    /**
     * 执行自定义查询
     */
    @Select("SELECT * FROM information_schema.tables WHERE table_schema = DATABASE() LIMIT 10")
    List<Map<String, Object>> getTableInfo();
}
