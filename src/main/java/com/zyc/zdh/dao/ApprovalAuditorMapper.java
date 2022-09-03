package com.zyc.zdh.dao;

import com.zyc.notscan.BaseMapper;
import com.zyc.zdh.entity.ApprovalAuditorInfo;
import com.zyc.zdh.entity.ApprovalConfigInfo;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * ClassName: ApprovalAuditorMapper
 * @author zyc-admin
 * @date 2017年12月26日  
 * @Description: TODO  
 */
public interface ApprovalAuditorMapper extends BaseMapper<ApprovalAuditorInfo> {

    /**
     * 审批人信息
     * @param approval_context
     * @return
     */
    @Select({"<script>",
            "SELECT a.*,b.code_name,b.type,c.name as auditor_group_name,d.user_name FROM approval_auditor_info a inner join approval_config_info b on a.code=b.code left join user_group_info c on a.auditor_group=c.id",
            " left join permission_user_info d on a.auditor_id=d.user_account",
            "WHERE 1=1",
            "<when test='approval_context!=null and approval_context !=\"\"'>",
            "AND (a.code like #{approval_context}",
            "or auditor_group like #{approval_context}",
            "or a.id like #{approval_context})",
            "</when>",
            "<when test='auditor_id!=null and auditor_id !=\"\"'>",
            "AND auditor_id=#{auditor_id}",
            "</when>",
            "</script>"})
    public List<ApprovalAuditorInfo> selectByContext(@Param("approval_context") String approval_context,@Param("auditor_id") String auditor_id);

    @Select({"<script>",
            "SELECT a.*,b.code_name,b.type FROM approval_auditor_info a inner join approval_config_info b on a.code=b.code",
            "WHERE a.id=#{id}",
            "</script>"})
    public ApprovalAuditorInfo selectById(@Param("id") String id);

    @Select({
            "<script>",
            "select auditor.* from approval_auditor_info auditor inner join approval_config_info config on auditor.code = config.code ",
            " inner join approval_event_info event on event.code=config.code and event.event_code=#{event}",
            "<when test='product_code!=null and product_code !=\"\"'>",
            " AND product_code=#{product_code}",
            "</when>",
            " where FIND_IN_SET('${auditor_group}', auditor.auditor_group)>0",
            "</script>"
    })
    public List<ApprovalAuditorInfo> selectByEvent(@Param("event") String event, @Param("auditor_group") String auditor_group, @Param("product_code") String product_code);

    @Delete({
            "<script>",
            "delete from approval_auditor_info where id in ",
            "<foreach collection='ids' item='id' open='(' separator=',' close=')'>",
            "#{id}",
            "</foreach>",
            "</script>"
    })
    public int deleteByPrimaryKeys(@Param("ids")String[] ids);
}
