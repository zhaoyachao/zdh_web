package com.zyc.zspringboot.other;

import com.zyc.zspringboot.controller.PictureController;

/**
 * ClassName: PirctureTest   
 * @author zyc-admin
 * @date 2018年2月27日  
 * @Description: TODO  
 */
public class PirctureTest {

	public static void main(String[] args) {
		PictureController p=new PictureController();
		p.regex("z赵亚超abc");
		p.regex("123");
		p.regex("abcdef ");

	}

}
