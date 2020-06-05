package com.zyc.zdh.dao;

import java.util.List;

import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import com.zyc.zdh.entity.ProductInfo;

/**
 * ClassName: ProductDao   
 * @author zyc-admin
 * @date 2018年2月28日  
 * @Description: TODO  
 */
public interface ProductDao {

	@Select("select * from product_info where type_id=#{typeId} order by ${sortColumn} ${sortDir}")
	@Results(
			{
				@Result(column="id",property="id"),
				@Result(column="picture_url",property="pictureUrl"),
				@Result(column="product_name",property="productName"),
				@Result(column="content",property="content"),
				@Result(column="price",property="price"),
				@Result(column="status",property="status"),
				@Result(column="type_id",property="typeId")
			}
			)
	public List<ProductInfo> findList(ProductInfo productInfo);
}
