package com.zyc.zdh.dao;

import com.zyc.notscan.BaseMapper;
import com.zyc.zdh.entity.EveryDayNotice;
import com.zyc.zdh.entity.ZdhLogs;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.sql.Timestamp;
import java.util.List;

/**
 * ClassName: EveryDayNoticeMapper
 * @author zyc-admin
 * @date 2017年12月26日  
 * @Description: TODO  
 */
public interface EveryDayNoticeMapper extends BaseMapper<EveryDayNotice> {

}
