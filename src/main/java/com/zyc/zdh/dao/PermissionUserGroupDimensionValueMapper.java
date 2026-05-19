package com.zyc.zdh.dao;

import com.zyc.notscan.base.BasePermissionUserGroupDimensionValueMapper;
import com.zyc.zdh.entity.PermissionDimensionInfo;
import com.zyc.zdh.entity.PermissionUserGroupDimensionValueInfo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface PermissionUserGroupDimensionValueMapper extends BasePermissionUserGroupDimensionValueMapper<PermissionUserGroupDimensionValueInfo> {

    @Select({
            "<script>",
            "select distinct pdi.* from permission_usergroup_dimension_value_info udv inner join permission_dimension_info pdi on udv.dim_code=pdi.dim_code and udv.group_code=#{group_code} and udv.product_code=pdi.product_code and udv.product_code=#{product_code}",
            "and udv.is_delete=0",
            "and pdi.is_delete=0",
            "</script>"

    })
    public List<PermissionDimensionInfo> selectDimByGroup(@Param("product_code") String product_code, @Param("group_code") String group_code);
}