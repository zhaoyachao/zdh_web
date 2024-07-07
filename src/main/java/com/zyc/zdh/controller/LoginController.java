package com.zyc.zdh.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.fastjson.JSONObject;
import com.zyc.zdh.dao.PermissionMapper;
import com.zyc.zdh.entity.PermissionUserInfo;
import com.zyc.zdh.entity.RETURN_CODE;
import com.zyc.zdh.entity.ReturnInfo;
import com.zyc.zdh.entity.User;
import com.zyc.zdh.service.AccountService;
import com.zyc.zdh.service.JemailService;
import com.zyc.zdh.shiro.MyAuthenticationToken;
import com.zyc.zdh.shiro.MyRealm;
import com.zyc.zdh.util.ConfigUtil;
import com.zyc.zdh.util.Const;
import com.zyc.zdh.util.Encrypt;
import io.swagger.v3.oas.annotations.Operation;
import org.apache.commons.lang3.StringUtils;
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
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import tk.mybatis.mapper.entity.Example;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.regex.Pattern;

/**
 * 登录服务
 */
@Controller
public class LoginController {

    private static Logger logger = LoggerFactory.getLogger(LoginController.class);

    public static ApplicationContext context = null;

    @Autowired
    private AccountService accountService;
    @Autowired
    private MyRealm myRealm;
    @Autowired
    private JemailService jemailService;
    @Autowired
    private PermissionMapper permissionMapper;

    /**
     * 系统根页面
     * @return
     */
    @RequestMapping("/")
    public String getLogin() {
        return "redirect:index";
    }

    /**
     * 注册页面
     * @return
     */
    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public String register() {
        return "register";
    }

    /**
     * 注册
     * @param user
     * @return
     */
    @SentinelResource(value = "register", blockHandler = "handleReturn")
    @Operation(summary = "register", description = "user register")
    @RequestMapping(value = "/register", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation = Propagation.NESTED)
    public ReturnInfo<Object> register2(User user) {
        JSONObject json = new JSONObject();
        try {
            //自定义加密 算法和shiro 的算法保持一致
            //user.setPassword(new SimpleHash("md5", new String(user.getPassword()), null, 2).toString());
            user.setPassword(Encrypt.AESencrypt(user.getPassword()));

            //判断是否是字母和数字
            String regex = "^([a-zA-Z0-9])*$";
            if(!user.getUserName().matches(regex)){
                return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "用户名必须只包含字母或者数字", null);
            }

            //判断是否存在用户
            List<User> users = accountService.findByUserName(user);

            if (users.size() > 0) {
                json.put("error", "账户已存在");
                return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "账户以存在", null);
            }

