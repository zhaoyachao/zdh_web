package com.zyc.zdh.dao;

import com.zyc.notscan.base.BaseProcessFlowMapper;
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
public interface ProcessFlowMapper extends BaseProcessFlowMapper<ProcessFlowInfo> {

    @Select({
            "<script>",
            "select pfi.*,acc.user_name as by_person_name from process_flow_info pfi left join permission_user_info acc on pfi.owner=acc.user_account and pfi.product_code=acc.product_code where is_show=#{is_show} and ( find_in_set(#{auditor_id}, auditor_id) or agent_user=#{auditor_id} )",
            "<when test='context!=null and context !=\"\"'>",
            "and context like #{context}",
            "</when>",
            " and pfi.product_code in ",
            "<foreach collection='product_codes' item='product_code' open='(' separator=',' close=')'>",
            "#{product_code}",
            "</foreach>",
            "order by pfi.create_time desc",
            "</script>"

    })
    public List<ProcessFlowInfo> selectByAuditorId(@Param("is_show") String is_show, @Param("auditor_id") String auditor_id,@Param("context") String context, @Param("product_codes") List<String> product_codes);

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
            " and product_code in ",
            "<foreach collection='product_codes' item='product_code' open='(' separator=',' close=')'>",
            "#{product_code}",
            "</foreach>",
            "group by owner,flow_id",
            "order by create_time desc ",
            "</script>"

    })
    public List<ProcessFlowInfo> selectByOwner(@Param("owner") String owner,@Param("context") String context, @Param("product_codes") List<String> product_codes);

    @Select({
            "<script>",
            "select pfi.*,pfi.auditor_id as by_person_name from process_flow_info pfi where pfi.flow_id=#{flow_id} and owner = #{owner} ",
            "</script>"

    })
    public List<ProcessFlowInfo> selectByFlowId(@Param("flow_id") String flow_id, @Param("owner") String owner);

    @Update(
            {
                    "<script>",
                    "update process_flow_info set agent_user=#{agent_user} where id = #{id}",
                    "</script>"
            }
    )
    public int updateAgentUser(@Param("id") String id, @Param("agent_user") String agent_user);
}
