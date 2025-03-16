//package com.rebootcrew.trendly.common.config;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.CorsRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
//@Configuration
//public class WebConfig implements WebMvcConfigurer {
//
//	@Value("${server.base-url}")
//	private String baseUrl;
//
//	@Override
//	public void addCorsMappings(CorsRegistry registry) {
//		registry.addMapping("/**")	// 모든 API에 대해
//				.allowedOrigins("http://" + baseUrl + ":3000")	// 허용할 주소
//				.allowedMethods("GET", "POST", "PUT", "DELETE","PATCH", "OPTIONS")  // 허용할 HTTP 메서드
//				.allowedHeaders("*");  // 모든 헤더 허용)
//	}
//}
