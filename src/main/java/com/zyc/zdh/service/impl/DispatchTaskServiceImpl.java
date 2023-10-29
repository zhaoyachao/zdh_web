package com.zyc.zdh.service.impl;

import com.zyc.zdh.dao.DispatchTaskMapper;
import com.zyc.zdh.entity.DispatchTaskInfo;
import com.zyc.zdh.service.DispatchTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DispatchTaskServiceImpl implements DispatchTaskService {

    @Autowired
    DispatchTaskMapper dispatchTaskMapper;


    @Override
    public DispatchTaskInfo selectById(String id) {
        DispatchTaskInfo dispatchTaskInfo=new DispatchTaskInfo();
        dispatchTaskInfo.setId(id);
        return dispatchTaskMapper.selectByPrimaryKey(dispatchTaskInfo);
    }

    @Override
    public int insert(DispatchTaskInfo dispatchTaskInfo) {
        return dispatchTaskMapper.insertSelective(dispatchTaskInfo);
    }

    @Override
    public int delete(long id) {
        return dispatchTaskMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int update(DispatchTaskInfo dispatchTaskInfo) {
        return dispatchTaskMapper.updateByPrimaryKeySelective(dispatchTaskInfo);
    }

    @Override
    public List<DispatchTaskInfo> selectByExample(DispatchTaskInfo dispatchTaskInfo) {
        return null;
    }

    @Override
    public List<DispatchTaskInfo> selectAll() {
        return dispatchTaskMapper.selectAll();
    }

    @Override
    public int deleteBatchById(Long[] ids) {
        String ids_str = "";
        for (Long id : ids) {
            dispatchTaskMapper.deleteBatchById(id.toString());
        }

        return 0;
    }

    @Override
    public List<DispatchTaskInfo> select(DispatchTaskInfo dispatchTaskInfo) {
        return dispatchTaskMapper.select(dispatchTaskInfo);
    }
}
