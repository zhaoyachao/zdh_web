package com.zyc.zdh.entity;

import com.zyc.zdh.annotation.SortMark;

import java.io.Serializable;

/**
 * ClassName: ProductInfo   
 * @author zyc-admin
 * @date 2018年2月28日  
 * @Description: TODO  
 */
public class ProductInfo extends PageBase implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5228161258255410238L;

	@SortMark(0)
	private String id;
	@SortMark(1)
	private String pictureUrl;
	@SortMark(2)
	private String productName;
	@SortMark(3)
	private String content;
	@SortMark(4)
	private String price;
	@SortMark(5)
	private String status;
	
	private String typeId;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPictureUrl() {
		return pictureUrl;
	}

	public void setPictureUrl(String pictureUrl) {
		this.pictureUrl = pictureUrl;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getTypeId() {
		return typeId;
	}

	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}
	
	
}
