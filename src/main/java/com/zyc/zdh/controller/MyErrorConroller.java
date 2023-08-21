package com.zyc.zdh.controller;

import java.util.Arrays;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.zyc.zdh.annotation.White;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 异常页面服务
 * @author zyc-admin
 * @date 2018年3月2日  
 * @Description: TODO  
 */
@Controller
public class MyErrorConroller {

	/**
	 * 404页面
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/404")
	@White
	public String  error(HttpServletRequest request){
		Map<String, String[]> parameterMap = request.getParameterMap();
		
		for(Map.Entry<String, String[]> m:parameterMap.entrySet()){
			System.out.println(m.getKey()+"==="+Arrays.toString(m.getValue()));
		}
		return "404";
	}

	/**
	 * 403页面
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/403")
	@White
	public String  permission(HttpServletRequest request){
		Map<String, String[]> parameterMap = request.getParameterMap();

		return "403";
	}

	/**
	 * 503页面
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/503")
	@White
	public String  manager(HttpServletRequest request){
		return "503";
	}
}
