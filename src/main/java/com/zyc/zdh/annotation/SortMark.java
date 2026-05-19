package com.zyc.zdh.annotation;

import java.lang.annotation.*;

/**
 * ClassName: SortMark
 *
 * @author zyc-admin
 * @date 2018年2月26日
 * @Description: 自定义注解，用来和datatables排序的下标相对应
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SortMark {

	int value();

	String column() default "";
}
