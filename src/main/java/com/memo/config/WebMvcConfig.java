package com.memo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.memo.common.FileManagerService;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer{
	
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/images/**")	//웹이미지주소 http://localhost/images/aaaa_23011718035458/sun.png
		.addResourceLocations("file:///"+FileManagerService.FILE_UPLOAD_PATH);	//실제파일위치
	}

}
