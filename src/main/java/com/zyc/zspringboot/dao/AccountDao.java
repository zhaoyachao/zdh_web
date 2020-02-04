package com.zyc.zspringboot.dao;

import java.util.List;

import com.zyc.zspringboot.entity.User;
import org.apache.ibatis.annotations.*;


/**
 * ClassName: AccountDao   
 * @author zyc-admin
 * @date 2018年2月6日  
 * @Description: TODO  
 */
public interface AccountDao {

	@Select(value="select * from account_info where user_name=#{userName} and user_password=#{password}")
	@Results({@Result(column="id",property="id"),
			@Result(column="user_name",property="userName"),
		@Result(column="user_password",property="password"),
		@Result(column="email",property="email")
	})
	public User findByPw(User user);
	
	@Select(value="select * from account_info where user_password=#{password}")
	@Results({@Result(column="id",property="id"),
			@Result(column="user_name",property="userName"),
			@Result(column="user_password",property="password"),
			@Result(column="email",property="email")
	})
	public List<User> findList(User user);

	@Select(value="select * from account_info where user_name=#{userName}")
	@Results({@Result(column="id",property="id"),
			@Result(column="user_name",property="userName"),
			@Result(column="user_password",property="password"),
			@Result(column="email",property="email")
	})
	public List<User> findByUserName(User user);


	@Insert({ "insert into account_info(user_name, user_password,email) values(#{userName}, #{password},#{email})" })
	public int insert(User user);

}
