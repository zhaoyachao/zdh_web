package com.zyc.zdh.dao;

import com.zyc.notscan.base.BaseSurveyMapper;
import com.zyc.zdh.entity.SurveyInfo;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface SurveyMapper extends BaseSurveyMapper<SurveyInfo> {

    @Select("SELECT * FROM survey_info WHERE is_delete='0' ORDER BY create_time DESC")
    List<SurveyInfo> selectAllSurveys();

    @Delete({
            "<script>",
            "UPDATE survey_info SET is_delete='1' WHERE id IN ",
            "<foreach collection='ids' item='id' open='(' separator=',' close=')'>",
            "#{id}",
            "</foreach>",
            "</script>"
    })
    int deleteBatchByIds(@Param("ids") String[] ids);
}
