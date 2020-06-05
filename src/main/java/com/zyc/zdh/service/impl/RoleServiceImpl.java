package com.zyc.zdh.service.impl;

import java.lang.reflect.Field;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zyc.zdh.annotation.SortMark;
import com.zyc.zdh.dao.RoleDao;
import com.zyc.zdh.entity.PageBase;
import com.zyc.zdh.entity.Role;
import com.zyc.zdh.service.RoleService;

@Service("roleService")
public class RoleServiceImpl implements RoleService {

	@Autowired
	private RoleDao roleDao;

	@Override
	@CachePut(cacheManager = "cacheManager", value = "myRedis", key = "'role:id:'+#id")
	//@Log(value = "获取数据并存入缓存")
	public Role getRole(String id) {
		// TODO Auto-generated method stub
		return roleDao.getRole(id);
	}

	@Override
	public List<Role> findList(PageBase page) {
		System.out.println("page.getPageNum2()====" + page.getPageNum2()
				+ "=======" + page.getPageSize());
		Page startPage = PageHelper.startPage(page.getPageNum2(),
				page.getPageSize());
		List<Role> findList = roleDao.findList(page);
		page.setTotalResult((int) startPage.getTotal());
		System.out.println("startPage.getTotal()====" + startPage.getTotal());
		return findList;
	}

	@Override
	public int delRole(String id) {
		// TODO Auto-generated method stub
		return roleDao.delRole(id);
	}

	@Override
	public String list(String aoData) {
		JSONArray jsonarray = JSONArray.parseArray(aoData);
		PageBase page = new PageBase();
		resolve(aoData, page);

		List<Role> findList = findList(page);
		JSONObject getObj = new JSONObject();

		getObj.put("sEcho", page.getsEcho());
		getObj.put("totalNum", page.getTotalResult());
		getObj.put("iTotalRecords", page.getTotalResult()); // 实际的行数
		getObj.put("iTotalDisplayRecords", page.getTotalResult()); // 显示的行数,这个要和上面写的一样
		System.out.println(JSONArray.toJSONString(findList));
		getObj.put("aaData", findList);// 要以JSON格式返回，否则前台没法显示
		return getObj.toString();
	}

	private <T extends PageBase> T resolve(String aoData, T t) {
		JSONArray jsonarray = JSONArray.parseArray(aoData);

		String sEcho = null; // 记录操作的次数
		int iDisplayStart = 0; // 起始索引
		int iDisplayLength = 0; // 每页显示的行数
		// 这里获取从前台传递过来的参数，从而确保是否分页、是否进行查询、是否排序等
		for (int i = 0; i < jsonarray.size(); i++) {
			JSONObject obj = (JSONObject) jsonarray.get(i);
			if (obj.get("name").equals("sEcho"))
				sEcho = obj.get("value").toString();

			if (obj.get("name").equals("iDisplayStart"))
				iDisplayStart = obj.getIntValue("value");

			if (obj.get("name").equals("iDisplayLength"))
				iDisplayLength = obj.getIntValue("value");

			if (obj.get("name").equals("iSortCol_0"))
				t.setSortColumn(getSortColumn(Role.class,
						obj.getIntValue("value")));

			if (obj.get("name").equals("sSortDir_0"))
				t.setSortDir(obj.get("value").toString());
		}
		System.out.printf("操作了[ %s ]次\n", sEcho);
		int n = (iDisplayStart + 1) / iDisplayLength;
		t.setPageNum(n > 0 ? n + 1 : 1);
		t.setPageSize(iDisplayLength);
		t.setsEcho(sEcho);
		return t;
	}

	private String getSortColumn(Class c, int column) {
		Field[] declaredFields = c.getDeclaredFields();
		for (Field f : declaredFields) {
			if (f.isAnnotationPresent(SortMark.class)) {
				int sort = f.getAnnotation(SortMark.class).value();
				if (sort == column) {
					return f.getAnnotation(SortMark.class).column();
				}
			}
		}
		return "id";
	}

}
