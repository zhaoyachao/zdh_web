package com.zyc.zdh.filter;

import com.zyc.zdh.config.SystemConfig;
import com.zyc.zdh.entity.SystemFilterParam;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * zdh初始化基础参数过滤器
 */
public class ZdhFilter implements Filter {



    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        SystemFilterParam systemFilterParam = new SystemFilterParam();
        HttpServletRequest request = (HttpServletRequest)servletRequest;
        String ip = getRemoteHost(request);
        systemFilterParam.setIp(ip);

        systemFilterParam.setRequestURI(request.getRequestURI());
        systemFilterParam.setContextPath(request.getContextPath());
        systemFilterParam.setServletPath(request.getServletPath());
        systemFilterParam.setRequestURL(request.getRequestURL().toString());
        systemFilterParam.setUrl(request.getRequestURL().toString());
        SystemConfig.urlThread.set(systemFilterParam);
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {

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
}
