package com.zyc.zdh.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zyc.zdh.dao.AccountDao;
import com.zyc.zdh.entity.User;
import com.zyc.zdh.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * ClassName: AccountServiceImpl   
 * @author zyc-admin
 * @date 2018年2月6日  
 * @Description: TODO  
 */
@Deprecated
@Service("accountService")
public class AccountServiceImpl implements AccountService {

	@Autowired
	private AccountDao accountDao;
	
	@Override
	public User findByPw(User user) {
		
		return accountDao.findByPw(user);
	}

	@Override
	public List<User> findList(User user) {
		// TODO Auto-generated method stub
		@SuppressWarnings("rawtypes")
		Page startPage = PageHelper.startPage(user.getPageNum2(), user.getPageSize());
		List<User> findList = accountDao.findList(user);
		return findList;
	}

	@Override
	public List<User> findByUserName(User user) {
		return accountDao.findByUserName(user);
	}

	@Override
	public int insert(User user) {
		return accountDao.insert(user);
	}

	@Override
	public int updateUser(User user) {
		return accountDao.update(user);
	}

	@Override
	public User selectByPrimaryKey(String id) {
		return accountDao.selectByPrimaryKey(id);
	}

	@Override
	public List<User> findByUserName2(String[] user_names) {
		return accountDao.findByUserName2(user_names);
	}


}
