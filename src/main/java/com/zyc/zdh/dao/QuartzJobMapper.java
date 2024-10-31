package com.zyc.zdh.dao;

import com.zyc.notscan.base.BaseQuartzJobMapper;
import com.zyc.zdh.entity.QuartzJobInfo;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;


public interface QuartzJobMapper extends BaseQuartzJobMapper<QuartzJobInfo> {


    @Update({
            "<script>",
            "update ${table_name} set is_delete=1 , update_time = #{update_time} where job_id in ",
            "<foreach collection='ids' item='id' open='(' separator=',' close=')'>",
            "#{id}",
            "</foreach>",
            "</script>"
    }
    )
    @Override
    public int deleteLogicByIds(@Param("table_name") String table_name, @Param("ids") String[] ids, @Param("update_time") Timestamp update_time);


    @Select({
            "<script>",
            "select * from ${table_name} where job_id in ",
            "<foreach collection='ids' item='id' open='(' separator=',' close=')'>",
            "#{id}",
            "</foreach>",
            "</script>"
    }
    )
    @Override
    public List<QuartzJobInfo> selectObjectByIds(@Param("table_name") String table_name, @Param("ids") String[] ids);

    @Select({
            "<script>",
            "select * from ${table_name} where job_id in ",
            "<foreach collection='ids' item='id' open='(' separator=',' close=')'>",
            "#{id}",
            "</foreach>",
            "</script>"
    }
    )
    @Override
    public List<Map<String,Object>> selectListMapByIds(@Param("table_name") String table_name, @Param("ids") String[] ids);

    @Update({ "update quartz_job_info set status = #{status} where job_id = #{job_id}" })
    public int updateStatus(@Param("job_id") String job_id,@Param("status") String status);

    @Update({ "update quartz_job_info set status = #{status} ,last_status = #{last_status} where job_id = #{job_id}" })
    public int updateStatus2(@Param("job_id") String job_id,@Param("status") String status,@Param("last_status") String last_status);

    @Update({ "update quartz_job_info set last_status = #{last_status} where job_id = #{job_id}" })
    public int updateLastStatus(@Param("job_id") String job_id,@Param("last_status") String last_status);

    @Update({ "update quartz_job_info set status = #{status},last_time = #{last_time} where job_id = #{job_id}" })
    public int updateStatusLastTime(@Param("job_id") String job_id,@Param("status") String status,@Param("last_time") Timestamp last_time);

    @Update({ "update quartz_job_info set task_log_id = #{task_log_id} where job_id = #{job_id}" })
    public int updateTaskLogId(@Param("job_id") String job_id,@Param("task_log_id") String task_log_id);


    @Select({ "<script>",
            "select * from quartz_job_info where is_delete=0",
            "and product_code in ",
            "<foreach collection='product_codes' item='product_code' open='(' separator=',' close=')'>",
            "#{product_code}",
            "</foreach>",
            "and dim_group in ",
            "<foreach collection='dim_groups' item='dim_group' open='(' separator=',' close=')'>",
            "#{dim_group}",
            "</foreach>",
            "<when test='product_code!=null and product_code !=\"\"'>",
            "AND product_code = #{product_code}",
            "</when>",
            "<when test='dim_group!=null and dim_group !=\"\"'>",
            "AND dim_group = #{dim_group}",
            "</when>",
            "</script>"
    })
    public List<QuartzJobInfo> selectByOwner(@Param("owner") String owner,
                                             @Param("product_code") String product_code, @Param("dim_group") String dim_group,
                                             @Param("product_codes") List<String> product_codes,
                                             @Param("dim_groups") List<String> dim_groups);

    //finish,etl,error
    @Select(value = "select count(1),last_status from quartz_job_info where owner=#{owner} and is_delete=0 group by last_status")
    public int selectCountByOwnerStatus(@Param("owner") String owner,@Param("last_status") String last_status);