            //增加特别信息校验
            String pattern_str = "(;|\\*|expr \\s*|delete \\s*)";
            Pattern pattern = Pattern.compile(pattern_str, Pattern.CASE_INSENSITIVE);
            if (pattern.matcher(user.getUserName()).find() || pattern.matcher(user.getPassword()).find()) {
                //此处可做校验判断,是否加入ip黑名单
                return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "账户包含不合法字符,请修改后提交", null);
            }
            user.setEnable(Const.TRUR);
            int result = accountService.insert(user);
            PermissionUserInfo pui = new PermissionUserInfo();
            pui.setUser_account(user.getUserName());
            pui.setUser_name(user.getUserName());
            String product_code = ConfigUtil.getValue("zdh.product", "zdh");
            pui.setProduct_code(product_code);
            List<PermissionUserInfo> puis = permissionMapper.select(pui);
            if(puis != null && puis.size() > 0){
                throw new Exception("存在重复账号");
            }
            pui.setEnable(Const.TRUR);
            if(!StringUtils.isEmpty(user.getEmail())){
                pui.setEmail(user.getEmail());
            }

            pui.setCreate_time(new Timestamp(System.currentTimeMillis()));
            pui.setUpdate_time(new Timestamp(System.currentTimeMillis()));

            String roles = ConfigUtil.getValue("zdp.init.roles","role_base");
            pui.setRoles(roles);

            pui.setTag_group_code("");
            permissionMapper.insertSelective(pui);
            if (result > 0) {
                return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "注册成功", null);
            } else {
                return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "注册失败", null);
            }
        } catch (Exception e) {
            String error = "类:" + Thread.currentThread().getStackTrace()[1].getClassName() + " 函数:" + Thread.currentThread().getStackTrace()[1].getMethodName() + " 异常: {}";
            logger.error(error, e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "注册失败", e);
        }
    }

    /**
     * 登录页面
     * @param model
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "login", method = RequestMethod.GET)
    public String login1(Model model, HttpServletRequest request, HttpServletResponse response) {
        model.addAttribute("msg", "登陆");
        Subject subject = SecurityUtils.getSubject();
        if (subject.isAuthenticated()) {
            logger.info("用户已登录,跳转到首页");
            //WebUtils.issueRedirect(request, response, "/index");
            //SecurityUtils.getSecurityManager().logout(subject);
            return "redirect:index";
        }
        return "login";
    }

    /**
     * 登录
     * 2022-06-18 因登录时提示不友好,删除ResponseBody注解,并在session中存储错误信息
     * @param model
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "login", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
    //@ResponseBody
    public String login(Model model, HttpServletRequest request, HttpServletResponse response) {
        try {
            Subject subject = SecurityUtils.getSubject();
            String captcha = WebUtils.getCleanParam(request, "captcha");
            String username = WebUtils.getCleanParam(request, "username");
            String password = WebUtils.getCleanParam(request, "password");
            boolean rememberMe = WebUtils.isTrue(request, "rememberMe");
            String session_captcha = (String) ((HttpServletRequest) request).getSession().getAttribute(MyAuthenticationToken.captcha_key);
            MyAuthenticationToken token = new MyAuthenticationToken(username, password, rememberMe, "", captcha, "", session_captcha);
            subject.login(token);
            return "index";
            //return ReturnInfo.createInfo(RETURN_CODE.SUCCESS.getCode(),"登陆成功","index.html");
        } catch (Exception e) {
            String error = "类:" + Thread.currentThread().getStackTrace()[1].getClassName() + " 函数:" + Thread.currentThread().getStackTrace()[1].getMethodName() + " 异常: {}";
            logger.error(error, e);
            ((HttpServletRequest) request).getSession().setAttribute("error", e.getMessage());
            return "login";
            //return ReturnInfo.createInfo(RETURN_CODE.FAIL.getCode(),e.getMessage(),"login.html");
        }
    }

    /**
     * 获取错误信息
     * @param request
     * @param response
     * @param captcha
     * @return
     */
    @SentinelResource(value = "get_error_msg", blockHandler = "handleReturn")
    @RequestMapping(value = "/get_error_msg", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<String> get_error_msg(HttpServletRequest request, HttpServletResponse response, String captcha) {
        try {
            if (((HttpServletRequest) request).getSession().getAttribute("error") != null) {
                String msg = ((HttpServletRequest) request).getSession().getAttribute("error").toString();
                ((HttpServletRequest) request).getSession().removeAttribute("error");
                if (!StringUtils.isEmpty(msg)) {
                    return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), msg, "login.html");
                }
            }
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "", "login.html");
        } catch (Exception e) {
            String error = "类:" + Thread.currentThread().getStackTrace()[1].getClassName() + " 函数:" + Thread.currentThread().getStackTrace()[1].getMethodName() + " 异常: {}";
            logger.error(error, e);
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), e.getMessage(), "login.html");
        }
    }

    /**
     * 校验验证码
     * @param request
     * @param response
     * @param captcha 验证码
     * @return
     */
    @SentinelResource(value = "check_captcha", blockHandler = "handleReturn")
    @RequestMapping(value = "/check_captcha", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<String> check_captcha(HttpServletRequest request, HttpServletResponse response, String captcha) {
        try {

            String session_captcha = (String) ((HttpServletRequest) request).getSession().getAttribute(MyAuthenticationToken.captcha_key);

            if (!session_captcha.equalsIgnoreCase(captcha)) {
                throw new Exception("验证码错误");
            }
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "登陆成功", "index.html");
        } catch (Exception e) {
            String error = "类:" + Thread.currentThread().getStackTrace()[1].getClassName() + " 函数:" + Thread.currentThread().getStackTrace()[1].getMethodName() + " 异常: {}";
            logger.error(error, e);
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), e.getMessage(), "login.html");
        }
    }

    /**
     * 首页
     * @return
     */
    @RequestMapping(value = "index", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
    public String getIndex() {
        Subject subject = SecurityUtils.getSubject();
        if (subject.isAuthenticated()) {
            //WebUtils.issueRedirect(request, response, "/index");
            //SecurityUtils.getSecurityManager().logout(subject);
            return "index";
        }
        return "redirect:login";
    }

    /**
     * 下线系统
     * @return
     */
    @RequestMapping(value = "shutdown", method = RequestMethod.GET)
    public String shutdown() {
        ConfigurableApplicationContext ctx = (ConfigurableApplicationContext) context;
        ctx.close();
        System.exit(0);
        return "user";
    }


    /**
     * 个人信息页面
     * @return
     */
    @RequestMapping(value = "user", method = RequestMethod.GET)
    public String updateUser() {

        return "user";
    }

    /**
     * 个人信息更新
     * @param user
     * @return
     */
    @SentinelResource(value = "user", blockHandler = "handleReturn")
    @RequestMapping(value = "user", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<Object> updateUser2(User user) {
        try {
            debugInfo(user);
            JSONObject json = new JSONObject();
            User user_old = (User) SecurityUtils.getSubject().getPrincipal();
            if (user.getPassword().equals("")) {
                user.setPassword(user_old.getPassword());
            }else{
                user.setPassword(Encrypt.AESencrypt(user.getPassword()));
            }

            user.setId(user_old.getId());
            if (!user_old.getUserName().equals(user.getUserName())) {
                List<User> users = accountService.findByUserName(user);
                if (users.size() > 0) {
                    json.put("status", "已经存在相同用户名");
                    return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "已经存在相同用户名", null);
                }
            }
            User user_old2 = accountService.selectByPrimaryKey(user.getId());
            if (StringUtils.isEmpty(user.getSignature())) {
                user.setSignature(user_old2.getSignature());
            }

            accountService.updateUser(user);

            Subject subject = SecurityUtils.getSubject();

            Cache<Object, AuthenticationInfo> cache = myRealm.getAuthenticationCache();
            if (cache != null) {
                cache.remove(user.getUserName());
            }


            PrincipalCollection principalCollection = subject.getPrincipals();
            String realmName = principalCollection.getRealmNames().iterator().next();
            PrincipalCollection newPrincipalCollection =
                    new SimplePrincipalCollection(user, realmName);
            subject.runAs(newPrincipalCollection);
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "更新成功", null);
        } catch (Exception e) {
            String error = "类:" + Thread.currentThread().getStackTrace()[1].getClassName() + " 函数:" + Thread.currentThread().getStackTrace()[1].getMethodName() + " 异常: {}";
            logger.error(error, e);
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "更新失败", e);
        }
    }


    /**
     * 获取用户信息
     * @return
     */
    @SentinelResource(value = "getUserInfo", blockHandler = "handleReturn")
    @RequestMapping(value = "getUserInfo", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<User> getUserInfo() {

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

        return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "获取用户信息", user);

    }

    /**
     * 找回密码
     * @param username
     * @return
     */
    @SentinelResource(value = "retrieve_password", blockHandler = "handleReturn")
    @RequestMapping(value = "retrieve_password", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo retrieve_password(String username) {
        try{
            User user = new User();
            user.setUserName(username);
            List<User> users = accountService.findByUserName(user);

            if (users.size() != 1) {
                return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "请检查用户名是否正确", null);
            } else {
                System.out.println("username:" + username);
                jemailService.sendEmail(new String[]{users.get(0).getEmail()}, "找回密码", "你好,你正在使用大数据采集平台zdh,您的密码是:"
                        + Encrypt.AESdecrypt(users.get(0).getPassword()));
                return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "密码已发送到注册邮箱,如果你已忘记注册邮箱,请联系管理员解决", null);
            }
        }catch (Exception e){
            String error = "类:" + Thread.currentThread().getStackTrace()[1].getClassName() + " 函数:" + Thread.currentThread().getStackTrace()[1].getMethodName() + " 异常: {}";
            logger.error(error, e);
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "找回密码异常", e);
        }

    }

    /**
     * 获取账号名
     * @param username 关键字
     * @return
     */
    @SentinelResource(value = "user_names", blockHandler = "handleReturn")
    @RequestMapping(value = "user_names", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public  ReturnInfo<List<JSONObject>> user_names(String username) {

        try{
            PermissionUserInfo accountInfo = new PermissionUserInfo();
            Example example = new Example(accountInfo.getClass());
            Example.Criteria criteria = example.createCriteria();
            if (!StringUtils.isEmpty(username)) {
                criteria.andLike("user_account", "%" + username + "%");
            }

            List<PermissionUserInfo> permissionUserInfos = permissionMapper.selectByExample(example);
            List<JSONObject> user_names = new ArrayList<>();
            for (PermissionUserInfo acc : permissionUserInfos) {
                JSONObject js = new JSONObject();
                js.put("id", acc.getUser_account());
                js.put("name", acc.getUser_account());
                user_names.add(js);
            }
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "查询成功", user_names);
        }catch (Exception e){
            String error = "类:" + Thread.currentThread().getStackTrace()[1].getClassName() + " 函数:" + Thread.currentThread().getStackTrace()[1].getMethodName() + " 异常: {}";
            logger.error(error, e);
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "查询失败", e);
        }

    }


    /**
     * 退出
     * @param req
     * @param resp
     * @return
     * @throws ServletException
     * @throws IOException
     */
    @RequestMapping("logout")
    public String logout(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        Subject subject = SecurityUtils.getSubject();
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        Cache<Object, AuthenticationInfo> cache = myRealm.getAuthenticationCache();
        if (cache != null && user != null) {
            cache.remove(user.getUserName());
        }

        subject.logout();
        System.out.println("logout");
        //WebUtils.issueRedirect(req, resp, "/login");
        return "redirect:login";
    }

    /**
     * 验证码
     * @param request
     * @param response
     * @throws IOException
     */
    @RequestMapping(value = "/captcha", method = RequestMethod.GET)
    public void captcha(HttpServletRequest request,
                        HttpServletResponse response) throws IOException {

        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        response.setContentType("image/jpeg");
        BufferedImage image = new BufferedImage
                (IMG_WIDTH, IMG_HEIGTH, BufferedImage.TYPE_INT_RGB);
        Graphics g = image.getGraphics();
        Random random = new Random();
        g.setColor(getRandColor(200, 250));
        g.fillRect(1, 1, IMG_WIDTH - 1, IMG_HEIGTH - 1);
        g.setColor(new Color(102, 102, 102));
        g.drawRect(0, 0, IMG_WIDTH - 1, IMG_HEIGTH - 1);
        g.setColor(getRandColor(160, 200));
        for (int i = 0; i < 30; i++) {
            int x = random.nextInt(IMG_WIDTH - 1);
            int y = random.nextInt(IMG_HEIGTH - 1);
            int xl = random.nextInt(6) + 1;
            int yl = random.nextInt(12) + 1;
            g.drawLine(x, y, x + xl, y + yl);
        }
        g.setColor(getRandColor(160, 200));
        for (int i = 0; i < 30; i++) {
            int x = random.nextInt(IMG_WIDTH - 1);
            int y = random.nextInt(IMG_HEIGTH - 1);
            int xl = random.nextInt(12) + 1;
            int yl = random.nextInt(6) + 1;
            g.drawLine(x, y, x - xl, y - yl);
        }
        g.setFont(mFont);
        String sRand = "";
        for (int i = 0; i < 4; i++) {
            String tmp = getRandomChar();
            //去除有歧义的字母数字
            if(Arrays.asList(new String[]{"0", "1", "i", "I", "l", "L", "o", "O"}).contains(tmp)) {
                tmp = "A";
            }

            sRand += tmp;
            g.setColor(new Color(20 + random.nextInt(110)
                    , 20 + random.nextInt(110)
                    , 20 + random.nextInt(110)));
            g.drawString(tmp, 15 * i + 10, 15);
        }
        HttpSession session = request.getSession(true);
        session.setAttribute(MyAuthenticationToken.captcha_key, sRand);
//			System.out.println("写入session"+sRand);
        g.dispose();
        ImageIO.write(image, "JPEG", response.getOutputStream());
    }

    private String getRandomChar() {
        int rand = (int) Math.round(Math.random() * 2);
        long itmp = 0;
        char ctmp = '\u0000';
        switch (rand) {
            case 1:
                itmp = Math.round(Math.random() * 25 + 65);
                ctmp = (char) itmp;
                return String.valueOf(ctmp);
            case 2:
                itmp = Math.round(Math.random() * 25 + 97);
                ctmp = (char) itmp;
                return String.valueOf(ctmp);
            default:
                itmp = Math.round(Math.random() * 9);
                return itmp + "";
        }
    }

    private final Font mFont =
            new Font("Arial Black", Font.PLAIN, 16);
    private final int IMG_WIDTH = 100;
    private final int IMG_HEIGTH = 18;

    private Color getRandColor(int fc, int bc) {
        Random random = new Random();
        if (fc > 255) {
            fc = 255;
        }
        if (bc > 255) {
            bc = 255;
        }
        int r = fc + random.nextInt(bc - fc);
        int g = fc + random.nextInt(bc - fc);
        int b = fc + random.nextInt(bc - fc);
        return new Color(r, g, b);
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
                logger.error("类:" + Thread.currentThread().getStackTrace()[1].getClassName() + " 函数:" + Thread.currentThread().getStackTrace()[1].getMethodName() + " 异常: {}", e);
            }
        }
    }

}
