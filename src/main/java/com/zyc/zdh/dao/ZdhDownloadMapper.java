package com.zyc.zdh.dao;

import com.zyc.notscan.BaseMapper;
import com.zyc.zdh.entity.ZdhDownloadInfo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;


public interface ZdhDownloadMapper extends BaseMapper<ZdhDownloadInfo> {

    @Select({"<script>",
            "select * from zdh_download_info where owner=#{owner}",
            "<when test='file_name!=null and file_name !=\"\"'>",
            "AND file_name like #{file_name}",
            "</when>",
            "</script>"})
    public List<ZdhDownloadInfo> slectByOwner(@Param("owner") String owner,@Param("file_name")String file_name);


    @Select({"<script>",
            "select * from zdh_download_info where is_notice='false'",
            "</script>"})
    public List<ZdhDownloadInfo> selectNotice();
}