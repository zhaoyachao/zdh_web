package com.zyc.zspringboot.entity;

import java.util.ArrayList;
import java.util.List;
@SuppressWarnings("rawtypes")
public class DataGrid {

	
	private int total = 0;
	
	
	
	private List rows = new ArrayList();
	
	
	public List getRows() {
		return rows;
	}

	public void setRows(List rows) {
		this.rows = rows;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}
}