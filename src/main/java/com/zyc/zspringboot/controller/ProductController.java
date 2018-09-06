package com.zyc.zspringboot.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.zyc.zspringboot.entity.TreeData;
import com.zyc.zspringboot.service.ProductService;

/**
 * ClassName: ProductController   
 * @author zyc-admin
 * @date 2018年2月28日  
 * @Description: TODO  
 */
@Controller
@RequestMapping("product")
public class ProductController {

	@Autowired
	private ProductService productService;
	
	@RequestMapping("list")
	public String list(){
		return "product/product-list";
	}
	
	@RequestMapping("treeData")
	@ResponseBody
	public JSONArray treeData(String id){
		if(id==null||id.equals("")){
			id="0";
		}
		List<TreeData> findByPid = productService.findByPid(id);
		JSONArray parseArray = JSONArray.parseArray(JSON.toJSONString(findByPid));
		return parseArray;
	}
	
	@RequestMapping(value="tableData",produces="text/html;charset=UTF-8")
	@ResponseBody
	public String tableData(@RequestParam String aoData,String id){
		return productService.tableData(aoData,id);
	}
}
