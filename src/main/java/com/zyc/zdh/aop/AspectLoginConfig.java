package com.zyc.zdh.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Service;
import com.zyc.zdh.entity.ResultInfo;
import com.zyc.zdh.entity.User;
import com.zyc.zdh.shiro.RedisOtherDb;

/**
 * ClassName: AspectLoginConfig   
 * @author zyc-admin
 * @date 2018年2月5日  
 * @Description: TODO  
 */
@Aspect
@Service
public class AspectLoginConfig implements Ordered {

	@Autowired
	private RedisOtherDb redisOtherDb;
	
	@Pointcut("execution(* com.zyc.zdh.cloud.api.LoginService.*(..))")
	public void pointLogin(){}
	
	public Object around(ProceedingJoinPoint join) throws Throwable{
		MethodSignature signature = (MethodSignature)join.getSignature();
		String methodName=signature.getMethod().getName();
		Object[] args=join.getArgs();
		
		if(checkMethod(methodName)){
			return join.proceed();
		}else if(isLogined(args)){
			return join.proceed();
		}
		return returnExceptionValue(signature.getReturnType(),"未登录");
	}
	
	@Override
	public int getOrder() {
		// TODO Auto-generated method stub
		return 10;
	}

	private boolean checkMethod(String methodName){
		switch (methodName) {
		case "login":
			return true;
		default:
			break;
		}
		return false;
	}
	
	private boolean isLogined(Object[] args){
		if(args.length==1&&args[0]!=null){
			String userName=((User)args[0]).getUserName();
			if(redisOtherDb.exists(userName)){
				return true;
			}
		}
		return false;
	}
	
	protected Object returnExceptionValue(Class<?> clazz, String message) throws Exception{
		Object object = clazz.newInstance();
		if(object instanceof ResultInfo){
			((ResultInfo) object).setStatus("101");
			((ResultInfo) object).setMessage(message);
		}
		return (ResultInfo)object;
	}
}
