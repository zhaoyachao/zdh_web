package com.zyc.zdh.aop;

import com.alibaba.fastjson.JSON;
import com.zyc.zdh.annotation.White;
import com.zyc.zdh.dao.NoticeMapper;
import com.zyc.zdh.dao.UserOperateLogMapper;
import com.zyc.zdh.entity.*;
import com.zyc.zdh.exception.ZdhException;
import com.zyc.zdh.shiro.RedisUtil;
import com.zyc.zdh.util.Const;
import com.zyc.zdh.util.DateUtil;
import com.zyc.zdh.util.SpringContext;
import org.apache.shiro.SecurityUtils;
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
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


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
	@Pointcut("execution(* com.zyc.zdh.controller.*.*(..)) && !execution(* com.zyc.zdh.controller.LoginController.login1(..)) && !execution(* com.zyc.zdh.controller.LoginController.getIndex(..))")
	public void pointcutMethod3(){}

	@Around(value = "pointcutMethod()")
	public Object around(ProceedingJoinPoint pjp){
		try {
			logger.info("start run aop method around ");
			return pjp.proceed();
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			String error = "类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}";
			logger.error(error, e);
		}
		return null;
	}
	
	@Around(value = "pointcutMethod3()")
	public Object aroundLog(ProceedingJoinPoint pjp) throws Exception {
		try {
			long start = System.currentTimeMillis();
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
			String reqParam = "";

			//校验ip黑名单
			boolean is_ipbacklist = is_ipblacklist(ipAddr);
			if(is_ipbacklist){
				UserOperateLogMapper userOperateLogMapper= (UserOperateLogMapper)SpringContext.getBean("userOperateLogMapper");
				UserOperateLogInfo userOperateLogInfo=new UserOperateLogInfo();
				userOperateLogInfo.setOwner(getUser().getId());
				userOperateLogInfo.setUser_name(getUser().getUserName());
				userOperateLogInfo.setOperate_url(url);
				userOperateLogInfo.setOperate_input(reqParam);
				userOperateLogInfo.setTime(String.valueOf((System.currentTimeMillis()-start)/1000.0));
				userOperateLogInfo.setIp(ipAddr);
				String output = String.format("用户:%s命中IP黑名单,IP地址:%s", getUser().getUserName(), ipAddr);
				userOperateLogInfo.setOperate_output(output);

				userOperateLogInfo.setCreate_time(new Timestamp(new Date().getTime()));
				userOperateLogInfo.setUpdate_time(new Timestamp(new Date().getTime()));
				userOperateLogMapper.insert(userOperateLogInfo);
				if (request.getMethod().equalsIgnoreCase("get")){
					return "redirect:403";
				}else{
					return ReturnInfo.createInfo(RETURN_CODE.FAIL.getCode(), "命中ip黑名单,禁止访问", "命中ip黑名单,禁止访问");
				}
			}

			//校验网址是否可访问
			boolean is_pass = is_pass(getUrl(request));
			if(!is_pass){
				logger.warn("系统维护中,只有admin用户和zyc用户可访问....");
				if (request.getMethod().equalsIgnoreCase("get")){
					return "redirect:503";
				}else{
					return ReturnInfo.createInfo(RETURN_CODE.FAIL.getCode(), "系统维护中", "系统维护中");
				}

			}


			//未登录且非登录请求强制跳转到登录页面
			if(!SecurityUtils.getSubject().isAuthenticated()){
				//WebUtils.issueRedirect(request, response, "login");
				//SecurityUtils.getSecurityManager().logout(subject);
				String whiteUrl =getUrl(request);
				if(!white().contains(whiteUrl)){
					return "redirect:login";
				}
			}
			//此处校验用户是否在黑名单中,有则不允许访问
			if(getUser()!=null && is_blacklist(getUser().getUserName())){
				return ReturnInfo.createInfo(RETURN_CODE.FAIL.getCode(), "当前用户为黑名单用户,请联系管理员解封", null);
			}

			try{
				reqParam = preHandle(pjp,request);
			}catch (Exception e){
				try{
					UserOperateLogMapper userOperateLogMapper= (UserOperateLogMapper)SpringContext.getBean("userOperateLogMapper");
					UserOperateLogInfo userOperateLogInfo=new UserOperateLogInfo();
					userOperateLogInfo.setOwner(getUser().getId());
					userOperateLogInfo.setUser_name(getUser().getUserName());
					userOperateLogInfo.setOperate_url(url);
					userOperateLogInfo.setOperate_input(reqParam);
					userOperateLogInfo.setTime(String.valueOf((System.currentTimeMillis()-start)/1000.0));
					userOperateLogInfo.setIp(ipAddr);
					if((e.getMessage()).length()>6400){
						userOperateLogInfo.setOperate_output((e.getMessage()).substring(0,256));
					}else{
						userOperateLogInfo.setOperate_output(e.getMessage());
					}
					userOperateLogInfo.setCreate_time(new Timestamp(new Date().getTime()));
					userOperateLogInfo.setUpdate_time(new Timestamp(new Date().getTime()));
					userOperateLogMapper.insert(userOperateLogInfo);
				}catch (Exception ex){
					String error = "类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}";
					logger.error(error, ex);
				}

				if(e.getMessage().contains("没有权限")){
					return ReturnInfo.createInfo(RETURN_CODE.FAIL.getCode(), "无权限", e);
				}
				if (request.getMethod().equalsIgnoreCase("get")){
					return "404";
				}
				String error = "类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}";
				logger.error(error, e);
				return ReturnInfo.createInfo(RETURN_CODE.FAIL.getCode(), "系统错误", e);
			}
            String uid = getUser() == null? "":getUser().getId();
			logger.info("请求源IP:【{}】,用户:【{}】,请求URL:【{}】,类型:【{}】请求参数:【{}】",ipAddr,uid,url,request.getMethod(),reqParam);
			Object o=pjp.proceed();
			logger.info("请求源IP:【{}】,用户:【{}】,请求URL:【{}】,结束",ipAddr,uid,url);
			try{
				//跳过特殊操作，etlEcharts,getTotalNum,notice_list
				if(is_operate_log(url) && getUser() != null){
					if(o instanceof String){
						UserOperateLogMapper userOperateLogMapper= (UserOperateLogMapper)SpringContext.getBean("userOperateLogMapper");
						UserOperateLogInfo userOperateLogInfo=new UserOperateLogInfo();
						userOperateLogInfo.setOwner(getUser().getId());
						userOperateLogInfo.setUser_name(getUser().getUserName());
						userOperateLogInfo.setOperate_url(url);
						userOperateLogInfo.setOperate_input(reqParam);
						userOperateLogInfo.setIp(ipAddr);
						userOperateLogInfo.setTime(String.valueOf((System.currentTimeMillis()-start)/1000.0));
						if(((String) o).length()>6400){
							userOperateLogInfo.setOperate_output(((String) o).substring(0,256));
						}else{
							userOperateLogInfo.setOperate_output((String) o);
						}
						userOperateLogInfo.setCreate_time(new Timestamp(new Date().getTime()));
						userOperateLogInfo.setUpdate_time(new Timestamp(new Date().getTime()));
						userOperateLogMapper.insert(userOperateLogInfo);
					}
				}
			}catch (Exception e){
				String error = "类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}";
                logger.error(error, e);
			}

			return o;
		} catch (Throwable e) {
			String error = "类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}";
            logger.error(error, e);
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
			String error = "类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}";
			logger.error(error, e);
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
		boolean is_white=false;
		for (Annotation annotation : annotations) {
			if (annotation.annotationType().equals(White.class)){
				if(targetMethod.getAnnotation(White.class).value())
					is_white=true;
			}
		}
		for (Annotation annotation : annotations) {
			//此处可以改成自定义的注解
			if (annotation.annotationType().equals(RequestMapping.class)) {
				reqParam = JSON.toJSONString(request.getParameterMap());
				//验证权限
				String url =getUrl(request);
				String method = request.getMethod();
				if(white().contains(url) || is_white){
					break;
				}
				if(!SecurityUtils.getSubject().isPermitted(url)){
					//此处增加zdh通知
					send_notice(getUser(),"接口权限通知", "用户名:"+getUser().getUserName()+", 时间:"+ DateUtil.getCurrentTime()+", "+url+"没有权限");
					throw new ZdhException(url+"没有权限");
				}

				break;
			}
		}
		return reqParam;
	}

	private String getUrl(HttpServletRequest request){
		String url = request.getServletPath();
		if(url.startsWith("/"))
			url = url.substring(1).replaceAll("function:","");
		url = url.split("\\.")[0];
		return url;
	}

	private void send_notice(User user, String title, String msg){
		try{
			NoticeMapper noticeMapper = (NoticeMapper) SpringContext.getBean("noticeMapper");
			NoticeInfo ni=new NoticeInfo();
			ni.setMsg_type("告警");
			ni.setMsg_title(title);
			ni.setMsg_url("");
			ni.setMsg(msg);
			ni.setIs_see(Const.FALSE);
			ni.setOwner(user.getId());
			ni.setCreate_time(new Timestamp(new Date().getTime()));
			ni.setUpdate_time(new Timestamp(new Date().getTime()));
			noticeMapper.insert(ni);
		}catch (Exception e){
			String error = "类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}";
			logger.error(error, e);
			logger.error("接口无权限告警异常, {}",e);
		}
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
		permissions.add("403");
		permissions.add("503");
		permissions.add("500");
		permissions.add("logout");
		permissions.add("retrieve_password");
		permissions.add("register");
		permissions.add("zdh_version");
		permissions.add("zdh_download_index");
		permissions.add("favicon");
		permissions.add("index");
		permissions.add("every_day_notice");
		permissions.add("notice_list");
		permissions.add("readme");
		permissions.add("zdh_help");
		permissions.add("check_captcha");
		permissions.add("get_platform_name");

		return permissions;
	}

	/**
	 * 查询是否命中用户名单,命中则不许访问
	 * @param userName
	 * @return
	 */
	private boolean is_blacklist(String userName){
		//查询黑名单

		return false;
	}

	/**
	 * 校验网址是否可访问,不区分用户,如果想个别用户区分,则使用黑名单限制功能
	 * @return
	 */
	private boolean is_pass(String url){
		if(url.contains("503")){
			return true;
		}
		RedisUtil redisUtil = (RedisUtil) SpringContext.getBean("redisUtil");
		if(getUser()==null){
			return true;
		}
		if(getUser().getUserName().equalsIgnoreCase("admin") || getUser().getUserName().equalsIgnoreCase("zyc") ){
			return true;
		}
		Object o = redisUtil.get(Const.ZDH_IS_PASS);
		if(o == null){
			return true;
		}
		if(o!=null && o.toString().equalsIgnoreCase("true")){
			return true;
		}
		return false;
	}

	/**
	 * ip 是否在黑名单中
	 * @param ip
	 * @return true:命中黑名单,false:为命中黑名单
	 */
	private boolean is_ipblacklist(String ip){

		if(getUser()==null){
			return false;
		}

		RedisUtil redisUtil = (RedisUtil) SpringContext.getBean("redisUtil");

		Object o = redisUtil.get(Const.ZDH_IP_BACKLIST);
		if(o == null){
			return false;
		}
		if(o!=null && !o.toString().contains(ip)){
			return false;
		}
		return true;
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
					String error = "类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}";
			        logger.error(error, e);
				}
				// 恢复访问控制权限
				fields[i].setAccessible(accessFlag);
			} catch (IllegalArgumentException e) {
				String error = "类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}";
				logger.error(error, e);
			}
		}
	}
	
}
