package com.zyc.zdh;


import com.zyc.zdh.annotation.MyMark;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.FilterType;


@ComponentScan(basePackages={"com.zyc.zdh"},includeFilters={@Filter(type= FilterType.ANNOTATION,value=MyMark.class)})
@MapperScan(basePackages={"com.zyc.zdh.dao"})
@SpringBootApplication
@EnableAspectJAutoProxy(exposeProxy=true,proxyTargetClass=true)
//@EnableAspectJAutoProxy exposeProxy:暴露代理对象,proxyTargetClass强制使用cglib代理
public class ZdhApplication {

	public static void main(String[] args) {
		SpringApplication springApplication = new SpringApplication(ZdhApplication.class);
		System.out.println("ApplicationRun......run....");
		springApplication.run(args);
	}
}