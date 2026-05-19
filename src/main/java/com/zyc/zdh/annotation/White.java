package com.zyc.zdh.annotation;

import java.lang.annotation.*;

/**
 *
 * 白名单注解,在请求接口层方法增加此注解,可跳过权限验证
 * ClassName: White
 * @author zyc-admin
 * @date 2022年1月18日
 * @Description: 自定义注解,标识作用  
 */
@Target({ElementType.TYPE,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface White {

	boolean value() default true;
}
