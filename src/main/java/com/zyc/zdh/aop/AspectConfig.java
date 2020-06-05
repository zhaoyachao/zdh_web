package com.zyc.zdh.aop;

import java.lang.reflect.Method;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;


/***
 * ClassName: AspectConfig   
 * @author zyc-admin
 * @date 2017年12月21日  
 * @Description: TODO  
 */
@Aspect
@Component
public class AspectConfig implements Ordered{

	private static Logger logger=LoggerFactory.getLogger(AspectConfig.class);
	@Pointcut("execution(* com.zyc.zdh.quartz..*(..))")
	public void pointcutMethod(){}
	//在方法上的自定义注解使用@annotation,类上自定义注解使用@within(com.zyc.springboot.类名)
	@Pointcut("@annotation(com.zyc.zdh.aop.Log)")
	public void pointcutMethod2(){}
	@Pointcut("execution(* com.zyc.zdh.service.impl.RoleServiceImpl.*(..))")
	public void pointcutMethod3(){}

	@Around(value = "pointcutMethod()")
	public Object around(ProceedingJoinPoint pjp){
		try {
			logger.info("start run aop method around ");
			return pjp.proceed();
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	@Around(value = "pointcutMethod2()")
	public Object aroundLog(ProceedingJoinPoint pjp){
		try {
			logger.info("aroundLog1....start...");
			Object o=pjp.proceed();
			logger.info("aroundLog1...end....");
			return o;
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return null;
	}
	//@Around(value = "pointcutMethod3()")
	public Object aroundLog2(ProceedingJoinPoint pjp){
		try {
			logger.info("aroundLog2...start....");
			Object o=pjp.proceed();
			logger.info("aroundLog2...end....");
			return o;
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return null;
	}
	//@Before("pointcutMethod3()")
	public void before(JoinPoint joinPoint){
		MethodSignature signature = (MethodSignature) joinPoint.getSignature();
		Method method = signature.getMethod();
		if(method.isAnnotationPresent(Log.class)){
			Log log=method.getAnnotation(Log.class);
			logger.info(log.value());
		}
	}
	/**
	 * 多个aspect 如果pointcut重复,优先级顺序按照order 的大小来排序，值越小，优先级越高
	 * 如果在一个aspect界面,可根据顺序加载
	 */
	@Override
	public int getOrder() {
	
		return 20;
	}
	
}
