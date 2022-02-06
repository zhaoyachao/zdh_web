package com.zyc.zdh.controller;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.google.gson.JsonObject;
import com.zyc.zdh.dao.AccountMapper;
import com.zyc.zdh.entity.AccountInfo;
import com.zyc.zdh.entity.RETURN_CODE;
import com.zyc.zdh.entity.ReturnInfo;
import com.zyc.zdh.entity.User;
import com.zyc.zdh.service.AccountService;
import com.zyc.zdh.service.JemailService;
import com.zyc.zdh.shiro.MyAuthenticationToken;
import com.zyc.zdh.shiro.MyRealm;
import com.zyc.zdh.util.Const;
import com.zyc.zdh.util.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.annotation.Transient;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import com.zyc.zdh.service.RoleService;
import tk.mybatis.mapper.entity.Example;

@Controller
public class LoginController {

    private static Logger logger = LoggerFactory.getLogger(LoginController.class);

    public static ApplicationContext context=null;

    @Autowired
    private RoleService roleService;
    @Autowired
    AccountService accountService;
    @Autowired
    MyRealm myRealm;
    @Autowired
    JemailService jemailService;
    @Autowired
    AccountMapper accountMapper;

    @RequestMapping("/")
    public String getLogin() {
        return "redirect:index";
    }

    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public String register() {
        return "register";
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
    @ResponseBody
    @Transactional
    public String register2(User user) {
        JSONObject json = new JSONObject();
        try{
            //散列hash 算法和shiro 的算法保持一致
            //user.setPassword(new SimpleHash("md5", new String(user.getPassword()), null, 1).toString());

            //判断是否存在用户
            List<User> users=accountService.findByUserName(user);

            if(users.size()>0){
                json.put("error", "账户已存在");
                return ReturnInfo.createInfo(RETURN_CODE.FAIL.getCode(), "账户以存在", null);
            }
            user.setEnable(Const.TRUR);
            int result = accountService.insert(user);
            if (result > 0) {
                return ReturnInfo.createInfo(RETURN_CODE.SUCCESS.getCode(), "注册成功", null);
            } else {
                return ReturnInfo.createInfo(RETURN_CODE.FAIL.getCode(), "注册失败", null);
            }
        }catch (Exception e){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ReturnInfo.createInfo(RETURN_CODE.FAIL.getCode(), "注册失败", e);
        }
    }

    @RequestMapping(value = "login", method = RequestMethod.GET)
    public String login1(Model model, HttpServletRequest request, HttpServletResponse response){
        model.addAttribute("msg","登陆");
        Subject subject = SecurityUtils.getSubject();
        if (subject.isAuthenticated()) {
                //WebUtils.issueRedirect(request, response, "/index");
                //SecurityUtils.getSecurityManager().logout(subject);
                return "redirect:index";
            }
        return "login";
    }

    @RequestMapping(value = "login", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String login(Model model, HttpServletRequest request, HttpServletResponse response) {
        try{
            Subject subject = SecurityUtils.getSubject();
            String captcha=WebUtils.getCleanParam(request, "captcha");
            String username=WebUtils.getCleanParam(request, "username");
            String password=WebUtils.getCleanParam(request, "password");
            boolean rememberMe=WebUtils.isTrue(request, "rememberMe");
            String session_captcha = (String)((HttpServletRequest) request).getSession().getAttribute(MyAuthenticationToken.captcha_key);
            MyAuthenticationToken token=new MyAuthenticationToken(username,password,rememberMe,"",captcha,"",session_captcha);
            subject.login(token);
            return ReturnInfo.createInfo(RETURN_CODE.SUCCESS.getCode(),"登陆成功","index.html");
        }catch (Exception e){
            String error = "类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常:"+e.getMessage()+", 异常详情:{}";
            logger.error(error, e);
            return ReturnInfo.createInfo(RETURN_CODE.FAIL.getCode(),e.getMessage(),"login.html");
        }
    }

    @RequestMapping("index")
    public String getIndex() {
        Subject subject = SecurityUtils.getSubject();
        if (subject.isAuthenticated()) {
            //WebUtils.issueRedirect(request, response, "/index");
            //SecurityUtils.getSecurityManager().logout(subject);
            return "index";
        }
        return "login";
    }

    @RequestMapping(value = "shutdown", method = RequestMethod.GET)
    public String shutdown(){
        ConfigurableApplicationContext ctx = (ConfigurableApplicationContext) context;
        ctx.close();
        System.exit(0);
        return "user";
    }


    @RequestMapping(value = "user", method = RequestMethod.GET)
    public String updateUser(){

        return "user";
    }

    @RequestMapping(value = "user", method = RequestMethod.POST,produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String updateUser2(User user){
        try{
            debugInfo(user);
            JSONObject json = new JSONObject();
            User user_old = (User) SecurityUtils.getSubject().getPrincipal();
            if(user.getPassword().equals("")){
                user.setPassword(user_old.getPassword());
            }

            user.setId(user_old.getId());
            if(!user_old.getUserName().equals(user.getUserName())){
                List<User> users= accountService.findByUserName(user);
                if(users.size()>0){
                    json.put("status","已经存在相同用户名");
                    return ReturnInfo.createInfo(RETURN_CODE.FAIL.getCode(), "已经存在相同用户名", null);
                }
            }
            User user_old2 = accountService.selectByPrimaryKey(user.getId());
            if(StringUtils.isEmpty(user.getSignature())){
                user.setSignature(user_old2.getSignature());
            }

            accountService.updateUser(user);

            Subject subject = SecurityUtils.getSubject();

            Cache<Object,AuthenticationInfo> cache=myRealm.getAuthenticationCache();
            if (cache!=null){
                cache.remove(user.getUserName());
            }


            PrincipalCollection principalCollection = subject.getPrincipals();
            String realmName = principalCollection.getRealmNames().iterator().next();
            PrincipalCollection newPrincipalCollection =
                    new SimplePrincipalCollection(user, realmName);
            subject.runAs(newPrincipalCollection);
            return ReturnInfo.createInfo(RETURN_CODE.SUCCESS.getCode(), "更新成功", null);
        }catch (Exception e){
            return ReturnInfo.createInfo(RETURN_CODE.FAIL.getCode(), "更新失败", e);
        }
    }


    @RequestMapping(value="getUserInfo", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String getUserInfo(){

        User user = (User) SecurityUtils.getSubject().getPrincipal();
        user.setPassword("");
//        JSONObject json = new JSONObject();
//        json.put("id",user.getId());
//        json.put("userName",user.getUserName());
//        //json.put("password",user.getUserName());
//        json.put("email",user.getEmail());
//        json.put("is_use_email",user.getIs_use_email());
//        json.put("phone",user.getPhone());
//        json.put("is_use_phone",user.getIs_use_phone());

        return ReturnInfo.createInfo(RETURN_CODE.SUCCESS.getCode(),"获取用户信息", user);

    }
    @RequestMapping(value = "retrieve_password", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String retrieve_password(String username){
        User user=new User();
        user.setUserName(username);
        List<User> users=accountService.findByUserName(user);

        if(users.size()!=1){
            return ReturnInfo.createInfo(RETURN_CODE.FAIL.getCode(),"请检查用户名是否正确", null );
        }else{
            System.out.println("username:"+username);
            jemailService.sendEmail(new String[]{users.get(0).getEmail()},"找回密码","你好,你正在使用大数据采集平台zdh,您的密码是:"
                    +users.get(0).getPassword());
            return ReturnInfo.createInfo(RETURN_CODE.SUCCESS.getCode(),"密码已发送到注册邮箱,如果你已忘记注册邮箱,请联系管理员解决", null );
        }
    }

    @RequestMapping(value = "user_names", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String user_names(String username){

        AccountInfo accountInfo=new AccountInfo();
        Example example=new Example(accountInfo.getClass());
        Example.Criteria criteria=example.createCriteria();
        if(!StringUtils.isEmpty(username)){
            criteria.andLike("user_name", "%"+username+"%");
        }
        List<AccountInfo> accountInfos=accountMapper.selectByExample(example);
        List<JSONObject> user_names=new ArrayList<>();
        for (AccountInfo acc:accountInfos){
            JSONObject js=new JSONObject();
            js.put("id", acc.getUser_name());
            js.put("name", acc.getUser_name());
            user_names.add(js);
        }


        return JSONObject.toJSONString(user_names);
    }


    @RequestMapping("logout")
    public String logout(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        Subject subject = SecurityUtils.getSubject();
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        Cache<Object,AuthenticationInfo> cache=myRealm.getAuthenticationCache();
        if (cache!=null && user !=null){
            cache.remove(user.getUserName());
        }

        subject.logout();
        System.out.println("logout");
        //WebUtils.issueRedirect(req, resp, "/login");
        return "redirect:login";
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
                    String error = "类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常:"+e.getMessage()+", 异常详情:{}";
                    logger.error(error, e);
                }
                // 恢复访问控制权限
                fields[i].setAccessible(accessFlag);
            } catch (IllegalArgumentException e) {
                 logger.error("类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常:"+e.getMessage()+", 异常详情:{}", e);
            }
        }
    }

}
