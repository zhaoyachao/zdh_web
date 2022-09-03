package com.zyc.zdh.entity;

import java.io.Serializable;

import javax.persistence.Transient;

/**
 * ClassName: Page   
 * @author zyc-admin
 * @date 2018年2月8日  
 * @Description: TODO  
 */
public class PageBase implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6764120441628433192L;

	/**
	 * 每页显示记录数
	 */
	@Transient
	private int pageSize; //每页显示记录数
	/**
	 * 总页数
	 */
	@Transient
	private int totalPage;		//总页数
	/**
	 * 总记录数
	 */
	@Transient
	private int totalResult;	//总记录数
	/**
	 * 当前页
	 */
	@Transient
	private int pageNum;	//当前页
	/**
	 * 当前记录起始索引
	 */
	@Transient
	private int currentResult;	//当前记录起始索引
	/**
	 * true:需要分页的地方，传入的参数就是Page实体；false:需要分页的地方，传入的参数所代表的实体拥有Page属性
	 */
	@Transient
	private boolean entityOrField;	//true:需要分页的地方，传入的参数就是Page实体；false:需要分页的地方，传入的参数所代表的实体拥有Page属性
	/**
	 *
	 */
	@Transient
	private String sortColumn;
	@Transient
	private String sortDir;
	@Transient
	private String sEcho;

	
	public PageBase(){
		try {
			this.pageSize = Integer.parseInt("2");
		} catch (Exception e) {
			this.pageSize = 15;
		}
	}
	
	public int getTotalPage() {
		if(totalResult%pageSize==0)
			totalPage = totalResult/pageSize;
		else
			totalPage = totalResult/pageSize+1;
		return totalPage;
	}
	
	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}
	
	public long getTotalResult() {
		return totalResult;
	}
	
	public void setTotalResult(int totalResult) {
		this.totalResult = totalResult;
	}
	
	public int getPageNum() {
		if(pageNum<=0)
			pageNum = 1;
		if(pageNum>getTotalPage())
			pageNum = getTotalPage();
		return pageNum;
	}
	/**
	 * 使用pagehelper插件分页时 获取当前页使用此方法
	 * @return
	 */
	public int getPageNum2() {
		
		return pageNum;
	}
	
	public void setPageNum(int pageNum) {
		this.pageNum = pageNum;
	}
	
	public int getPageSize() {
		return pageSize;
	}
	
	public void setPageSize(int pageSize) {
		
		this.pageSize = pageSize;
	}
	
	public long getCurrentResult() {
		currentResult = (getPageNum()-1)*getPageSize();
		if(currentResult<0)
			currentResult = 0;
		return currentResult;
	}
	
	public void setCurrentResult(int currentResult) {
		this.currentResult = currentResult;
	}
	
	public boolean isEntityOrField() {
		return entityOrField;
	}
	
	public void setEntityOrField(boolean entityOrField) {
		this.entityOrField = entityOrField;
	}
	
	public String getSortColumn() {
		return sortColumn;
	}

	public void setSortColumn(String sortColumn) {
		this.sortColumn = sortColumn;
	}

	public String getSortDir() {
		return sortDir;
	}

	public void setSortDir(String sortDir) {
		this.sortDir = sortDir;
	}

	public String getsEcho() {
		return sEcho;
	}

	public void setsEcho(String sEcho) {
		this.sEcho = sEcho;
	}

	
}
