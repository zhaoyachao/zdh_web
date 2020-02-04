package com.zyc.zspringboot.shiro;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.subject.PrincipalCollection;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zyc.zspringboot.entity.User;
import com.zyc.zspringboot.service.AccountService;
import com.zyc.zspringboot.util.SpringContext;

public class MyRealm extends AuthorizingRealm {

	@Override
	public CacheManager getCacheManager() {
		// TODO Auto-generated method stub
		return super.getCacheManager();
	}

	/**
	 * 权限认证
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(
			PrincipalCollection principals) {
		System.out.println("授权=====");
		// 获取登录用户的信息,在认证时存储的是ShiroUser 所以得到的就是ShiroUser
		// 在其他地方也可通过SecurityUtils.getSubject().getPrincipals()获取用户信息
		String userName = principals.getPrimaryPrincipal().toString();
		// 权限字符串
		List<String> permissions = new ArrayList<>();
		// 从数据库中获取对应权限字符串并存储permissions
		permissions.add("111");
		// 角色字符串
		List<String> roles = new ArrayList<>();
		// 从数据库中获取对应角色字符串并存储roles

		SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
		simpleAuthorizationInfo.addStringPermissions(permissions);
		simpleAuthorizationInfo.addRoles(roles);// 角色类型

		return simpleAuthorizationInfo;
	}

	/**
	 * 登录验证
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(
			AuthenticationToken arg0) throws AuthenticationException {
		System.out.println("认证=====");
		String userName = ((MyAuthenticationToken) arg0).getUsername();
		char[] password = ((MyAuthenticationToken) arg0).getPassword();
		User user = new User();// 根据用户名密码获取user
		//Object obj = new SimpleHash("md5", new String(password), null, 1);
		user.setPassword(new String(password));
		user.setUserName(userName);
		user = ((AccountService) SpringContext.getBean("accountService"))
				.findByPw(user);
		if (user == null) {
			throw new AuthenticationException("用户名密码错误");
		}

		SimpleAuthenticationInfo simpleAuthenticationInfo = new SimpleAuthenticationInfo(
				user, user.getPassword(), this.getName());
		return simpleAuthenticationInfo;
	}

	@Override
	protected void doClearCache(PrincipalCollection principals) {
		// TODO Auto-generated method stub
		super.doClearCache(principals);
		clearCachedAuthenticationInfo(principals);
		clearCachedAuthorizationInfo(principals);
	}

	@Override
	protected void clearCachedAuthenticationInfo(PrincipalCollection principals) {
		// TODO Auto-generated method stub
		super.clearCachedAuthenticationInfo(principals);
		Object key = principals.getPrimaryPrincipal();
		// ShiroUser shiroUser=new ShiroUser();
		// try {
		// BeanUtils.copyProperties(shiroUser, key);
		// } catch (Exception e) {
		// // TODO Auto-generated catch block
		// }
		getAuthenticationCache().remove(key);
	}

	@Override
	protected void clearCachedAuthorizationInfo(PrincipalCollection principals) {
		// TODO Auto-generated method stub
		super.clearCachedAuthorizationInfo(principals);
		Object key = getAuthorizationCacheKey(principals);
		getAuthorizationCache().remove(key);
	}

	protected Object getAuthorizationCacheKey(PrincipalCollection principals) {
		return principals.getPrimaryPrincipal();
	}
}
