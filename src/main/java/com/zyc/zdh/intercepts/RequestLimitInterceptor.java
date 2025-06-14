package com.zyc.zdh.intercepts;

import com.zyc.zdh.config.SystemConfig;
import com.zyc.zdh.dao.UserOperateLogMapper;
import com.zyc.zdh.entity.*;
import com.zyc.zdh.util.ConfigUtil;
import com.zyc.zdh.util.Const;
import com.zyc.zdh.util.LogUtil;
import com.zyc.zdh.util.SpringContext;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 系统限制拦截器
 */
@Component
@Order(3)
public class RequestLimitInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        if(!isBlack(request, response)){
            return false;
        }
        if(!isPass(request, response)){
            return false;
        }
        if(!isLogin(request, response)){
            return false;
        }
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
//        if (ex != null) {
//            logger.error("Exception occurred during request processing", ex);
//        }
    }

    private boolean isBlack(HttpServletRequest request, HttpServletResponse response) {

        try{
            String reqParam = "";
            SystemFilterParam systemFilterParam = SystemConfig.urlThread.get();
            String url = systemFilterParam.getRequestURL();
            String ipAddr = systemFilterParam.getIp();
            long start = systemFilterParam.getStartTime();
            //校验账号是否启用
            boolean is_unenable = is_unenable();
            //校验ip黑名单
            boolean is_ipbacklist = is_ipblacklist(SystemConfig.urlThread.get().getIp());
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

                userOperateLogInfo.setCreate_time(new Timestamp(System.currentTimeMillis()));
                userOperateLogInfo.setUpdate_time(new Timestamp(System.currentTimeMillis()));
                userOperateLogMapper.insertSelective(userOperateLogInfo);

                if (request.getMethod().equalsIgnoreCase("get")) {
                    response.sendRedirect(request.getContextPath() + "/403");
                } else {
                    response.setCharacterEncoding("UTF-8");
                    response.getWriter().write(ReturnInfo.createInfo(RETURN_CODE.FAIL.getCode(), "命中IP/用户黑名单,禁止访问", "命中IP/用户黑名单,禁止访问"));
                }
                return false;
            }

            return true;
        }catch (Exception e){
            try {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write("服务异常");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        return false;

    }

    private boolean isPass(HttpServletRequest request, HttpServletResponse response) {

        try{
            String reqParam = "";
            SystemFilterParam systemFilterParam = SystemConfig.urlThread.get();
            String url = systemFilterParam.getRequestURL();
            String ipAddr = systemFilterParam.getIp();
            long start = systemFilterParam.getStartTime();

            //校验网址是否可访问
            boolean is_pass = is_pass(getUrl());
            if (!is_pass) {
                LogUtil.warn(this.getClass(), "系统维护中,只有admin用户和zyc用户可访问....");
                String uid = getUser() == null ? "" : getUser().getUserName();
                LogUtil.debug(this.getClass(), "请求源IP:【{}】,用户:【{}】,请求URL:【{}】,类型:【{}】请求参数:【{}】", ipAddr, uid, url, request.getMethod(), reqParam);
                if (request.getMethod().equalsIgnoreCase("get")) {
                    response.sendRedirect(request.getContextPath() + "/503");
                } else {
                    response.setCharacterEncoding("UTF-8");
                    response.getWriter().write(ReturnInfo.createInfo(RETURN_CODE.FAIL.getCode(), "系统维护中", "系统维护中"));
                }
                return false;
            }

            return true;
        }catch (Exception e){
            try {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write("服务异常");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        return false;

    }

    private boolean isLogin(HttpServletRequest request, HttpServletResponse response) {

        try{
            String reqParam = "";
            SystemFilterParam systemFilterParam = SystemConfig.urlThread.get();
            String url = systemFilterParam.getRequestURL();
            String ipAddr = systemFilterParam.getIp();

            //未登录且非登录请求强制跳转到登录页面
            if (!SecurityUtils.getSubject().isAuthenticated()) {
                String whiteUrl = getUrl();
                if (!white().contains(whiteUrl)) {
                    LogUtil.error(this.getClass(), "用户未登录, 请求URL:【{}】", url);
                    LogUtil.debug(this.getClass(), "请求源IP:【{}】,用户:【{}】,请求URL:【{}】,类型:【{}】请求参数:【{}】", ipAddr, "xxxx", url, request.getMethod(), reqParam);
                    response.sendRedirect(request.getContextPath() + "/login");
                }
            }
            return true;
        }catch (Exception e){
            try {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write("服务异常");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        return false;

    }

    private String getUrl() {
        String url = SystemConfig.urlThread.get().getServletPath();
        if (url.startsWith("/")) {
            url = url.substring(1).replaceAll("function:", "");
        }
        url = url.split("\\.")[0];
        return url;
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

            return ConfigUtil.getParamUtil().exists(getUser().getProduct_code(), Const.ZDH_USER_UNENABLE+"_"+getUser().getUserName());

        }catch (Exception e){
            LogUtil.error(this.getClass(), e);
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

            Object o = ConfigUtil.getParamUtil().getValue(getUser().getProduct_code(), Const.ZDH_USER_BACKLIST);
            if (o == null) {
                return false;
            }

            if (Arrays.asList(o.toString().split(",")).contains(getUser().getUserName())) {
                return true;
            }
        }catch (Exception e){
            LogUtil.error(this.getClass(), e);
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
            if (url.endsWith("503")) {
                return true;
            }
            if (getUser() == null) {
                return true;
            }
            Object pass_user = ConfigUtil.getParamUtil().getValue(getUser().getProduct_code(), Const.ZDH_IS_PASS_USER);
            if (pass_user != null) {
                if (Arrays.asList(pass_user.toString().split(",")).contains(getUser().getUserName())) {
                    return true;
                }
            }
            if (getUser().getUserName().equalsIgnoreCase("admin") || getUser().getUserName().equalsIgnoreCase("zyc")) {
                return true;
            }
            Object o = ConfigUtil.getParamUtil().getValue(getUser().getProduct_code(),Const.ZDH_IS_PASS);
            if (o == null) {
                return true;
            }
            if (o != null && o.toString().equalsIgnoreCase("true")) {
                return true;
            }
        }catch (Exception e){
            LogUtil.error(this.getClass(), e);
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
            Object o = ConfigUtil.getParamUtil().getValue(ConfigUtil.getProductCode(), Const.ZDH_IP_BACKLIST);
            if (o == null) {
                return false;
            }
            if (o != null && !o.toString().contains(ip)) {
                return false;
            }
        }catch (Exception e){
            LogUtil.error(this.getClass(), e);
        }

        return true;
    }

}
