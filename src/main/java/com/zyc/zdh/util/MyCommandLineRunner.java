package com.zyc.zdh.util;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;


/**
 * ClassName: MyCommandLineRunner   
 * @author zyc-admin
 * @date 2018年3月8日  
 * @Description: spring boot 自己提供的启动项目自动运行的类  
 */
@Component
public class MyCommandLineRunner implements CommandLineRunner {

	@Override
	public void run(String... args) throws Exception {
		LogUtil.info(this.getClass(), "[{}]项目启动完成", "ZDH");
	}

}
