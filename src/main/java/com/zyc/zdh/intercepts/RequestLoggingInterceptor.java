package com.zyc.zdh.intercepts;

import com.zyc.zdh.config.SystemConfig;
import com.zyc.zdh.entity.SystemFilterParam;
import com.zyc.zdh.entity.User;
import com.zyc.zdh.util.Const;
import com.zyc.zdh.util.LogUtil;
import org.apache.shiro.SecurityUtils;
import org.slf4j.MDC;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

/**
 * 日志拦截器
 * 初始化日志信息,初始化请求信息
 */
@Component
@Order(1)
public class RequestLoggingInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        MDC.put(Const.MDC_LOG_ID, UUID.randomUUID().toString());

        if (getUser() == null) {
            MDC.put(Const.MDC_USER_ID, UUID.randomUUID().toString());
        } else {
            MDC.put(Const.MDC_USER_ID, getUser().getUserName());
        }
        logRequest(request);
        return true;
    }

    public User getUser() {
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        return user;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        // 可以在此处记录响应信息
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        MDC.remove(Const.MDC_USER_ID);
        MDC.remove(Const.MDC_LOG_ID);
        if (ex != null) {
            LogUtil.error(this.getClass(), "Exception occurred during request after processing", ex);
        }
    }

    private void logRequest(HttpServletRequest request) {

        try{
            SystemFilterParam systemFilterParam = new SystemFilterParam();
            String ip = getRemoteHost(request);
            systemFilterParam.setIp(ip);
            systemFilterParam.setMethod(request.getMethod());
            systemFilterParam.setRequestURI(request.getRequestURI());
            systemFilterParam.setContextPath(request.getContextPath());
            systemFilterParam.setServletPath(request.getServletPath());
            systemFilterParam.setRequestURL(request.getRequestURL().toString());
            systemFilterParam.setUrl(request.getRequestURL().toString());
            systemFilterParam.setStartTime(System.currentTimeMillis());

            SystemConfig.urlThread.set(systemFilterParam);
        }catch (Exception e){
            LogUtil.error(this.getClass(), "Exception occurred during request pre processing", e);
        }

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

        return "0:0:0:0:0:0:0:1".equals(ip) ? "127.0.0.1" : ip.split(",")[0];
    }
}
