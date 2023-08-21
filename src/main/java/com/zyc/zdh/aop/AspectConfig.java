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
import com.zyc.zdh.util.IpUtil;
import com.zyc.zdh.util.SpringContext;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RRateLimiter;
import org.redisson.api.RateIntervalUnit;
import org.redisson.api.RateType;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.TimeUnit;


/***
 * ClassName: AspectConfig   
 * @author zyc-admin
 * @date 2017年12月21日
 * @Description: TODO
 */
@Aspect
@Component
public class AspectConfig implements Ordered {

    private static Logger logger = LoggerFactory.getLogger(AspectConfig.class);

    @Pointcut("execution(* com.zyc.zdh.quartz..*(..))")
    public void pointcutMethod() {
    }

    //在方法上的自定义注解使用@annotation,类上自定义注解使用@within(com.zyc.springboot.类名)
    @Pointcut("@annotation(com.zyc.zdh.aop.Log)")
    public void pointcutMethod2() {
    }

    @Pointcut("execution(* com.zyc.zdh.controller.*.*.*(..)) || (execution(* com.zyc.zdh.controller.*.*(..)) && !execution(* com.zyc.zdh.controller.LoginController.login1(..)) && !execution(* com.zyc.zdh.controller.LoginController.getIndex(..)))")
    public void pointcutMethod3() {
    }


