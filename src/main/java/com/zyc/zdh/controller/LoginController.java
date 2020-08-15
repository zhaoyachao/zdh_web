package com.zyc.zdh.controller;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;
import com.zyc.zdh.entity.User;
import com.zyc.zdh.service.AccountService;
import com.zyc.zdh.service.JemailService;
import com.zyc.zdh.shiro.MyRealm;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import com.zyc.zdh.service.RoleService;

@Controller
public class LoginController {

    private static Logger logger = LoggerFactory
            .getLogger(LoginController.class);
    @Autowired
    private RoleService roleService;
    @Autowired
    AccountService accountService;
    @Autowired
    MyRealm myRealm;
    @Autowired
    JemailService jemailService;

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
    public String register2(User user) {
        System.out.println("========注册-----post");
        JSONObject json = new JSONObject();
        //散列hash 算法和shiro 的算法保持一致
        //user.setPassword(new SimpleHash("md5", new String(user.getPassword()), null, 1).toString());

        //判断是否存在用户
        List<User> users=accountService.findByUserName(user);

        if(users.size()>0){
            json.put("error", "账户已存在");
            return json.toJSONString();
        }
        int result = accountService.insert(user);

        if (result > 0) {
            json.put("error", "");
            json.put("success", "200");
            return json.toJSONString();
        } else {
            json.put("error", "");
            return json.toJSONString();
        }
    }

    @RequestMapping("login")
    public String login(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        System.out.println("login =======start==");
        Subject subject = SecurityUtils.getSubject();
        if (subject.isAuthenticated()) {
            // 已经登录，重新登录
            //WebUtils.issueRedirect(request, response, "/index");
            //SecurityUtils.getSecurityManager().logout(subject);
            return "redirect:index";
        }
        System.out.println("login ======end===");
        return "login";
    }

    @RequestMapping("index")
    public String getIndex() {
        return "index";
    }


    @RequestMapping(value = "user", method = RequestMethod.GET)
    public String updateUser(String password){

        return "user";
    }

    @RequestMapping(value = "user", method = RequestMethod.POST,produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String updateUser2(User user){
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
               return json.toJSONString();
           }
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


        json.put("status","200");
        return json.toJSONString();
    }


    @RequestMapping("getUserInfo")
    @ResponseBody
    public String getUserInfo(){

        User user = (User) SecurityUtils.getSubject().getPrincipal();

        JSONObject json = new JSONObject();
        json.put("id",user.getId());
        json.put("userName",user.getUserName());
        //json.put("password",user.getUserName());
        json.put("email",user.getEmail());
        json.put("is_use_email",user.getIs_use_email());
        json.put("phone",user.getPhone());
        json.put("is_use_phone",user.getIs_use_phone());

        return json.toJSONString();
    }
    @RequestMapping(value = "retrieve_password", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String retrieve_password(String username){
        User user=new User();
        user.setUserName(username);
        List<User> users=accountService.findByUserName(user);

        JSONObject json = new JSONObject();
        if(users.size()!=1){
            json.put("value","请检查用户名是否正确");
        }else{
            System.out.println("username:"+username);
            jemailService.sendEmail(new String[]{users.get(0).getEmail()},"找回密码","你好,你正在使用大数据采集平台zdh,您的密码是:"
                    +users.get(0).getPassword());
            json.put("value","密码已发送到指定邮箱");
        }
        return json.toJSONString();
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
