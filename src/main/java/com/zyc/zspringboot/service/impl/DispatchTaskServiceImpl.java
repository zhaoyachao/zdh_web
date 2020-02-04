package com.zyc.zspringboot.service.impl;

import com.zyc.zspringboot.dao.DispatchTaskMapper;
import com.zyc.zspringboot.dao.EtlTaskMapper;
import com.zyc.zspringboot.entity.DispatchTaskInfo;
import com.zyc.zspringboot.entity.EtlTaskInfo;
import com.zyc.zspringboot.service.DispatchTaskService;
import com.zyc.zspringboot.service.EtlTaskService;
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
        return dispatchTaskMapper.insert(dispatchTaskInfo);
    }

    @Override
    public int delete(long id) {
        return dispatchTaskMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int update(DispatchTaskInfo dispatchTaskInfo) {
        return dispatchTaskMapper.updateByPrimaryKey(dispatchTaskInfo);
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
