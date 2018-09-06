package com.zyc.zspringboot.util;

public class StringUtils {

	/**
	 * 如果text为空或者null则返回true
	 * @param text
	 * @return
	 */
	public static boolean isEmpty(String text){
		if(text!=null&&!text.trim().equals("")){
			return false;
		}
		return true;
	}
}
