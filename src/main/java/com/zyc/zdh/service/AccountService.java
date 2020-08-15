package com.zyc.zdh.service;

import java.util.List;

import com.zyc.zdh.entity.User;

/**
 * ClassName: AccountService   
 * @author zyc-admin
 * @date 2018年2月6日  
 * @Description: TODO  
 */
public interface AccountService {

	public User findByPw(User user);
	
	public List<User> findList(User user);

	public List<User> findByUserName(User user);

	public int insert(User user);

	public int updateUser(User user);

	public User selectByPrimaryKey(String id);

	public List<User> findByUserName2(String[] user_names);
}
