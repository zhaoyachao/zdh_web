package com.zyc.zdh.dao;

import com.zyc.notscan.base.BaseWeMockTreeMapper;
import com.zyc.zdh.entity.WeMockTreeInfo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

public interface WeMockTreeMapper extends BaseWeMockTreeMapper<WeMockTreeInfo> {

    @Update(value = "update we_mock_tree_info  set parent=#{parent},level=#{level} where id=#{id}")
    public int updateParentById(@Param("id") String id, @Param("parent") String parent, @Param("level") String level);
}