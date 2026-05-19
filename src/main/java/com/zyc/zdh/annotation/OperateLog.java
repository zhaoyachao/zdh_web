package com.zyc.zdh.annotation;

import java.lang.annotation.*;

/**
 * 操作日志注解
 * 用于函数中, 当前设置ignore=true时不记录日志
 */
@Target({ElementType.TYPE,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface OperateLog {

	public String value() default "";

	public boolean ignore() default false;
}
