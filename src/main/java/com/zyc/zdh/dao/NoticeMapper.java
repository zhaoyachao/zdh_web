package com.zyc.zdh.dao;

import com.zyc.notscan.base.BaseNoticeMapper;
import com.zyc.zdh.entity.NoticeInfo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * ClassName: NoticeMapper
 * @author zyc-admin
 * @date 2021年09月19日
 * @Description: TODO  
 */
public interface NoticeMapper extends BaseNoticeMapper<NoticeInfo> {


    @Select({"<script>",
            "SELECT * FROM notice_info",
            "WHERE owner=#{owner}",
            "<when test='message!=null and message !=\"\"'>",
            "AND ( msg_type like #{message}",
            "OR msg_title like #{message} ",
            "OR msg like #{message})",
            "</when>",
            "order by create_time desc",
            "</script>"})
    public List<NoticeInfo> selectByMessage(@Param("message") String message,@Param("owner") String owner);

    @Update({
            "<script>",
            "update notice_info set is_see=#{is_see} where id in ",
            "<foreach collection='ids' item='id' open='(' separator=',' close=')'>",
            "#{id}",
            "</foreach >",
            "</script>",
    })
    public int updateIsSeeByIds(@Param("ids") String[] ids,@Param("is_see") String is_see);
}
