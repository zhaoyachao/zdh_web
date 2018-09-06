package com.zyc.zspringboot.api;

import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zyc.zspringboot.entity.ResultInfo;
import com.zyc.zspringboot.entity.User;

/**
 * ClassName: LoginService   
 * @author zyc-admin
 * @date 2018年2月5日  
 * @Description: cloud.api包下的服务 不需要通过shiro验证拦截，需要自定义的token验证  
 */
@Controller("loginService")
@RequestMapping("api")
public class LoginService {
	
	@RequestMapping("login")
	@ResponseBody
	public ResultInfo login(User user){
		
		return new ResultInfo();
	}
}
