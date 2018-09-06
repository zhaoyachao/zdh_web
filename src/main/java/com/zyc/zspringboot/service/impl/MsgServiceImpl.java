package com.zyc.zspringboot.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.zyc.zspringboot.dao.MsgMapper;
import com.zyc.zspringboot.netty.MsgInfo;
import com.zyc.zspringboot.service.MsgService;

/**
 * ClassName: MsgServiceImpl   
 * @author zyc-admin
 * @date 2017年12月26日  
 * @Description: TODO  
 */
@Service("msgService")
public class MsgServiceImpl implements MsgService {

	@Autowired
	private MsgMapper msgMapper;
	
	@Override
	public MsgInfo selectById(long id) {
		
		return msgMapper.selectByPrimaryKey(id);
	}

	@Override
	public int insert(MsgInfo msgInfo) {
		// TODO Auto-generated method stub
		return msgMapper.insert(msgInfo);
	}

	@Override
	@Transactional(rollbackFor=Exception.class,propagation=Propagation.REQUIRED,value="txManager")
	public int delete(long id) {
		// TODO Auto-generated method stub
		return msgMapper.deleteByPrimaryKey(id);
	}

	@Override
	public int update(MsgInfo msgInfo) {
		// TODO Auto-generated method stub
		return msgMapper.updateByPrimaryKey(msgInfo);
	}

	@Override
	public List<MsgInfo> selectByExample(MsgInfo msgInfo) {
		// TODO Auto-generated method stub
		return msgMapper.selectByExample(msgInfo);
	}

}
