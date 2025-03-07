package com.rebootcrew.trendly.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**")	// 모든 API에 대해
				.allowedOrigins("http://localhost:3000", "http://54.180.63.180:80")	// 허용할 주소
				.allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")  // 허용할 HTTP 메서드
				.allowedHeaders("*");  // 모든 헤더 허용)
	}
}
