package com.zyc.zdh.service;

import com.zyc.zdh.entity.ProductInfo;
import com.zyc.zdh.entity.TreeData;

import java.util.List;

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
