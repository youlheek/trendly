package com.rebootcrew.trendly.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JwtConfig {
// SignInApplication 에서 autowired을 사용하기 위해 Bean 주입을 해주는 클래스

	@Bean
	public JwtTokenProvider jwtAuthenticationProvider() {
		return new JwtTokenProvider();
	}
}