package com.zyc.zdh.dao;

import com.zyc.notscan.base.BaseApprovalEventMapper;
import com.zyc.zdh.entity.ApprovalEventInfo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * ClassName: ApprovalEventMapper
 * @author zyc-admin
 * @date 2021年10月12日
 * @Description: TODO  
 */
public interface ApprovalEventMapper extends BaseApprovalEventMapper<ApprovalEventInfo> {

    /**
     * 审批事件信息
     * @param event_context
     * @return
     */
    @Select({"<script>",
            "SELECT a.*,b.code_name,b.type FROM approval_event_info a inner join approval_config_info b on a.code=b.code ",
            "and a.product_code=b.product_code",
            "WHERE 1=1",
            "<when test='event_context!=null and event_context !=\"\"'>",
            "AND (a.code like #{event_context}",
            "or event_code like #{event_context}",
            "or event_context like #{event_context}",
            "or a.id like #{event_context})",
            "</when>",
            "and a.product_code in ",
            "<foreach collection='product_codes' item='product_code' open='(' separator=',' close=')'>",
            "#{product_code}",
            "</foreach>",
            "</script>"})
    public List<ApprovalEventInfo> selectByContext(@Param("event_context") String event_context, @Param("product_codes")List<String> product_codes);

    @Select({"<script>",
            "SELECT a.*,b.code_name,b.type FROM approval_event_info a inner join approval_config_info b on a.code=b.code",
            "WHERE a.id=#{id}",
            "</script>"})
    public ApprovalEventInfo selectById(@Param("id") String id);

}
