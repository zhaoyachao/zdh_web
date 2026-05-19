//package com.zyc.zdh.service.impl;
//
//import com.github.pagehelper.Page;
//import com.github.pagehelper.PageHelper;
//import com.zyc.zdh.annotation.SortMark;
//import com.zyc.zdh.dao.RoleDao;
//import com.zyc.zdh.entity.PageBase;
//import com.zyc.zdh.entity.RoleInfo;
//import com.zyc.zdh.service.RoleService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.cache.annotation.CachePut;
//import org.springframework.stereotype.Service;
//
//import java.lang.reflect.Field;
//import java.util.List;
//
//@Service("roleService")
//public class RoleServiceImpl implements RoleService {
//
//	@Autowired
//	private RoleDao roleDao;
//
//	@Override
//	@CachePut(cacheManager = "cacheManager", value = "myRedis", key = "'role:id:'+#id")
//	//@Log(value = "获取数据并存入缓存")
//	public RoleInfo getRole(String id) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public List<RoleInfo> findList(PageBase page) {
//		System.out.println("page.getPageNum2()====" + page.getPageNum2()
//				+ "=======" + page.getPageSize());
//		Page startPage = PageHelper.startPage(page.getPageNum2(),
//				page.getPageSize());
//		List<RoleInfo> findList =null;
//		page.setTotalResult((int) startPage.getTotal());
//		System.out.println("startPage.getTotal()====" + startPage.getTotal());
//		return findList;
//	}
//
//	@Override
//	public int delRole(String id) {
//		// TODO Auto-generated method stub
//		return 0;
//	}
//
//	@Override
//	public String list(String aoData) {
//		return null;
//	}
//
//}
