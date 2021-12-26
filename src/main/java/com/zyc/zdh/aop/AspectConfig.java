package com.zyc.zdh.aop;

import java.io.PrintWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.zyc.zdh.dao.UserOperateLogMapper;
import com.zyc.zdh.entity.RETURN_CODE;
import com.zyc.zdh.entity.ReturnInfo;
import com.zyc.zdh.entity.User;
import com.zyc.zdh.entity.UserOperateLogInfo;
import com.zyc.zdh.exception.ZdhException;
import com.zyc.zdh.util.SpringContext;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.web.util.WebUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


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
	@Pointcut("execution(* com.zyc.zdh.controller.*.*(..))")
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
	
	@Around(value = "pointcutMethod3()")
	public Object aroundLog(ProceedingJoinPoint pjp) throws Exception {
		try {
			String classType = pjp.getTarget().getClass().getName();
			Signature sig = pjp.getSignature();
			MethodSignature msig = (MethodSignature) sig;
			Object target = pjp.getTarget();
			LocalVariableTableParameterNameDiscoverer u = new LocalVariableTableParameterNameDiscoverer();
			Method currentMethod = target.getClass().getMethod(msig.getName(), msig.getParameterTypes());
			if(currentMethod.getName().contains("initBinder")){
				return pjp.proceed();
			}

			HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
			HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();

			//IP地址
			String ipAddr = getRemoteHost(request);
			String url = request.getRequestURL().toString();
			String reqParam = preHandle(pjp,request);
            String uid = getUser() == null? "":getUser().getId();
			logger.info("请求源IP:【{}】,用户:【{}】,请求URL:【{}】,请求参数:【{}】",ipAddr,uid,url,reqParam);
			Object o=pjp.proceed();
			logger.info("请求源IP:【{}】,用户:【{}】,请求URL:【{}】,结束",ipAddr,uid,url);
			//跳过特殊操作，etlEcharts,getTotalNum,notice_list
			if(is_operate_log(url) && getUser() != null){
				if(o instanceof String){
					UserOperateLogMapper userOperateLogMapper= (UserOperateLogMapper)SpringContext.getBean("userOperateLogMapper");
					UserOperateLogInfo userOperateLogInfo=new UserOperateLogInfo();
					userOperateLogInfo.setOwner(getUser().getId());
					userOperateLogInfo.setUser_name(getUser().getUserName());
					userOperateLogInfo.setOperate_url(url);
					userOperateLogInfo.setOperate_input(reqParam);

					if(((String) o).length()>6400){
						userOperateLogInfo.setOperate_output(((String) o).substring(0,6400));
					}else{
						userOperateLogInfo.setOperate_output((String) o);
					}
					userOperateLogInfo.setCreate_time(new Timestamp(new Date().getTime()));
					userOperateLogInfo.setUpdate_time(new Timestamp(new Date().getTime()));
					userOperateLogMapper.insert(userOperateLogInfo);
				}
			}
			return o;
		} catch (Throwable e) {
			e.printStackTrace();
			return ReturnInfo.createInfo(RETURN_CODE.FAIL.getCode(), "系统错误", e);
		}
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

	private String preHandle(ProceedingJoinPoint joinPoint,HttpServletRequest request) throws Exception {

		String reqParam = "";
		Signature signature = joinPoint.getSignature();
		MethodSignature methodSignature = (MethodSignature) signature;
		Method targetMethod = methodSignature.getMethod();
		Annotation[] annotations = targetMethod.getAnnotations();
		for (Annotation annotation : annotations) {
			//此处可以改成自定义的注解
			if (annotation.annotationType().equals(RequestMapping.class)) {
				reqParam = JSON.toJSONString(request.getParameterMap());
				//验证权限
				String url = request.getServletPath();
				if(url.startsWith("/"))
					url = url.substring(1).replaceAll("function:","");
				url = url.split("\\.")[0];

				if(white().contains(url)){
					break;
				}
				if(!SecurityUtils.getSubject().isPermitted(url)){
					throw new ZdhException(url+"没有权限");
				}

				break;
			}
		}
		return reqParam;
	}

	private boolean is_operate_log(String url){

		String[] back_urls = new String[]{"etlEcharts", "getTotalNum", "notice_list", "user_operate_log_list", "user_operate_log_index"};
		for (String back_url: back_urls){
			if(url.contains(back_url))
				return false;
		}

		return true;
	}

	private List<String> white(){
		List<String> permissions= new ArrayList<>();
		permissions.add("login");
		permissions.add("captcha");
		permissions.add("404");
		permissions.add("logout");
		permissions.add("retrieve_password");
		permissions.add("register");

		return permissions;
	}
	/**
	 * 获取ip
	 * @param request
	 * @return
	 */
	private String getRemoteHost(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return "0:0:0:0:0:0:0:1".equals(ip) ? "127.0.0.1" : ip;
	}

	public User getUser() {
		User user = (User) SecurityUtils.getSubject().getPrincipal();
		return user;
	}

	private void debugInfo(Object obj) {
		Field[] fields = obj.getClass().getDeclaredFields();
		for (int i = 0, len = fields.length; i < len; i++) {
			// 对于每个属性，获取属性名
			String varName = fields[i].getName();
			try {
				// 获取原来的访问控制权限
				boolean accessFlag = fields[i].isAccessible();
				// 修改访问控制权限
				fields[i].setAccessible(true);
				// 获取在对象f中属性fields[i]对应的对象中的变量
				Object o;
				try {
					o = fields[i].get(obj);
					System.err.println("传入的对象中包含一个如下的变量：" + varName + " = " + o);
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				// 恢复访问控制权限
				fields[i].setAccessible(accessFlag);
			} catch (IllegalArgumentException ex) {
				ex.printStackTrace();
			}
		}
	}
	
}
