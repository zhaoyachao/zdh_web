package com.zyc.zdh.dao;

import com.zyc.notscan.BaseMapper;
import com.zyc.zdh.entity.ApplyAlarmInfo;
import com.zyc.zdh.entity.ApplyInfo;
import com.zyc.zdh.entity.ApplyIssueInfo;
import com.zyc.zdh.entity.ApprovalConfigInfo;
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
public interface ApprovalConfigMapper extends BaseMapper<ApprovalConfigInfo> {

    /**
     * 审批节点信息
     * @param approval_context
     * @return
     */
    @Select({"<script>",
            "SELECT * FROM approval_config_info",
            "WHERE 1=1",
            "<when test='approval_context!=null and approval_context !=\"\"'>",
            "AND (code like '%${approval_context}%'",
            "or code_name like '%${approval_context}%'",
            "or id like '%${approval_context}%'",
            "</when>",
            "</script>"})
    public List<ApprovalConfigInfo> selectByContext(@Param("approval_context") String approval_context);


}
