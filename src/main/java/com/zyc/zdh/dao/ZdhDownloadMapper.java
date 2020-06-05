package com.zyc.zdh.dao;

import com.zyc.notscan.BaseMapper;
import com.zyc.zdh.entity.EmailTaskLogs;
import com.zyc.zdh.entity.EtlEcharts;
import com.zyc.zdh.entity.TaskLogs;
import com.zyc.zdh.entity.ZdhDownloadInfo;
import org.apache.ibatis.annotations.*;

import java.sql.Timestamp;
import java.util.List;


public interface ZdhDownloadMapper extends BaseMapper<ZdhDownloadInfo> {

    @Select({"<script>",
            "select * from zdh_download_info where owner=#{owner}",
            "<when test='file_name!=null and file_name !=\"\"'>",
            "AND file_name like '%${file_name}%'",
            "</when>",
            "</script>"})
    public List<ZdhDownloadInfo> slectByOwner(@Param("owner") String owner,@Param("file_name")String file_name);


    @Select({"<script>",
            "select * from zdh_download_info where down_count=0",
            "</script>"})
    public List<ZdhDownloadInfo> selectNotice();
}