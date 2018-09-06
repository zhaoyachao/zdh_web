package com.zyc.zspringboot;


import com.zyc.zspringboot.annotation.MyMark;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.FilterType;
import java.util.Calendar;
import java.util.Date;


@ComponentScan(basePackages={"com.zyc.zspringboot"},includeFilters={@Filter(type= FilterType.ANNOTATION,value=MyMark.class)})
@MapperScan(basePackages={"com.zyc.zspringboot.dao"})
@SpringBootApplication
@EnableAspectJAutoProxy(exposeProxy=true,proxyTargetClass=true)
//@EnableAspectJAutoProxy exposeProxy:暴露代理对象,proxyTargetClass强制使用cglib代理
public class ZspringbootApplication {

	public static void main(String[] args) {
		SpringApplication springApplication = new SpringApplication(ZspringbootApplication.class);
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		int week=cal.get(Calendar.DAY_OF_WEEK)-1;//从星期日开始
		if(week<=5){
			springApplication.setAdditionalProfiles("pro");
		}else{
			springApplication.setAdditionalProfiles("dev");
		}
		System.out.println("ApplicationRun......run....");
		springApplication.run(args);
	}
}