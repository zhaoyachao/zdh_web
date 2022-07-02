package com.zyc.zdh.dao;

import com.zyc.notscan.BaseMapper;
import com.zyc.zdh.entity.ApprovalAuditorInfo;
import com.zyc.zdh.entity.ProcessFlowInfo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * ClassName: ApprovalAuditorMapper
 * @author zyc-admin
 * @date 2021年10月19日
 * @Description: TODO  
 */
public interface ProcessFlowMapper extends BaseMapper<ProcessFlowInfo> {

    @Select({
            "<script>",
            "select pfi.*,acc.user_name as by_person_name from process_flow_info pfi left join account_info acc on pfi.owner=acc.id where is_show=#{is_show} and find_in_set(#{auditor_id}, auditor_id) ",
            "<when test='context!=null and context !=\"\"'>",
            "and context like #{context}",
            "</when>",
            "</script>"

    })
    public List<ProcessFlowInfo> selectByAuditorId(@Param("is_show") String is_show, @Param("auditor_id") String auditor_id,@Param("context") String context);

    @Update(
            {
                    "<script>",
                    "update process_flow_info set status=#{status} where id = #{id}",
                    "</script>"
            }
    )
    public int updateStatus(@Param("id") String id, @Param("status") String status);

    @Update(
            {
                    "<script>",
                    "update process_flow_info set status=#{status} where flow_id = #{flow_id} and status=0",
                    "</script>"
            }
    )
    public int updateStatus2(@Param("flow_id") String flow_id, @Param("status") String status);

    @Update(
            {
                    "<script>",
                    "update process_flow_info set is_show=#{is_show} where pre_id = #{pre_id}",
                    "</script>"
            }
    )
    public int updateIsShow(@Param("pre_id") String pre_id, @Param("is_show") String is_show);

    @Update(
            {
                    "<script>",
                    "update process_flow_info set is_end=#{is_end} where id = #{id}",
                    "</script>"
            }
    )
    public int updateIsEnd(@Param("id") String id, @Param("is_end") String is_end);

    @Select({
            "<script>",
            "select  min(create_time) as create_time,owner,flow_id as id, min(context) as context,  case when max(status) > 1 then max(status)  when min(status) = 0 then min(status) when max(status) = 1 then '1' end as status from process_flow_info pfi  where pfi.owner=#{owner} ",
            "<when test='context!=null and context !=\"\"'>",
            "and context like #{context}",
            "</when>",
            "group by owner,flow_id",
            "</script>"

    })
    public List<ProcessFlowInfo> selectByOwner(@Param("owner") String owner,@Param("context") String context);

    @Select({
            "<script>",
            "select pfi.*,'${auditor_id}' as by_person_name from process_flow_info pfi where pfi.flow_id=#{flow_id} and find_in_set('${auditor_id}', auditor_id) ",
            "</script>"

    })
    public List<ProcessFlowInfo> selectByFlowId(@Param("flow_id") String flow_id, @Param("auditor_id") String auditor_id);
}
