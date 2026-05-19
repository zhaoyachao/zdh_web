package com.zyc.zdh.dao;

import com.zyc.notscan.base.BasePermissionApplyMapper;
import com.zyc.zdh.entity.PermissionApplyInfo;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.SelectKey;
import tk.mybatis.mapper.provider.base.BaseInsertProvider;

public interface PermissionApplyMapper extends BasePermissionApplyMapper<PermissionApplyInfo> {
    @Override
    @InsertProvider(
            type = BaseInsertProvider.class,
            method = "dynamicSQL"
    )
    @SelectKey(statement="SELECT LAST_INSERT_ID()", keyProperty="id", before=false, resultType=String.class)
    int insert(PermissionApplyInfo permissionApplyInfo);
}