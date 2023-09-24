package com.zyc.zdh.shiro;

import com.zyc.zdh.dao.PermissionMapper;
import com.zyc.zdh.dao.ResourceTreeMapper;
import com.zyc.zdh.entity.PermissionUserInfo;
import com.zyc.zdh.entity.User;
import com.zyc.zdh.entity.UserResourceInfo2;
import com.zyc.zdh.service.AccountService;
import com.zyc.zdh.util.Const;
import com.zyc.zdh.util.Encrypt;
import com.zyc.zdh.util.SpringContext;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.core.env.Environment;

import java.util.ArrayList;
import java.util.List;

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
		// 获取登录用户的信息,在认证时存储的是ShiroUser 所以得到的就是ShiroUser
		// 在其他地方也可通过SecurityUtils.getSubject().getPrincipals()获取用户信息
		User user = (User)principals.getPrimaryPrincipal();
		// 权限字符串
		List<String> permissions = new ArrayList<>();
		// 从数据库中获取对应权限字符串并存储permissions
		//System.out.println(user.getUserName());
		List<UserResourceInfo2> uris=new ArrayList<>();
		uris=( (ResourceTreeMapper)SpringContext.getBean("resourceTreeMapper")).selectResourceByUserAccount(user.getUserName(), user.getProduct_code());
        for(UserResourceInfo2 uri2:uris){
        	if(!StringUtils.isEmpty(uri2.getUrl())){
				String url = uri2.getUrl();
				if(url.startsWith("/"))
					url = url.substring(1).replaceAll("function:","");
				url = url.split("\\.")[0];
				permissions.add(url);
			}
		}
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
		//验证码验证
		String captcha = ((MyAuthenticationToken) arg0).getCaptcha();
		String session_captcha = ((MyAuthenticationToken) arg0).getSession_captcha();

		if(!captcha.equalsIgnoreCase(session_captcha)){
			throw new AuthenticationException("验证码错误");
		}

		String userName = ((MyAuthenticationToken) arg0).getUsername();
		char[] password = ((MyAuthenticationToken) arg0).getPassword();
		User user = new User();// 根据用户名密码获取user
		//Object obj = new SimpleHash("md5", new String(password), null, 2);
		try {
			user.setPassword(Encrypt.AESencrypt(new String(password)));
		} catch (Exception e) {
			throw new AuthenticationException("密码解析异常");
		}
		user.setUserName(userName);
		user = ((AccountService) SpringContext.getBean("accountService"))
				.findByPw(user);

		if (user == null) {
			throw new AuthenticationException("用户名密码错误");
		}

		//同步权限信息
		PermissionMapper permissionMapper = ((PermissionMapper) SpringContext.getBean("permissionMapper"));
		Environment environment= (Environment) SpringContext.getBean("environment");
		String product_code = environment.getProperty("zdh.product", "zdh");
		PermissionUserInfo permissionUserInfo=new PermissionUserInfo();
		permissionUserInfo.setUser_account(userName);
		permissionUserInfo.setProduct_code(product_code);
		List<PermissionUserInfo> permissionUserInfos = permissionMapper.select(permissionUserInfo);

		if(permissionUserInfos == null || permissionUserInfos.size()<1){
			throw new AuthenticationException("当前用户为配置权限,请联系管理员配置权限");
		}

		user.setEnable(permissionUserInfos.get(0).getEnable());
		user.setRoles(permissionUserInfos.get(0).getRoles());
		user.setUser_group(permissionUserInfos.get(0).getUser_group());
		user.setTag_group_code(permissionUserInfos.get(0).getTag_group_code());
		user.setSignature(permissionUserInfos.get(0).getSignature());
		user.setProduct_code(product_code);

		if(user.getEnable()==null || user.getEnable().equalsIgnoreCase(Const.FALSE)){
			throw new AuthenticationException("当前用户未启用,请联系管理员");
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
		//getAuthorizationCache().remove(key);
	}

	protected Object getAuthorizationCacheKey(PrincipalCollection principals) {
		return principals.getPrimaryPrincipal();
	}
}
