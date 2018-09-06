package com.zyc.zspringboot.util;

import java.util.Locale;
import java.util.ResourceBundle;

public class BeanNameUtils {

	private static ResourceBundle rbd = ResourceBundle.getBundle("beanname",Locale.getDefault());
	
	public static String getBeanNameByTable(String tableName){
		String str = rbd.getString(tableName);
		return str;
	}
}