    @Around(value = "pointcutMethod3()")
    public Object aroundLog(ProceedingJoinPoint pjp) throws Exception {
        MDC.put("logId", UUID.randomUUID().toString());
        //获取返回类型
        Signature signature = pjp.getSignature();
        String returnName = "";
        if (signature instanceof MethodSignature) {
            MethodSignature methodSignature = (MethodSignature) signature;
            // 被切的方法
            Method method = methodSignature.getMethod();
            // 返回类型
            Class<?> methodReturnType = method.getReturnType();
            returnName = methodReturnType.getName();
        }

        try {
            RedisUtil redisUtil = (RedisUtil) SpringContext.getBean("redisUtil");

            RedissonClient redissonClient = (RedissonClient) SpringContext.getBean("redissonClient");

            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
            String method = request.getMethod();

            if(method.equalsIgnoreCase("get")){
                response.setDateHeader("Expires", System.currentTimeMillis());
            }
            //IP地址
            String ipAddr = getRemoteHost(request);
            String url = request.getRequestURL().toString();
            Object clearTime = redisUtil.get(Const.ZDH_RATELIMIT_CLEART_TIME);
            if (getUser() != null) {
                Object user_limit = redisUtil.get(Const.ZDH_USER_RATELIMIT, "200");
                String key = Const.ZDH_RATELIMIT_KEY_USER_PRE + getUser().getUserName();
                RRateLimiter rateLimiter = redissonClient.getRateLimiter(key);
                //设置速率，time秒中产生count个令牌
                rateLimiter.trySetRate(RateType.OVERALL, Long.valueOf(user_limit.toString()), 1L, RateIntervalUnit.SECONDS);
                // 试图获取一个令牌，获取到返回true
                boolean tryAcquire = rateLimiter.tryAcquire(1L, TimeUnit.SECONDS);
                if(!tryAcquire){
                    logger.warn("当前IP/账号超出流量限制,IP_LIMIT:{},USER_LIMIT:{},IP:{}, 账号:{}", -1L,user_limit, ipAddr, getUser() == null ? "" : getUser().getUserName());
                    if (method.equalsIgnoreCase("get")) {
                        return "403";
                    }
                    return ReturnInfo.createInfo(RETURN_CODE.FAIL.getCode(), "当前IP/账号超出流量限制", "当前IP/账号超出流量限制");
                }
            }


            if (getUser() == null) {
                MDC.put("user_id", UUID.randomUUID().toString());
            } else {
                MDC.put("user_id", getUser().getUserName());
            }

            long start = System.currentTimeMillis();
            String classType = pjp.getTarget().getClass().getName();
            Signature sig = pjp.getSignature();
            MethodSignature msig = (MethodSignature) sig;
            Object target = pjp.getTarget();
            LocalVariableTableParameterNameDiscoverer u = new LocalVariableTableParameterNameDiscoverer();
            Method currentMethod = target.getClass().getMethod(msig.getName(), msig.getParameterTypes());
            if (currentMethod.getName().contains("initBinder")) {
                MDC.remove("user_id");
                return pjp.proceed();
            }


            String reqParam = "";

            //校验账号是否启用
            boolean is_unenable = is_unenable();
            //校验ip黑名单
            boolean is_ipbacklist = is_ipblacklist(ipAddr);
            boolean is_userbacklist = is_blacklist();
            if (is_ipbacklist || is_userbacklist || is_unenable) {
                UserOperateLogMapper userOperateLogMapper = (UserOperateLogMapper) SpringContext.getBean("userOperateLogMapper");
                UserOperateLogInfo userOperateLogInfo = new UserOperateLogInfo();
                userOperateLogInfo.setOwner(getUser().getUserName());
                userOperateLogInfo.setUser_name(getUser().getUserName());
                userOperateLogInfo.setOperate_url(url);
                userOperateLogInfo.setOperate_input(reqParam);
                userOperateLogInfo.setTime(String.valueOf((System.currentTimeMillis() - start) / 1000.0));
                userOperateLogInfo.setIp(ipAddr);
                String output = "";
                if (is_ipbacklist) {
                    output = String.format("用户:%s命中IP黑名单,IP地址:%s", getUser().getUserName(), ipAddr);
                } else if (is_userbacklist) {
                    output = String.format("用户:%s命中用户黑名单,IP地址:%s", getUser().getUserName(), ipAddr);
                } else if(is_unenable){
                    output = String.format("用户:%s已被禁用,IP地址:%s", getUser().getUserName(), ipAddr);
                }
                userOperateLogInfo.setOperate_output(output);

                userOperateLogInfo.setCreate_time(new Timestamp(new Date().getTime()));
                userOperateLogInfo.setUpdate_time(new Timestamp(new Date().getTime()));
                userOperateLogMapper.insert(userOperateLogInfo);
                MDC.remove("user_id");
                if (request.getMethod().equalsIgnoreCase("get")) {
                    return "redirect:403";
                } else {
                    if (returnName.contains("ReturnInfo")) {
                        return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "命中IP/用户黑名单,禁止访问", "命中IP/用户黑名单,禁止访问");
                    }
                    return ReturnInfo.createInfo(RETURN_CODE.FAIL.getCode(), "命中IP/用户黑名单,禁止访问", "命中IP/用户黑名单,禁止访问");
                }
            }

            //校验网址是否可访问
            boolean is_pass = is_pass(getUrl(request));
            if (!is_pass) {
                logger.warn("系统维护中,只有admin用户和zyc用户可访问....");
                MDC.remove("user_id");
                String uid = getUser() == null ? "" : getUser().getUserName();
                logger.debug("请求源IP:【{}】,用户:【{}】,请求URL:【{}】,类型:【{}】请求参数:【{}】", ipAddr, uid, url, request.getMethod(), reqParam);
                if (request.getMethod().equalsIgnoreCase("get")) {
                    return "redirect:503";
                } else {
                    if (returnName.contains("ReturnInfo")) {
                        return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "系统维护中", "系统维护中");
                    }
                    return ReturnInfo.createInfo(RETURN_CODE.FAIL.getCode(), "系统维护中", "系统维护中");
                }
            }


            //未登录且非登录请求强制跳转到登录页面
            if (!SecurityUtils.getSubject().isAuthenticated()) {
                //WebUtils.issueRedirect(request, response, "login");
                //SecurityUtils.getSecurityManager().logout(subject);
                String whiteUrl = getUrl(request);
                if (!white().contains(whiteUrl)) {
                    MDC.remove("user_id");
                    logger.error("用户未登录");
                    return "redirect:login";
                }
            }
//			//此处校验用户是否在黑名单中,有则不允许访问
//			if(getUser()!=null && is_blacklist(getUser().getUserName())){
//				return ReturnInfo.createInfo(RETURN_CODE.FAIL.getCode(), "当前用户为黑名单用户,请联系管理员解封", null);
//			}

