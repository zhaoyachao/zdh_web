package com.zyc.zspringboot.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;
import com.zyc.zspringboot.entity.User;
import com.zyc.zspringboot.service.AccountService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import com.zyc.zspringboot.entity.Role;
import com.zyc.zspringboot.service.RoleService;

@Controller
public class LoginController {

    private static Logger logger = LoggerFactory
            .getLogger(LoginController.class);
    @Autowired
    private RoleService roleService;
    @Autowired
    AccountService accountService;

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


    @RequestMapping("getUserInfo")
    @ResponseBody
    public String getUserInfo(){

        User user = (User) SecurityUtils.getSubject().getPrincipal();

        JSONObject json = new JSONObject();
        json.put("userName",user.getUserName());

        return json.toJSONString();
    }


    @RequestMapping("logout")
    public String logout(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        System.out.println("logout");
        //WebUtils.issueRedirect(req, resp, "/login");
        return "redirect:login";
    }

}
