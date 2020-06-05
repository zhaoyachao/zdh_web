package com.zyc.zdh.shiro;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;

public class MyFormAuthenticationFilter extends FormAuthenticationFilter {

	// 登录失败，异常抛出
	@Override
	protected boolean onLoginFailure(AuthenticationToken token,
			AuthenticationException e, ServletRequest request,
			ServletResponse response) {
		String className = e.getClass().getName();
		if (e != null
				&& !UnknownAccountException.class.getName().equals(className)
				&& !IncorrectCredentialsException.class.getName().equals(
						className)
				&& !LockedAccountException.class.getName().equals(className)) { // 用户被锁定
			e.printStackTrace(); // 非验证异常抛出
		}
		return super.onLoginFailure(token, e, request, response);
	}

	// 重写认证通过后的页面跳转，shiro会默认跳转到上一次请求的页面，不适用于iframe的框架
	@Override
	protected void issueSuccessRedirect(ServletRequest request,
			ServletResponse response) throws Exception {
		// //aop不能拦截filter的内容，记录登录认证的日志
		// SysUserActiveVo sysUserActive = ShiroUser.getUser();
		// //更新用户登录时间
		// SysUser user = new SysUser();
		// user.setId(sysUserActive.getId());
		// user.setLastLoginTime(new Date());
		//
		// HttpServletRequest httpServletRequest=(HttpServletRequest)request;
		// String ipAddr = TcpipUtil.getIpAddr(httpServletRequest);
		// user.setLastLoginIp(ipAddr);
		// user.setLoginErrTimes(Integer.valueOf(0));
		// //清空登录出错信息
		// user.setStatus(SysUser.STATUS_ENABLE);
		// sysUserService.updateByPrimaryKeySelective(user);
		// 认证通过后的跳转地址
		System.out.println("认证通过后的跳转地址"+getSuccessUrl());
		WebUtils.issueRedirect(request, response, getSuccessUrl(), null, true);
	}

//	@Override
//	protected boolean onLoginSuccess(AuthenticationToken token,
//			Subject subject, ServletRequest request, ServletResponse response)
//			throws Exception {
//		System.out.println("onLoginSuccess=="+getSuccessUrl());
//		WebUtils.issueRedirect(request, response, getSuccessUrl());
//		return false;
//	}
	
	protected String getCaptcha(ServletRequest request) {
		return WebUtils.getCleanParam(request, "passKey");
	}

	@Override
	protected AuthenticationToken createToken(ServletRequest request,
			ServletResponse response) {
		System.out.println("create Token");
		String username = getUsername(request);
		String password = getPassword(request);
		boolean remberMe = isRememberMe(request);
		String host = "";
		String captcha = "";
		String ipAddr = "";
		return new MyAuthenticationToken(username, password, remberMe, host,
				captcha, ipAddr);
	}

}
