package com.zyc.zspringboot.controller;

import io.netty.handler.codec.http.HttpResponse;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zyc.zspringboot.entity.PageBase;
import com.zyc.zspringboot.entity.ResultInfo;
import com.zyc.zspringboot.entity.Role;
import com.zyc.zspringboot.service.RoleService;

@RequestMapping("system")
@Controller
public class SystemController {

	@Autowired
	private RoleService roleService;
	
	@RequestMapping("index")
	public String getSystemIndex(){
		return "system/system-log";
	}
	
	@RequestMapping("role")
	public String getSystemRole(PageBase page,ModelMap model){
		/*List<Role> findList = roleService.findList(page);
		model.addAttribute("roleList", findList);*/
		return "system/system-role";
	}
	
	@RequestMapping(value="list",produces="text/html;charset=UTF-8")
	@ResponseBody
	public String list(@RequestParam String aoData) throws UnsupportedEncodingException{
		return roleService.list(aoData);
	}
	@RequestMapping(value="addRole")
	public String addRole(String id){
		
		return "system-role-add";
	}
	@RequestMapping(value="editRole")
	public String editRole(String id,ModelMap model){
		Role role=roleService.getRole(id);
		model.addAttribute("role", role); 
		return "system/system-role-edit";
	}
	@RequestMapping(value="delRole")
	@ResponseBody
	public String delRole(String id){
		int result=roleService.delRole(id);
		JSONObject jsonObject=new JSONObject();
		jsonObject.put("result", result);
		return jsonObject.toString();
	}
	
	
}
