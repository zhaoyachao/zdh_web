package com.zyc.zdh.dao;

import com.zyc.notscan.BaseMapper;
import com.zyc.zdh.entity.EtlTaskInfo;
import com.zyc.zdh.entity.IssueDataInfo;
import com.zyc.zdh.entity.QuotaInfo;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * ClassName: DataSourcesMapper
 * @author zyc-admin
 * @date 2017年12月26日  
 * @Description: TODO  
 */
public interface IssueDataMapper extends BaseMapper<IssueDataInfo> {


    @Delete({
            "<script>",
            "delete from issue_data_info where id in ",
            "<foreach collection='ids' item='id' open='(' separator=',' close=')'>",
            "#{id}",
            "</foreach>",
            "</script>"

    })
    public int deleteBatchByIds(@Param("ids") String[] ids);


    /**
     * 获取以发布完成的数据
     * @param issue_context
     * @return
     */
    @Select({"<script>",
            "SELECT * FROM issue_data_info",
            "WHERE status=1",
            "<when test='issue_context!=null and issue_context !=\"\"'>",
            "AND ( issue_context like #{issue_context}",
            "OR data_sources_table_name_input like #{issue_context})",
            "</when>",
            "<when test='label_params!=null and label_params.length>0'>",
            "and ",
            "<foreach collection='label_params' item='label_name' separator='and'>",
            "FIND_IN_SET('${label_name}',label_params)",
            "</foreach >",
            "</when>",
            "</script>"})
    public List<IssueDataInfo> selectByParams(@Param("issue_context") String issue_context,@Param("label_params") String[] label_params);


    @Select({"<script>",
            "SELECT * FROM issue_data_info",
            "WHERE owner=#{owner}",
            "<when test='issue_context!=null and issue_context !=\"\"'>",
            "AND ( issue_context like #{issue_context}",
            "OR data_sources_table_name_input like #{issue_context})",
            "</when>",
            "</script>"})
    public List<IssueDataInfo> selectByOwner(@Param("issue_context") String issue_context,@Param("owner") String owner);


    @Select({"<script>",
            "SELECT issue.*,acc.user_name FROM issue_data_info issue, account_info acc",
            "WHERE issue.owner=acc.user_name and issue.id=#{id}" ,
            "</script>"})
    public IssueDataInfo selectById(@Param("id") String id);



}
