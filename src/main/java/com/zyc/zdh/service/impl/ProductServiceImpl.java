package com.zyc.zdh.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zyc.zdh.annotation.SortMark;
import com.zyc.zdh.dao.ProductDao;
import com.zyc.zdh.entity.PageBase;
import com.zyc.zdh.entity.ProductInfo;
import com.zyc.zdh.entity.TreeData;
import com.zyc.zdh.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * ClassName: ProductServiceImpl
 * 
 * @author zyc-admin
 * @date 2018年2月28日
 * @Description: TODO
 */
@Service("productService")
public class ProductServiceImpl implements ProductService {

	@Autowired
	private ProductDao productDao;

	@Override
	public List<TreeData> findByPid(String pid) {
		List<TreeData> l = new ArrayList<>();
		for (int i = 0; i < 10; i++) {
			TreeData t = new TreeData();
			t.setId(i);
			if (i != 0) {
				t.setpId(0);
			}
			t.setName("z" + i);
			l.add(t);
		}
		return l;
	}
	
	@Override
	public String tableData(String aoData, String id) {
		JSONArray jsonarray = JSONArray.parseArray(aoData);
		ProductInfo prod = new ProductInfo();
		resolve(aoData, prod);
		prod.setTypeId(id);
		List<ProductInfo> findList = findList(prod);
		JSONObject getObj = new JSONObject();
		
		getObj.put("sEcho", prod.getsEcho());
		getObj.put("totalNum", prod.getTotalResult());
		getObj.put("iTotalRecords", prod.getTotalResult()); // 实际的行数
		getObj.put("iTotalDisplayRecords", prod.getTotalResult()); // 显示的行数,这个要和上面写的一样
		System.out.println(JSONArray.toJSONString(findList));
		getObj.put("aaData", findList);// 要以JSON格式返回，否则前台没法显示
		return getObj.toString();
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

	@Override
	public List<ProductInfo> findList(ProductInfo productInfo) {
		Page startPage = PageHelper.startPage(productInfo.getPageNum2(),
				productInfo.getPageSize());
		productDao.findList(productInfo);
		productInfo.setTotalResult((int) startPage.getTotal());
		return startPage.getResult();
	}
	
	private <T extends PageBase> T resolve(String aoData,T t){
		JSONArray jsonarray = JSONArray.parseArray(aoData);
	
		String sEcho = null; // 记录操作的次数
		int iDisplayStart = 0; // 起始索引
		int iDisplayLength = 0; // 每页显示的行数
		// 这里获取从前台传递过来的参数，从而确保是否分页、是否进行查询、是否排序等
		for (int i = 0; i < jsonarray.size(); i++) {
			JSONObject obj = (JSONObject) jsonarray.get(i);
			if (obj.get("name").equals("sEcho")) {
                sEcho = obj.get("value").toString();
            }

			if (obj.get("name").equals("iDisplayStart")) {
                iDisplayStart = obj.getIntValue("value");
            }

			if (obj.get("name").equals("iDisplayLength")) {
                iDisplayLength = obj.getIntValue("value");
            }

			if (obj.get("name").equals("iSortCol_0")) {
                t.setSortColumn(getSortColumn(ProductInfo.class,
                        obj.getIntValue("value")));
            }

			if (obj.get("name").equals("sSortDir_0")) {
                t.setSortDir(obj.get("value").toString());
            }
		}
		System.out.printf("操作了[ %s ]次\n",sEcho);
		int n = (iDisplayStart + 1) / iDisplayLength;
		t.setPageNum(n > 0 ? n + 1 : 1);
		t.setPageSize(iDisplayLength);
		t.setsEcho(sEcho);
		return t;
	}
}
