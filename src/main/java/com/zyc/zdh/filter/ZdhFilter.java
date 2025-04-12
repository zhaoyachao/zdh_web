package com.zyc.zdh.filter;

import javax.servlet.*;
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
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {

    }
}
