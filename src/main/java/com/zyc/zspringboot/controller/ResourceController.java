package com.zyc.zspringboot.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.zyc.zspringboot.entity.DataGrid;
import com.zyc.zspringboot.entity.SysResource;

@RequestMapping("/resource")
@Controller
public class ResourceController {

	@RequestMapping("/list.do")
	@ResponseBody
	public Object list(HttpServletRequest request){
		System.out.println(request.getParameter("limit"));
		System.out.println(request.getParameter("offset"));
		System.out.println("in list");
		List<SysResource> list=new ArrayList<>();
		SysResource sysr;
		for(int i=0;i<10;i++){
			sysr=new SysResource();
			sysr.setCreateTime(new Date());
			sysr.setCreateUserId(i);
			sysr.setId(i);
			sysr.setIsEnable(1);
			sysr.setResourceName("资源"+i);
			sysr.setResourcePath("路径"+i);
			sysr.setOrderNum(i);
			sysr.setResourceDesc("资源说明"+i);
			list.add(sysr);
		}
		DataGrid dataGrid=new DataGrid();
		dataGrid.setTotal(100);
		dataGrid.setRows(list);
		return dataGrid;
	}
}