    @Select(value = "select count(1) from quartz_job_info where owner=#{owner}")
    public int selectCountByOwner(@Param("owner") String owner);

    @Select({
            "<script>",
            "select",
            "*",
            "from quartz_job_info",
            "where is_delete=0",
            " AND status='running'",
            "<when test='job_ids!=null and job_ids.size > 0'>",
            " and job_id in ",
            "<foreach collection='job_ids' item='job_id' open='(' separator=',' close=')'>",
            "#{job_id}",
            "</foreach>",
            "</when>",
            "and product_code in ",
            "<foreach collection='product_codes' item='product_code' open='(' separator=',' close=')'>",
            "#{product_code}",
            "</foreach>",
            "and dim_group in ",
            "<foreach collection='dim_groups' item='dim_group' open='(' separator=',' close=')'>",
            "#{dim_group}",
            "</foreach>",
            "</script>"
    })
    public List<QuartzJobInfo> selectRunJobByOwner(@Param("job_ids") List<String> job_ids, @Param("product_codes") List<String> product_codes,
                                                   @Param("dim_groups") List<String> dim_groups);


    @Select({"<script>",
            "SELECT * FROM quartz_job_info",
            "WHERE is_delete=0",
            "<when test='etl_context!=null and etl_context !=\"\"'>",
            "AND etl_context like #{etl_context}",
            "</when>",
            "<when test='job_context!=null and job_context !=\"\"'>",
            "AND ( job_context like #{job_context} ",
            " or job_id like #{job_context}" ,
            " or jsmind_data like #{job_context}" ,
            " or alarm_account like #{job_context}" ,
            " or job_model like #{job_context}" ,
            " or params like #{job_context} )" ,
            "</when>",
            "<when test='status!=null and status !=\"\" and status == \"no_use\"'>",
            "AND status in ('create','remove')",
            "</when>",
            "<when test='status!=null and status !=\"\" and status != \"no_use\" '>",
            "AND status = #{status}",
            "</when>",
            "<when test='last_status!=null and last_status !=\"\"'>",
            "AND last_status = #{last_status}",
            "</when>",
            "and product_code in ",
            "<foreach collection='product_codes' item='product_code' open='(' separator=',' close=')'>",
            "#{product_code}",
            "</foreach>",
            "and dim_group in ",
            "<foreach collection='dim_groups' item='dim_group' open='(' separator=',' close=')'>",
            "#{dim_group}",
            "</foreach>",
            "<when test='product_code!=null and product_code !=\"\"'>",
            "AND product_code = #{product_code}",
            "</when>",
            "<when test='dim_group!=null and dim_group !=\"\"'>",
            "AND dim_group = #{dim_group}",
            "</when>",
            "</script>"})
    public List<QuartzJobInfo> selectByParams(@Param("owner") String owner,@Param("job_context") String job_context,@Param("etl_context") String etl_context,
                                              @Param("status") String status,@Param("last_status") String last_stauts,
                                              @Param("product_code") String product_code, @Param("dim_group") String dim_group,
                                              @Param("product_codes") List<String> product_codes,
                                              @Param("dim_groups") List<String> dim_groups);

    @Select({
            "<script>",
            "select",
            "*",
            "from quartz_job_info",
            "where is_delete=0",
            "<when test='last_status!=null and last_status!=\"\"'>",
            " and last_status=#{last_status}",
            "</when>",
            "</script>"
    })
    public List<QuartzJobInfo> selectByLastStatus(@Param("last_status") String last_status );

    @Delete({
            "<script>",
            "delete from quartz_job_info where job_type in ('email','retry','check')",
            "</script>"
    })
    public int deleteSystemJob();

    @Select({
            "<script>",
            "select",
            "*",
            "from quartz_job_info",
            "where is_delete=0",
            "<when test='job_type!=null and job_type!=\"\"'>",
            " and job_type=#{job_type}",
            "</when>",
            "</script>"
    })
    public List<QuartzJobInfo> selectByJobType(@Param("job_type") String job_type );


}