            try {
                reqParam = preHandle(pjp, request);
            } catch (Exception e) {
                try {
                    UserOperateLogMapper userOperateLogMapper = (UserOperateLogMapper) SpringContext.getBean("userOperateLogMapper");
                    UserOperateLogInfo userOperateLogInfo = new UserOperateLogInfo();
                    userOperateLogInfo.setOwner(getUser().getUserName());
                    userOperateLogInfo.setUser_name(getUser().getUserName());
                    userOperateLogInfo.setOperate_url(url);
                    userOperateLogInfo.setOperate_input(reqParam);
                    userOperateLogInfo.setTime(String.valueOf((System.currentTimeMillis() - start) / 1000.0));
                    userOperateLogInfo.setIp(ipAddr);
                    if ((e.getMessage()).length() > 6400) {
                        userOperateLogInfo.setOperate_output((e.getMessage()).substring(0, 256));
                    } else {
                        userOperateLogInfo.setOperate_output(e.getMessage());
                    }
                    userOperateLogInfo.setCreate_time(new Timestamp(new Date().getTime()));
                    userOperateLogInfo.setUpdate_time(new Timestamp(new Date().getTime()));
                    userOperateLogMapper.insert(userOperateLogInfo);
                } catch (Exception ex) {
                    String error = "类:" + Thread.currentThread().getStackTrace()[1].getClassName() + " 函数:" + Thread.currentThread().getStackTrace()[1].getMethodName() + " 异常: {}";
                    logger.error(error, ex);
                }

                if (e.getMessage().contains("没有权限")) {
                    MDC.remove("user_id");
                    String uid = getUser() == null ? "" : getUser().getUserName();
                    logger.error("请求源IP:【{}】,用户:【{}】,请求URL:【{}】,类型:【{}】,请求参数:【{}】, 当前请求无权限", ipAddr, uid, url, request.getMethod(), reqParam);
                    if (request.getMethod().equalsIgnoreCase("get")) {
                        //get请求无权限,返回403
                        return "403";
                    }
                    if (returnName.contains("ReturnInfo")) {
                        return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "无权限", e);
                    }

                    return ReturnInfo.createInfo(RETURN_CODE.FAIL.getCode(), "无权限", e);

                }
                if (request.getMethod().equalsIgnoreCase("get")) {
                    MDC.remove("user_id");
                    String uid = getUser() == null ? "" : getUser().getUserName();
                    logger.error("请求源IP:【{}】,用户:【{}】,请求URL:【{}】,类型:【{}】,请求参数:【{}】,当前请求异常,强制跳转404", ipAddr, uid, url, request.getMethod(), reqParam);
                    return "404";
                }
                String error = "类:" + Thread.currentThread().getStackTrace()[1].getClassName() + " 函数:" + Thread.currentThread().getStackTrace()[1].getMethodName() + " 异常: {}";
                logger.error(error, e);
                MDC.remove("user_id");
                if (returnName.contains("ReturnInfo")) {
                    return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "系统错误", e);
                }
                return ReturnInfo.createInfo(RETURN_CODE.FAIL.getCode(), "系统错误", e);
            }
            String uid = getUser() == null ? "" : getUser().getUserName();
            String city = IpUtil.getCityByIp(ipAddr);
            if (is_operate_log(url)){
                logger.debug("请求源IP:【{}】【{}】,用户:【{}】,请求URL:【{}】,类型:【{}】,请求参数:【{}】", ipAddr, city, uid, url, request.getMethod(), reqParam);
            }
            Object o = pjp.proceed();
            if (is_operate_log(url)){
                logger.debug("请求源IP:【{}】【{}】,用户:【{}】,请求URL:【{}】,结束", ipAddr, city, uid, url);
            }
            try {
                //跳过特殊操作，etlEcharts,getTotalNum,notice_list
                if (is_operate_log(url) && getUser() != null) {
                    UserOperateLogMapper userOperateLogMapper = (UserOperateLogMapper) SpringContext.getBean("userOperateLogMapper");
                    UserOperateLogInfo userOperateLogInfo = new UserOperateLogInfo();
                    userOperateLogInfo.setOwner(getUser().getUserName());
                    userOperateLogInfo.setUser_name(getUser().getUserName());
                    userOperateLogInfo.setOperate_url(url);
                    userOperateLogInfo.setOperate_input(reqParam);
                    userOperateLogInfo.setIp(ipAddr);
                    userOperateLogInfo.setTime(String.valueOf((System.currentTimeMillis() - start) / 1000.0));
                    userOperateLogInfo.setCreate_time(new Timestamp(new Date().getTime()));
                    userOperateLogInfo.setUpdate_time(new Timestamp(new Date().getTime()));

                    if (o instanceof String) {
                        if (((String) o).length() > 6400) {
                            userOperateLogInfo.setOperate_output(((String) o).substring(0, 256));
                        } else {
                            userOperateLogInfo.setOperate_output((String) o);
                        }
                    } else if (o instanceof List) {
                        userOperateLogInfo.setOperate_output(JSON.toJSONString(((List) o)));
                        if (((List) o).size() > 10) {
                            userOperateLogInfo.setOperate_output(JSON.toJSONString(((List) o).subList(0, 10)));
                        }
                    } else if (o instanceof ReturnInfo) {
                        ReturnInfo returnInfo = new ReturnInfo();
                        returnInfo.setCode(((ReturnInfo) o).getCode());
                        returnInfo.setMsg(((ReturnInfo) o).getMsg());
                        Object obj = ((ReturnInfo) o).getResult();
                        if (obj instanceof List) {
                            List list = ((List) ((ReturnInfo) o).getResult());
                            returnInfo.setResult(list);
                            if (list.size() > 10) {
                                returnInfo.setResult(list.subList(0, 10));
                            }
                        } else if (obj instanceof PageResult) {
                            returnInfo.setResult(obj);
                        } else {
                            returnInfo.setResult(obj);
                        }
                        userOperateLogInfo.setOperate_output(JSON.toJSONString(returnInfo));
                    } else {
                        //未知的object类型
                        userOperateLogInfo.setOperate_output(JSON.toJSONString("未知的返回值类型,请管理员确认"));
                    }
                    userOperateLogMapper.insert(userOperateLogInfo);
                }
            } catch (Exception e) {
                String error = "类:" + Thread.currentThread().getStackTrace()[1].getClassName() + " 函数:" + Thread.currentThread().getStackTrace()[1].getMethodName() + " 异常: {}";
                logger.error(error, e);
            }
            MDC.remove("user_id");
            return o;
        } catch (Throwable e) {
            String error = "类:" + Thread.currentThread().getStackTrace()[1].getClassName() + " 函数:" + Thread.currentThread().getStackTrace()[1].getMethodName() + " 异常: {}";
            logger.error(error, e);
            MDC.remove("user_id");
            MDC.remove("logId");
            if (returnName.contains("ReturnInfo")) {
                return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "系统错误", e);
            }
            return ReturnInfo.createInfo(RETURN_CODE.FAIL.getCode(), "系统错误", e);
        }
    }

    //@Before("pointcutMethod3()")
    public void before(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        if (method.isAnnotationPresent(Log.class)) {
            Log log = method.getAnnotation(Log.class);
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

    private String preHandle(ProceedingJoinPoint joinPoint, HttpServletRequest request) throws Exception {

        String reqParam = "";
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method targetMethod = methodSignature.getMethod();
        Annotation[] annotations = targetMethod.getAnnotations();
        boolean is_white = false;
        for (Annotation annotation : annotations) {
            if (annotation.annotationType().equals(White.class)) {
                if (targetMethod.getAnnotation(White.class).value())
                    is_white = true;
            }
        }
        for (Annotation annotation : annotations) {
            //此处可以改成自定义的注解
            if (annotation.annotationType().equals(RequestMapping.class)) {
                RequestMethod[] requestMethods = ((RequestMapping) annotation).method();
                if (requestMethods != null && requestMethods.length > 0) {
                    //校验请求类型和注解是否一致
                    if (requestMethods.length > 1) {
                        logger.error(getUrl(request) + "请求类型: " + request.getMethod() + ", 注解类型: " + JSON.toJSONString(requestMethods));
                    } else {
                        if (!request.getMethod().equalsIgnoreCase(requestMethods[0].toString())) {
                            logger.error(getUrl(request) + "请求类型: " + request.getMethod() + ", 注解类型: " + JSON.toJSONString(requestMethods));
                        }
                    }
                } else {
                    //logger.error(getUrl(request)+"请求类型: "+request.getMethod()+", 注解类型: "+JSON.toJSONString(requestMethods));
                }
                reqParam = JSON.toJSONString(request.getParameterMap());
                //验证权限
                String url = getUrl(request);
                String method = request.getMethod();
                if (white().contains(url) || is_white || url.contains("smart_doc")) {
                    break;
                }
                String url_tmp = url;
                if (url.startsWith("redis/get")) {
                    url_tmp = "redis/get";
                }
                if (url.startsWith("redis/del")) {
                    url_tmp = "redis/del";
                }
                if (!SecurityUtils.getSubject().isPermitted(url_tmp)) {
                    //此处增加zdh通知
                    send_notice(getUser(), "接口权限通知", "用户名:" + getUser().getUserName() + ", 时间:" + DateUtil.getCurrentTime() + ", " + url + "没有权限");
                    throw new ZdhException(url + "没有权限");
                }

                break;
            }
        }
        if (getUrl(request).contains("list") && request.getMethod().equalsIgnoreCase("get")) {
            logger.error(getUrl(request) + "============检测属于get请求,请检查是否合法");
        }
        return reqParam;
    }

    private String getUrl(HttpServletRequest request) {
        String url = request.getServletPath();
        if (url.startsWith("/"))
            url = url.substring(1).replaceAll("function:", "");
        url = url.split("\\.")[0];
        return url;
    }

    private void send_notice(User user, String title, String msg) {
        try {
            NoticeMapper noticeMapper = (NoticeMapper) SpringContext.getBean("noticeMapper");
            NoticeInfo ni = new NoticeInfo();
            ni.setMsg_type("告警");
            ni.setMsg_title(title);
            ni.setMsg_url("");
            ni.setMsg(msg);
            ni.setIs_see(Const.FALSE);
            ni.setOwner(user.getUserName());
            ni.setCreate_time(new Timestamp(new Date().getTime()));
            ni.setUpdate_time(new Timestamp(new Date().getTime()));
            noticeMapper.insert(ni);
        } catch (Exception e) {
            String error = "类:" + Thread.currentThread().getStackTrace()[1].getClassName() + " 函数:" + Thread.currentThread().getStackTrace()[1].getMethodName() + " 异常: {}";
            logger.error(error, e);
            logger.error("接口无权限告警异常, {}", e);
        }
    }

    private boolean is_operate_log(String url) {

        String[] back_urls = new String[]{"etlEcharts", "getTotalNum", "notice_list", "user_operate_log_list", "user_operate_log_index"};
        for (String back_url : back_urls) {
            if (url.contains(back_url))
                return false;
        }

        return true;
    }

    private List<String> white() {
        List<String> permissions = new ArrayList<>();
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
        //permissions.add("index");
        permissions.add("every_day_notice");
        permissions.add("notice_list");
        permissions.add("readme");
        permissions.add("zdh_help");
        permissions.add("check_captcha");
        permissions.add("get_platform_name");
        permissions.add("get_error_msg");

        return permissions;
    }

    /**
     * 校验是否禁用用户,true:禁用,false:未禁用
     * @return
     */
    private boolean is_unenable(){
        try{
            if (getUser() == null) {
                return false;
            }
            if (StringUtils.isEmpty(getUser().getUserName())) {
                return false;
            }

            RedisUtil redisUtil = (RedisUtil) SpringContext.getBean("redisUtil");

            return redisUtil.exists(Const.ZDH_USER_UNENABLE+"_"+getUser().getUserName());

        }catch (Exception e){
            String error = "类:" + Thread.currentThread().getStackTrace()[1].getClassName() + " 函数:" + Thread.currentThread().getStackTrace()[1].getMethodName() + " 异常: {}";
            logger.error(error, e);
        }

        return false;
    }

    /**
     * 查询是否命中用户名单,命中则不许访问
     *
     * @return
     */
    private boolean is_blacklist() {
        try{
            if (getUser() == null) {
                return false;
            }
            //查询黑名单
            if (StringUtils.isEmpty(getUser().getUserName())) {
                return false;
            }
            RedisUtil redisUtil = (RedisUtil) SpringContext.getBean("redisUtil");
            Object o = redisUtil.get(Const.ZDH_USER_BACKLIST);
            if (o == null) {
                return false;
            }

            if (Arrays.asList(o.toString().split(",")).contains(getUser().getUserName())) {
                return true;
            }
        }catch (Exception e){
            String error = "类:" + Thread.currentThread().getStackTrace()[1].getClassName() + " 函数:" + Thread.currentThread().getStackTrace()[1].getMethodName() + " 异常: {}";
            logger.error(error, e);
        }

        return false;
    }

    /**
     * 校验网址是否可访问,不区分用户,如果想个别用户区分,则使用黑名单限制功能
     *
     * @return
     */
    private boolean is_pass(String url) {
        try{
            if (url.contains("503")) {
                return true;
            }
            RedisUtil redisUtil = (RedisUtil) SpringContext.getBean("redisUtil");
            if (getUser() == null) {
                return true;
            }
            Object pass_user = redisUtil.get(Const.ZDH_IS_PASS_USER);
            if (pass_user != null) {
                if (Arrays.asList(pass_user.toString().split(",")).contains(getUser().getUserName())) {
                    return true;
                }
            }
            if (getUser().getUserName().equalsIgnoreCase("admin") || getUser().getUserName().equalsIgnoreCase("zyc")) {
                return true;
            }
            Object o = redisUtil.get(Const.ZDH_IS_PASS);
            if (o == null) {
                return true;
            }
            if (o != null && o.toString().equalsIgnoreCase("true")) {
                return true;
            }
        }catch (Exception e){
            String error = "类:" + Thread.currentThread().getStackTrace()[1].getClassName() + " 函数:" + Thread.currentThread().getStackTrace()[1].getMethodName() + " 异常: {}";
            logger.error(error, e);
        }

        return false;
    }

    /**
     * ip 是否在黑名单中
     *
     * @param ip
     * @return true:命中黑名单,false:为命中黑名单
     */
    private boolean is_ipblacklist(String ip) {
        try{
            if (getUser() == null) {
                return false;
            }

            RedisUtil redisUtil = (RedisUtil) SpringContext.getBean("redisUtil");

            Object o = redisUtil.get(Const.ZDH_IP_BACKLIST);
            if (o == null) {
                return false;
            }
            if (o != null && !o.toString().contains(ip)) {
                return false;
            }
        }catch (Exception e){
            String error = "类:" + Thread.currentThread().getStackTrace()[1].getClassName() + " 函数:" + Thread.currentThread().getStackTrace()[1].getMethodName() + " 异常: {}";
            logger.error(error, e);
        }

        return true;
    }

    /**
     * 获取ip
     *
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
                    String error = "类:" + Thread.currentThread().getStackTrace()[1].getClassName() + " 函数:" + Thread.currentThread().getStackTrace()[1].getMethodName() + " 异常: {}";
                    logger.error(error, e);
                }
                // 恢复访问控制权限
                fields[i].setAccessible(accessFlag);
            } catch (IllegalArgumentException e) {
                String error = "类:" + Thread.currentThread().getStackTrace()[1].getClassName() + " 函数:" + Thread.currentThread().getStackTrace()[1].getMethodName() + " 异常: {}";
                logger.error(error, e);
            }
        }
    }

}
