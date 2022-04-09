package com.zyc.zdh.dao;

import com.zyc.notscan.BaseMapper;
import com.zyc.zdh.entity.ApplyAlarmInfo;
import com.zyc.zdh.entity.ApplyInfo;
import com.zyc.zdh.entity.ApplyIssueInfo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * ClassName: DataSourcesMapper
 * @author zyc-admin
 * @date 2017年12月26日  
 * @Description: TODO  
 */
public interface ApplyMapper extends BaseMapper<ApplyInfo> {

    @Select({"<script>",
            "select * from apply_info where status=0 and is_notice != 'true'",
            "</script>"})
    public List<ApplyInfo> selectNotice();

    @Update({
            "<script>",
            "update apply_info set status=#{status} where id=#{id}",
            "</script>"
    })
    public int updateStatus(@Param("id") String id, @Param("status") String status);

    /**
     * 申请查询
     * @param apply_context
     * @param status
     * @param approve_id
     * @param owner
     * @return
     */
    @Select({"<script>",
            "SELECT * FROM apply_info",
            "WHERE 1=1",
            "<when test='owner!=null and owner !=\"\"'>",
            "AND owner=#{owner}",
            "</when>",
            "<when test='apply_context!=null and apply_context !=\"\"'>",
            "AND apply_context like #{apply_context}",
            "</when>",
            "<when test='approve_id!=null and approve_id !=\"\"'>",
            "AND approve_id = #{approve_id}",
            "</when>",
            "<when test='status!=null and status !=\"\"'>",
            "AND status = #{status}",
            "</when>",
            "</script>"})
    public List<ApplyInfo> selectByParams(@Param("apply_context") String apply_context, @Param("status") String status,
                                          @Param("approve_id") String approve_id, @Param("owner") String owner);


    /**
     * 申请数据源查询
     * @param apply_context
     * @param status
     * @param approve_id
     * @param owner
     * @return
     */
    @Select({"<script>",
            "SELECT a.*,b.issue_context,b.data_sources_choose_input,b.data_source_type_input,b.data_sources_table_name_input,b.data_sources_table_columns," +
                    "b.column_datas FROM apply_info a,issue_data_info b",
            "WHERE a.issue_id=b.id",
            "<when test='owner!=null and owner !=\"\"'>",
            "AND a.owner=#{owner}",
            "</when>",
            "<when test='apply_context!=null and apply_context !=\"\"'>",
            "AND a.apply_context like #{apply_context}",
            "</when>",
            "<when test='approve_id!=null and approve_id !=\"\"'>",
            "AND approve_id = #{approve_id}",
            "</when>",
            "<when test='status!=null and status !=\"\"'>",
            "AND a.status = #{status}",
            "</when>",
            "</script>"})
    public List<ApplyIssueInfo> selectByParams3(@Param("apply_context") String apply_context, @Param("status") String status,
                                                @Param("approve_id") String approve_id, @Param("owner") String owner);

    @Select({"<script>",
            "SELECT a.*,b.issue_context,b.data_sources_choose_input,b.data_source_type_input,b.data_sources_table_name_input,b.data_sources_table_columns," +
                    "b.column_datas FROM apply_info a,issue_data_info b",
            "WHERE a.issue_id=b.id and a.id=#{id}" ,
            "<when test='owner!=null and owner !=\"\"'>",
            "AND a.owner=#{owner}",
            "</when>",
            "<when test='approve_id!=null and approve_id !=\"\"'>",
            "AND approve_id = #{approve_id}",
            "</when>",
            "<when test='status!=null and status !=\"\"'>",
            "AND a.status = #{status}",
            "</when>",
            "</script>"})
    public ApplyIssueInfo selectByParams4(@Param("id") String id, @Param("status") String status,
                                          @Param("approve_id") String approve_id, @Param("owner") String owner);

    /**
     * 审批查询
     * @param apply_context
     * @param approve_id
     * @param owner
     * @return
     */
    @Select({"<script>",
            "SELECT * FROM apply_info",
            "WHERE 1=1",
            "<when test='owner!=null and owner !=\"\"'>",
            "AND owner=#{owner}",
            "</when>",
            "<when test='apply_context!=null and apply_context !=\"\"'>",
            "AND apply_context like #{apply_context}",
            "</when>",
            "<when test='approve_id!=null and approve_id !=\"\"'>",
            "AND approve_id = #{approve_id}",
            "</when>",
            "AND status != 3",
            "</script>"})
    public List<ApplyInfo> selectByParams2(@Param("apply_context") String apply_context,
                                           @Param("approve_id") String approve_id, @Param("owner") String owner);


    /**
     * 以申请的数据源查询(状态为以通过)
     * @param issue_id
     * @return
     */
    @Select({"<script>",
            "SELECT apply.*,acc.email FROM apply_info apply,account_info acc",
            "WHERE issue_id=${issue_id}",
            "AND apply.owner=acc.id",
            "AND status = 1",
            "</script>"})
    public List<ApplyAlarmInfo> selectByIssueId(@Param("issue_id") String issue_id);



}
