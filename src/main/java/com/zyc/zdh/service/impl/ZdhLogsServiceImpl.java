package com.zyc.zdh.service.impl;

import com.zyc.zdh.dao.ZdhLogsMapper;
import com.zyc.zdh.entity.ZdhLogs;
import com.zyc.zdh.service.ZdhLogsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Service
public class ZdhLogsServiceImpl implements ZdhLogsService {

    @Autowired
    ZdhLogsMapper zdhLogsMapper;


    @Override
    public ZdhLogs selectById(String id) {
        ZdhLogs zdhLogs=new ZdhLogs();
        zdhLogs.setJob_id(id);
        return zdhLogsMapper.selectByPrimaryKey(zdhLogs);
    }

    @Override
    public int insert(ZdhLogs zdhLogs) {
        return zdhLogsMapper.insertSelective(zdhLogs);
    }

    @Override
    public int delete(long id) {
        return zdhLogsMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int update(ZdhLogs zdhLogs) {
        return zdhLogsMapper.updateByPrimaryKeySelective(zdhLogs);
    }

    @Override
    public List<ZdhLogs> selectByExample(ZdhLogs zdhLogs) {
        return null;
    }

    @Override
    public List<ZdhLogs> selectAll() {
        return zdhLogsMapper.selectAll();
    }

    @Override
    public int deleteBatchById(Long[] ids) {
        String ids_str = "";
        for (Long id : ids) {
            zdhLogsMapper.deleteBatchById(id.toString());
        }

        return 0;
    }

    @Override
    public List<ZdhLogs> select(ZdhLogs zdhLogs) {
        return zdhLogsMapper.select(zdhLogs);
    }

    @Override
    public List<ZdhLogs> selectByTime(String job_id, String task_log_id,Timestamp start_time, Timestamp end_time,String levels) {
        return zdhLogsMapper.selectByTime(job_id,task_log_id,start_time,end_time,levels);
    }

    @Override
    public List<ZdhLogs> selectByTime(String job_id) {
        return zdhLogsMapper.selectByJobId(job_id);
    }

    @Override
    public int deleteByTime(String id,String task_log_id, Timestamp start_time, Timestamp end_time) {
        return zdhLogsMapper.deleteByTime(id,task_log_id,start_time,end_time);
    }
}
