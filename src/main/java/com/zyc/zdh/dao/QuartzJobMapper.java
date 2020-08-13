package com.zyc.zdh.dao;

import com.zyc.notscan.BaseMapper;
import com.zyc.zdh.entity.QuartzJobInfo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;


public interface QuartzJobMapper extends BaseMapper<QuartzJobInfo> {

    @Update({ "update quartz_job_info set status = #{status} where job_id = #{job_id}" })
    public int updateStatus(@Param("job_id") String job_id,@Param("status") String status);

    @Update({ "update quartz_job_info set last_status = #{last_status} where job_id = #{job_id}" })
    public int updateLastStatus(@Param("job_id") String job_id,@Param("last_status") String last_status);


    @Select(value="select * from quartz_job_info where owner=#{owner}")
    public List<QuartzJobInfo> selectByOwner(@Param("owner") String owner);

    //finish,etl,error
    @Select(value = "select count(1),last_status from quartz_job_info where owner=#{owner} group by last_status")
    public int selectCountByOwnerStatus(@Param("owner") String owner,@Param("last_status") String last_status);

    @Select(value = "select count(1) from quartz_job_info where owner=#{owner}")
    public int selectCountByOwner(@Param("owner") String owner);

    @Select({
            "<script>",
            "select",
            "*",
            "from quartz_job_info",
            "where owner=#{owner}",
            " AND status='running'",
            "<when test='job_ids!=null and job_ids.size > 0'>",
            " and job_id in ",
            "<foreach collection='job_ids' item='job_id' open='(' separator=',' close=')'>",
            "#{job_id}",
            "</foreach>",
            "</when>",
            "</script>"
    })
    public List<QuartzJobInfo> selectRunJobByOwner(@Param("owner") String owner,@Param("job_ids") List<String> job_ids );


    @Select({"<script>",
            "SELECT * FROM quartz_job_info",
            "WHERE owner=#{owner}",
            "<when test='etl_context!=null and etl_context !=\"\"'>",
            "AND etl_context like '%${etl_context}%'",
            "</when>",
            "<when test='job_context!=null and job_context !=\"\"'>",
            "AND job_context like '%${job_context}%'",
            "</when>",
            "</script>"})
    public List<QuartzJobInfo> selectByParams(@Param("owner") String owner,@Param("job_context") String job_context,@Param("etl_context") String etl_context);

    @Select({
            "<script>",
            "select",
            "*",
            "from quartz_job_info",
            "where",
            "<when test='last_status!=null and last_status!=\"\"'>",
            " last_status=#{last_status}",
            "</when>",
            "</script>"
    })
    public List<QuartzJobInfo> selectByLastStatus(@Param("last_status") String last_status );


}