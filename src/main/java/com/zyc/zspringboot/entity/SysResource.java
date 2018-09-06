package com.zyc.zspringboot.entity;

import java.io.Serializable;

public class SysResource implements Serializable{

	private static final long serialVersionUID = 1L;

 	/**
 	* 主键
 	*/
   	private Integer id;
 	/**
 	* 上级id
 	*/
   	private Integer parentId;
 	/**
 	* 资源名称
 	*/
   	private String resourceName;
 	/**
 	* 资源路径
 	*/
   	private String resourcePath;
 	/**
 	* 资源图标
 	*/
   	private String resourceIcon;
 	/**
 	* 层级（1-系统,2-模块, 3-菜单，4-按钮）
 	*/
   	private Integer level;
 	/**
 	* 资源描述
 	*/
   	private String resourceDesc;
 	/**
 	* 权限字符串
 	*/
   	private String permissionStr;
 	/**
 	* 排序
 	*/
   	private Integer orderNum;
 	/**
 	* 是否启用
 	*/
   	private Integer isEnable;
 	/**
 	* 创建人id
 	*/
   	private Integer createUserId;
 	/**
 	* 创建时间
 	*/
   	private java.util.Date createTime;
 	/**
 	* 修改人id
 	*/
   	private Integer modifyUserId;
 	/**
 	* 修改时间
 	*/
   	private java.util.Date modifyTime;


   	public void setId(Integer id){
   		this.id = id;
   	}

   	public Integer getId(){
		return id;
	}

   	public void setParentId(Integer parentId){
   		this.parentId = parentId;
   	}

   	public Integer getParentId(){
		return parentId;
	}

   	public void setResourceName(String resourceName){
   		this.resourceName = resourceName;
   	}

   	public String getResourceName(){
		return resourceName;
	}

   	public void setResourcePath(String resourcePath){
   		this.resourcePath = resourcePath;
   	}

   	public String getResourcePath(){
		return resourcePath;
	}

   	public void setResourceIcon(String resourceIcon){
   		this.resourceIcon = resourceIcon;
   	}

   	public String getResourceIcon(){
		return resourceIcon;
	}

   	public void setLevel(Integer level){
   		this.level = level;
   	}

   	public Integer getLevel(){
		return level;
	}

   	public void setResourceDesc(String resourceDesc){
   		this.resourceDesc = resourceDesc;
   	}

   	public String getResourceDesc(){
		return resourceDesc;
	}

   	public void setPermissionStr(String permissionStr){
   		this.permissionStr = permissionStr;
   	}

   	public String getPermissionStr(){
		return permissionStr;
	}

   	public void setOrderNum(Integer orderNum){
   		this.orderNum = orderNum;
   	}

   	public Integer getOrderNum(){
		return orderNum;
	}

   	public void setIsEnable(Integer isEnable){
   		this.isEnable = isEnable;
   	}

   	public Integer getIsEnable(){
		return isEnable;
	}

   	public void setCreateUserId(Integer createUserId){
   		this.createUserId = createUserId;
   	}

   	public Integer getCreateUserId(){
		return createUserId;
	}

   	public void setCreateTime(java.util.Date createTime){
   		this.createTime = createTime;
   	}

   	public java.util.Date getCreateTime(){
		return createTime;
	}

   	public void setModifyUserId(Integer modifyUserId){
   		this.modifyUserId = modifyUserId;
   	}

   	public Integer getModifyUserId(){
		return modifyUserId;
	}

   	public void setModifyTime(java.util.Date modifyTime){
   		this.modifyTime = modifyTime;
   	}

   	public java.util.Date getModifyTime(){
		return modifyTime;
	}
}