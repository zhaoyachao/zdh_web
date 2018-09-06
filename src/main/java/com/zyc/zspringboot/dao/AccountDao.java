package com.zyc.zspringboot.dao;

import java.util.List;

import com.zyc.zspringboot.entity.User;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;


/**
 * ClassName: AccountDao   
 * @author zyc-admin
 * @date 2018年2月6日  
 * @Description: TODO  
 */
public interface AccountDao {

	@Select(value="select * from account_info where user_name=#{userName} and user_password=#{password}")
	@Results({@Result(column="user_name",property="userName"),
		@Result(column="user_password",property="password")
	})
	public User findByPw(User user);
	
	@Select(value="select * from account_info where user_password=#{password}")
	@Results({@Result(column="user_name",property="userName"),
		@Result(column="user_password",property="password")
	})
	public List<User> findList(User user);
}
