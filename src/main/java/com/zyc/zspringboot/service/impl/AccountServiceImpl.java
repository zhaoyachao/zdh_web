package com.zyc.zspringboot.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zyc.zspringboot.dao.AccountDao;
import com.zyc.zspringboot.entity.User;
import com.zyc.zspringboot.service.AccountService;

/**
 * ClassName: AccountServiceImpl   
 * @author zyc-admin
 * @date 2018年2月6日  
 * @Description: TODO  
 */
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
		System.out.println("startPage.getTotal()------"+startPage.getTotal());
		return findList;
	}

}
