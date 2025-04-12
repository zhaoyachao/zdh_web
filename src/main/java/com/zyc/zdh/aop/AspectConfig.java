package com.zyc.zdh.aop;

import com.zyc.zdh.annotation.White;
import com.zyc.zdh.config.SystemConfig;
import com.zyc.zdh.dao.NoticeMapper;
import com.zyc.zdh.dao.UserOperateLogMapper;
import com.zyc.zdh.entity.*;
import com.zyc.zdh.exception.ZdhException;
import com.zyc.zdh.shiro.RedisUtil;
import com.zyc.zdh.util.*;
import org.apache.shiro.SecurityUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RedissonClient;
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
import java.util.ArrayList;
import java.util.List;


/***
 * ClassName: AspectConfig   
 * @author zyc-admin
 * @date 2017年12月21日
 * @Description: TODO
 */
@Aspect
@Component
public class AspectConfig implements Ordered {

    @Pointcut("execution(* com.zyc.zdh.quartz..*(..))")
    public void pointcutMethod() {
    }

    //在方法上的自定义注解使用@annotation,类上自定义注解使用@within(com.zyc.springboot.类名)
    @Pointcut("@annotation(com.zyc.zdh.aop.Log)")
    public void pointcutMethod2() {
    }

    @Pointcut("execution(* com.zyc.zdh.controller.*.*.*(..)) || (execution(* com.zyc.zdh.controller.*.*(..)) && !execution(* com.zyc.zdh.controller.MyErrorConroller.*(..)) && !execution(* com.zyc.zdh.controller.LoginController.getLogin(..)) && !execution(* com.zyc.zdh.controller.LoginController.captcha(..)) && !execution(* com.zyc.zdh.controller.LoginController.login(..)) && !execution(* com.zyc.zdh.controller.LoginController.login1(..)) && !execution(* com.zyc.zdh.controller.LoginController.getIndex(..)) && !execution(* com.zyc.zdh.controller.PermissionApiController.*(..)) && !execution(* com.zyc.zdh.controller.BaseController.*(..)) )")
    public void pointcutMethod3() {
    }


    @Around(value = "pointcutMethod3()")
    public Object aroundLog(ProceedingJoinPoint pjp) throws Exception {
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
                response.setDateHeader("Expires", System.currentTimeMillis()+1000*5);
            }
            SystemFilterParam systemFilterParam = SystemConfig.urlThread.get();
            //IP地址
            String ipAddr = systemFilterParam.getIp();
            String url = systemFilterParam.getRequestURL();

            long start = System.currentTimeMillis();
            String classType = pjp.getTarget().getClass().getName();
            Signature sig = pjp.getSignature();
            MethodSignature msig = (MethodSignature) sig;
            Object target = pjp.getTarget();
            LocalVariableTableParameterNameDiscoverer u = new LocalVariableTableParameterNameDiscoverer();
            Method currentMethod = target.getClass().getMethod(msig.getName(), msig.getParameterTypes());
            if (currentMethod.getName().contains("initBinder")) {
                //MDC.remove("user_id");
                return pjp.proceed();
            }

