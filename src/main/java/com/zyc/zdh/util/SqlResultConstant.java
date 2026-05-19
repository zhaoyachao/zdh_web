package com.zyc.zdh.util;

import java.lang.reflect.Field;

public class SqlResultConstant {

	public final static String SQL="SQL_";
	public final static String CONSTANT="_CONSTANT";
	public final static String SQL_ROLE_CONSTANT="id,role_name";
	
	public static String getAttr(String tableName) throws IllegalArgumentException, IllegalAccessException{
		tableName=SQL+tableName+CONSTANT;
		Field[] fields = SqlResultConstant.class.getDeclaredFields();  
		for(Field field:fields){
			System.out.println(tableName.toUpperCase());
			System.out.println("fieldName==="+field.getName());
			if(field.getName().equals(tableName.toUpperCase())){
				return field.get(new SqlResultConstant()).toString();
			}
		}
		return "";
	}
}
