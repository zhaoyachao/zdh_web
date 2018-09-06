package com.zyc.zspringboot;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;

public class SpringBootInit extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(
			SpringApplicationBuilder builder) {
		builder.sources(ZspringbootApplication.class);
		return super.configure(builder);
	}
}
