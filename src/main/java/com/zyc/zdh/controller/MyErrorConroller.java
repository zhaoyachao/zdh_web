package com.zyc.zdh.controller;

import java.util.Arrays;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * ClassName: MyErrorConroller   
 * @author zyc-admin
 * @date 2018年3月2日  
 * @Description: TODO  
 */
@Controller
public class MyErrorConroller {

	@RequestMapping(value = "/404", produces = "text/html;charset=UTF-8")
	public String  error(HttpServletRequest request){
		Map<String, String[]> parameterMap = request.getParameterMap();
		
		for(Map.Entry<String, String[]> m:parameterMap.entrySet()){
			System.out.println(m.getKey()+"==="+Arrays.toString(m.getValue()));
		}
		return "404";
	}

	@RequestMapping(value="/403", produces = "text/html;charset=UTF-8")
	public String  permission(HttpServletRequest request){
		Map<String, String[]> parameterMap = request.getParameterMap();

		return "403";
	}
}
