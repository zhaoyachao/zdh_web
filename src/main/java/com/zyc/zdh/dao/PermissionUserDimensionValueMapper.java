package com.zyc.zdh.dao;

import com.zyc.notscan.base.BasePermissionUserDimensionValueMapper;
import com.zyc.zdh.entity.PermissionDimensionInfo;
import com.zyc.zdh.entity.PermissionUserDimensionValueInfo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface PermissionUserDimensionValueMapper extends BasePermissionUserDimensionValueMapper<PermissionUserDimensionValueInfo> {


    @Select({
            "<script>",
            "select distinct pdi.* from permission_user_dimension_value_info udv inner join permission_dimension_info pdi on udv.dim_code=pdi.dim_code and udv.user_account=#{user_account} and udv.product_code=pdi.product_code and udv.product_code=#{product_code}",
            "and udv.is_delete=0",
            "and pdi.is_delete=0",
            "</script>"

    })
    public List<PermissionDimensionInfo> selectDimByUser(@Param("product_code") String product_code, @Param("user_account") String user_account);


}