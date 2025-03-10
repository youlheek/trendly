package com.rebootcrew.trendly.common.config;

import com.rebootcrew.trendly.common.config.filter.CustomFilter;
import com.rebootcrew.trendly.common.config.filter.JwtAuthenticationEntryPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfig {

	private final JwtTokenProvider jwtTokenProvider;
	private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;


	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.csrf(csrf -> csrf.disable())
				// csrf 비활성화 (API 호출용), Jwt 사용시 필요 없음
				// CSRF 보호 : 세션 기반 인증에서는 필요하지만 JWT는 상태를 저장하지 않는 무상태 방식이므로 불필요
				.cors(cors -> {}) // 기본 CORS 설정
				.exceptionHandling(exception ->
						exception.authenticationEntryPoint(jwtAuthenticationEntryPoint)) // 인증 실패 처리
						// 커스텀 예외 처리 추가: 접근 거부 발생 시 CustomAccessDeniedHandler 호출
				.authorizeHttpRequests(
						auth -> auth.requestMatchers(
										"/auth/**",
										"/api/auth/**",
										"/swagger-ui/**",
										"/swagger-ui/index.html",
										"/swagger-resources/**",
										"/v3/api-docs/**",
										"/hello/**"
								).permitAll()
								.anyRequest().authenticated() // 그 외 요청은 인증 필요
				)
				// 커스텀 필터를 UsernamePasswordAuthenticationFilter 앞에 추가
				.addFilterBefore(new CustomFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class)
				.sessionManagement(session -> session.sessionCreationPolicy((SessionCreationPolicy.STATELESS)));
		// 세션 사용 안 함
		return http.build();

	}
}
