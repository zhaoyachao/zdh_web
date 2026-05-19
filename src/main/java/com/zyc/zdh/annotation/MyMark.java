package com.zyc.zdh.annotation;

import java.lang.annotation.*;

/**
 * ClassName: MyMark   
 * @author zyc-admin
 * @date 2018年2月6日  
 * @Description: 自定义注解,标识作用  
 */
@Target({ElementType.TYPE,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MyMark {

	String value();
}
