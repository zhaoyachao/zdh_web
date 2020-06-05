package com.zyc.zdh.service;

import java.util.List;

import com.zyc.zdh.entity.ProductInfo;
import com.zyc.zdh.entity.TreeData;

/**
 * ClassName: ProductService   
 * @author zyc-admin
 * @date 2018年2月28日  
 * @Description: TODO  
 */
public interface ProductService {

	public List<ProductInfo> findList(ProductInfo productInfo);
	
	public List<TreeData> findByPid(String pid);
	
	public String tableData(String aoData, String id);
}
