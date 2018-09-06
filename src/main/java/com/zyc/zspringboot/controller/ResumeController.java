package com.zyc.zspringboot.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * ClassName: ResumeController   
 * @author zyc-admin
 * @date 2018年3月2日  
 * @Description: TODO  
 */
@Controller
@RequestMapping("resume")
public class ResumeController {

	@RequestMapping("myresume")
	public String resume(){
		return "resume/my-resume";
	}
}
