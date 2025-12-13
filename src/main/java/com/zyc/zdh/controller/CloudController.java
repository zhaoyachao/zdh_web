package com.zyc.zdh.controller;


import com.zyc.zdh.dao.ProjectMapper;
import com.zyc.zdh.service.ZdhPermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 云控制台服务
 */
@Controller
public class CloudController extends BaseController {

    @Autowired
    private ProjectMapper projectMapper;
    @Autowired
    private ZdhPermissionService zdhPermissionService;

    /**
     * 项目信息列表首页
     * @return
     */
    @RequestMapping(value = "/cloud_console", method = RequestMethod.GET)
    public String cloud_console() {

        return "cloud_console";
    }
}
