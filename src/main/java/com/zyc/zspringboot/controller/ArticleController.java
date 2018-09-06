package com.zyc.zspringboot.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/article")
public class ArticleController {

	@RequestMapping("/index")
	public String getArticleIndex(){
		
		return "article/article-list";
	}
}
