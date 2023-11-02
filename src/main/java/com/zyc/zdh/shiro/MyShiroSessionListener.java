package com.zyc.zdh.shiro;

import com.zyc.zdh.util.SpringContext;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.SessionListener;

public class MyShiroSessionListener implements SessionListener {

	@Override
	public void onStart(Session session) {

	}

	@Override
	public void onStop(Session session) {
		// TODO 退出登录时，先调用此方法，然后会继续调用SessionDao中的doDelete方法
		System.out.println("onStop===" + session.getId());
		RedisUtil redisUtil = (RedisUtil) SpringContext.getBean("redisUtil");
		// 清除session
		redisUtil.remove(session.getId().toString());
		// 清除缓存
		redisUtil.remove("shiro:cache:shiro-activeSessionCache1:"
				+ session.getId().toString());
	}

	@Override
	public void onExpiration(Session session) {
		System.out.println("onExpiration===" + session.getId());
		RedisUtil redisUtil = (RedisUtil) SpringContext.getBean("redisUtil");
		// 清除session
		redisUtil.remove(session.getId().toString());
		// 清除缓存
		redisUtil.remove("shiro:cache:shiro-activeSessionCache1:"
				+ session.getId().toString());
	}

}
