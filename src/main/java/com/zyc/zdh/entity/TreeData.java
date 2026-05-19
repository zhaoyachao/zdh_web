package com.zyc.zdh.entity;

import java.io.Serializable;

/**
 * ClassName: TreeData   
 * @author zyc-admin
 * @date 2018年2月28日  
 * @Description: TODO  
 */
public class TreeData implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5654214458271013881L;

	    private int id;
	    private int pId;
	    private String name;  
	    private int isParent;  
	  
	    public int getId() {  
	        return id;  
	    }  
	  
	    public void setId(int id) {  
	        this.id = id;  
	    }  
	  
	    public int getpId() {  
	        return pId;  
	    }  
	  
	    public void setpId(int pId) {  
	        this.pId = pId;  
	    }  
	  
	    public String getName() {  
	        return name;  
	    }  
	  
	    public void setName(String name) {  
	        this.name = name;  
	    }  
	  
	    public int getIsParent() {  
	        return isParent;  
	    }  
	  
	    public void setIsParent(int isParent) {  
	        this.isParent = isParent;  
	    }  

}
