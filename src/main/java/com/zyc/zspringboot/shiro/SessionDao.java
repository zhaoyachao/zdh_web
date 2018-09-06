package com.zyc.zspringboot.shiro;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.SimpleSession;
import org.apache.shiro.session.mgt.eis.EnterpriseCacheSessionDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;


public class SessionDao extends EnterpriseCacheSessionDAO {
	private String cacheKey="shiro:cache:shiro-activeSessionCache1:";
	private RedisUtil redisUtil;
	public RedisUtil getRedisUtil() {
		return redisUtil;
	}

	public void setRedisUtil(RedisUtil redisUtil) {
		this.redisUtil = redisUtil;
	}

	// 创建session，保存到数据库
    @Override
    protected Serializable doCreate(Session session) {
        @SuppressWarnings("unused")
		Serializable sessionId = super.doCreate(session);
        //redisUtil.set(cacheKey+session.getId().toString(), sessionToByte(session),1*60L);
        getCacheManager().getCache("shiro-activeSessionCache1").put(session.getId().toString(), session);
        return session.getId();
    }

    // 获取session
    @Override
    protected Session doReadSession(Serializable sessionId) {
        // 先从缓存中获取session，如果没有再去数据库中获取
        //Session session = super.doReadSession(sessionId); 
//        if(session == null){
//            byte[] bytes = (byte[]) redisUtil.get(sessionId.toString());
//            if(bytes != null && bytes.length > 0){
//                session = byteToSession(bytes);    
//            }
//        }
    	Session session = getActiveSessionsCache().get(sessionId);
        return session;
    }

    // 更新session的最后一次访问时间
    @Override
    protected void doUpdate(Session session) {
        super.doUpdate(session);
        //redisUtil.set(session.getId().toString(), sessionToByte(session),1*60L);
        getActiveSessionsCache().put(session.getId().toString(), session);
    }

    // 删除session
    @Override
    protected void doDelete(Session session) {
    	System.out.println("doDelete......");
//        super.doDelete(session);
//        redisUtil.remove(session.getId().toString());
    	getActiveSessionsCache().remove(session.getId().toString());
    }

    // 把session对象转化为byte保存到redis中
    public byte[] sessionToByte(Session session){
        ByteArrayOutputStream bo = new ByteArrayOutputStream();
        byte[] bytes = null;
        try {
            ObjectOutput oo = new ObjectOutputStream(bo);
            oo.writeObject(session);
            bytes = bo.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bytes;
    }
    
    // 把byte还原为session
    public Session byteToSession(byte[] bytes){
        ByteArrayInputStream bi = new ByteArrayInputStream(bytes);
        ObjectInputStream in;
        SimpleSession session = null;
        try {
            in = new ObjectInputStream(bi);
            session = (SimpleSession) in.readObject();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    
        return session;
    }
}