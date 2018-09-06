package com.zyc.zspringboot.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.zyc.zspringboot.entity.Role;
import com.zyc.zspringboot.service.RoleService;

@Controller
public class LoginController {

	private static Logger logger = LoggerFactory
			.getLogger(LoginController.class);
	@Autowired
	private RoleService roleService;

	@RequestMapping("hello")
	public String hello() {
		
		return "index";
	}

	@RequestMapping("login")
	public String login(Model model, HttpServletRequest request,HttpServletResponse response) throws Exception {
		System.out.println("login =======start==");
		Subject subject = SecurityUtils.getSubject();
		if (subject.isAuthenticated()) { // 已经登录，重新登录
			WebUtils.issueRedirect(request, response, "/getIndex");
			//SecurityUtils.getSecurityManager().logout(subject);
			return "login";
		}
//		 ShiroUser user = (ShiroUser) subject.getPrincipal();
//
//		 if(user != null){
//		 model.addAttribute("loginName",user.getUserName());
//		 }
//		 String exceptionClassName = (String)
//		 request.getAttribute("shiroLoginFailure");
//		 if(exceptionClassName!=null){
//		 if
//		 (UnknownAccountException.class.getName().equals(exceptionClassName))
//		 {
//		 model.addAttribute("warn","账号不存在");
//		 } else if (AuthenticationException.class.getName().equals(
//		 exceptionClassName)) {
//		 String msg="用户名/密码错误";
//		 model.addAttribute("warn",msg);
//		 }else {
//		 model.addAttribute("warn","登录异常");
//		 }
//		 }
		System.out.println("login ======end===");
		return "login";
	}

	@RequestMapping("getRole")
	@ResponseBody
	public String getRole() {
	/*	List<Role> role = roleService.getRole("1");
		System.out.println(role.get(0).getRoleName() + "=="
				+ role.get(0).getId());
		logger.info(role.toString());*/
		return "index";
	}

	@RequestMapping("getIndex")
	public String getIndex() {
		return "index";
	}

	@RequestMapping("getMyJsp")
	public String getMyJsp() {
		return "MyJsp";
	}
	@RequestMapping("getWelcome")
	public String getWelcome(){
		return "main/welcome";
	}
	
	@RequestMapping("logout")
	public String logout(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		Subject subject = SecurityUtils.getSubject();
		subject.logout();
		System.out.println("logout");
		//WebUtils.issueRedirect(req, resp, "/login");
		return "redirect:login";
	}

}
