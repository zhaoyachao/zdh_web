package com.zyc.zdh.aop;

import java.lang.annotation.*;

/**
 * ClassName: Log   
 * @author zyc-admin
 * @date 2018年1月25日  
 * @Description: TODO  
 */
@Target({ElementType.TYPE,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Log {

	public String value();
}