            String reqParam = "";

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
                    userOperateLogInfo.setCreate_time(new Timestamp(System.currentTimeMillis()));
                    userOperateLogInfo.setUpdate_time(new Timestamp(System.currentTimeMillis()));
                    userOperateLogMapper.insertSelective(userOperateLogInfo);
                } catch (Exception ex) {
                    LogUtil.error(this.getClass(), ex);
                }

                if (e.getMessage().contains("没有权限")) {
                    //MDC.remove("user_id");
                    String uid = getUser() == null ? "" : getUser().getUserName();
                    LogUtil.error(this.getClass(), "请求源IP:【{}】,用户:【{}】,请求URL:【{}】,类型:【{}】,请求参数:【{}】, 当前请求无权限", ipAddr, uid, url, request.getMethod(), reqParam);
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
                    //MDC.remove("user_id");
                    String uid = getUser() == null ? "" : getUser().getUserName();
                    LogUtil.error(this.getClass(), "请求源IP:【{}】,用户:【{}】,请求URL:【{}】,类型:【{}】,请求参数:【{}】,当前请求异常,强制跳转404", ipAddr, uid, url, request.getMethod(), reqParam);
                    return "404";
                }
                LogUtil.error(this.getClass(), e);
                //MDC.remove("user_id");
                if (returnName.contains("ReturnInfo")) {
                    return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "系统错误", e);
                }
                return ReturnInfo.createInfo(RETURN_CODE.FAIL.getCode(), "系统错误", e);
            }
            String uid = getUser() == null ? "" : getUser().getUserName();
            String city = IpUtil.getCityByIp(ipAddr);
            if (is_operate_log(url)){
                LogUtil.debug(this.getClass(), "请求源IP:【{}】【{}】,用户:【{}】,请求URL:【{}】,类型:【{}】,请求参数:【{}】", ipAddr, city, uid, url, request.getMethod(), reqParam);
            }
            Object o = pjp.proceed();
            if (is_operate_log(url)){
                LogUtil.debug(this.getClass(), "请求源IP:【{}】【{}】,用户:【{}】,请求URL:【{}】,结束", ipAddr, city, uid, url);
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
                    userOperateLogInfo.setCreate_time(new Timestamp(System.currentTimeMillis()));
                    userOperateLogInfo.setUpdate_time(new Timestamp(System.currentTimeMillis()));

                    if (o instanceof String) {
                        if (((String) o).length() > 6400) {
                            userOperateLogInfo.setOperate_output(((String) o).substring(0, 256));
                        } else {
                            userOperateLogInfo.setOperate_output((String) o);
                        }
                    } else if (o instanceof List) {
                        userOperateLogInfo.setOperate_output(JsonUtil.formatJsonString(((List) o)));
                        if (((List) o).size() > 10) {
                            userOperateLogInfo.setOperate_output(JsonUtil.formatJsonString(((List) o).subList(0, 10)));
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
                        String retStr = JsonUtil.formatJsonString(returnInfo);
                        if (retStr.length() > 6400) {
                            retStr = retStr.substring(0,256);
                        }
                        userOperateLogInfo.setOperate_output(retStr);
                    } else {
                        //未知的object类型
                        userOperateLogInfo.setOperate_output(JsonUtil.formatJsonString("未知的返回值类型,请管理员确认"));
                    }
                    userOperateLogMapper.insertSelective(userOperateLogInfo);
                }
            } catch (Exception e) {
                LogUtil.error(this.getClass(), e);
            }
            return o;
        } catch (Throwable e) {
            LogUtil.error(this.getClass(), e);
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
            LogUtil.info(this.getClass(), log.value());
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
                if (targetMethod.getAnnotation(White.class).value()) {
                    is_white = true;
                }
            }
        }
        for (Annotation annotation : annotations) {
            //此处可以改成自定义的注解
            if (annotation.annotationType().equals(RequestMapping.class)) {
                RequestMethod[] requestMethods = ((RequestMapping) annotation).method();
                if (requestMethods != null && requestMethods.length > 0) {
                    //校验请求类型和注解是否一致
                    if (requestMethods.length > 1) {
                        LogUtil.error(this.getClass(), getUrl(request) + "请求类型: " + request.getMethod() + ", 注解类型: " + JsonUtil.formatJsonString(requestMethods));
                    } else {
                        if (!request.getMethod().equalsIgnoreCase(requestMethods[0].toString())) {
                            LogUtil.error(this.getClass(), getUrl(request) + "请求类型: " + request.getMethod() + ", 注解类型: " + JsonUtil.formatJsonString(requestMethods));
                        }
                    }
                } else {
                    //logger.error(getUrl(request)+"请求类型: "+request.getMethod()+", 注解类型: "+JsonUtil.formatJsonString(requestMethods));
                }
                reqParam = JsonUtil.formatJsonString(request.getParameterMap());
                //验证权限
                String url = getUrl(request);
                String method = request.getMethod();
                if (white().contains(url) || is_white || url.contains("smart_doc")) {
                    break;
                }
                if(getUser()!=null && getUser().getRoles().contains(Const.SUPER_ADMIN_ROLE)){
                    //超级管理员有所有权限
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
            LogUtil.error(this.getClass(), getUrl(request) + "============检测属于get请求,请检查是否合法");
        }
        return reqParam;
    }

    private String getUrl(HttpServletRequest request) {
        String url = SystemConfig.urlThread.get().getServletPath();
        if (url.startsWith("/")) {
            url = url.substring(1).replaceAll("function:", "");
        }
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
            ni.setCreate_time(new Timestamp(System.currentTimeMillis()));
            ni.setUpdate_time(new Timestamp(System.currentTimeMillis()));
            noticeMapper.insertSelective(ni);
        } catch (Exception e) {
            LogUtil.error(this.getClass(), e);
            LogUtil.error(this.getClass(), "接口无权限告警异常, {}", e);
        }
    }

    private boolean is_operate_log(String url) {

        String[] back_urls = new String[]{"etlEcharts", "getTotalNum", "notice_list", "user_operate_log_list", "user_operate_log_index"};
        for (String back_url : back_urls) {
            if (url.contains(back_url)) {
                return false;
            }
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
                    LogUtil.error(this.getClass(), e);
                }
                // 恢复访问控制权限
                fields[i].setAccessible(accessFlag);
            } catch (IllegalArgumentException e) {
                LogUtil.error(this.getClass(), e);
            }
        }
    }

}
