package com.rebootcrew.trendly.common.config;

import com.rebootcrew.trendly.common.config.filter.CustomFilter;
import com.rebootcrew.trendly.common.config.filter.JwtAuthenticationEntryPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.messaging.MessageSecurityMetadataSourceRegistry;
import org.springframework.security.config.annotation.web.socket.AbstractSecurityWebSocketMessageBrokerConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.messaging.access.intercept.ChannelSecurityInterceptor;
import org.springframework.security.messaging.access.intercept.MessageSecurityMetadataSource;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfig {

	private final JwtTokenProvider jwtTokenProvider;
	private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

	// HTTP 보안 설정
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.csrf(csrf -> csrf.disable())
				// csrf 비활성화 (API 호출용), Jwt 사용시 필요 없음
				// CSRF 보호 : 세션 기반 인증에서는 필요하지만 JWT는 상태를 저장하지 않는 무상태 방식이므로 불필요
				.cors(cors -> cors.configurationSource(corsConfigurationSource())) // CORS 설정 적용
				.exceptionHandling(exception ->
						exception.authenticationEntryPoint(jwtAuthenticationEntryPoint)) // 인증 실패 처리
				.authorizeHttpRequests(
						auth -> auth.requestMatchers(
										"/auth/**",
										"/api/auth/**", // refresh token사용하여 토큰 재발급
										"/swagger-ui/**",
										"/swagger-ui/index.html",
										"/swagger-resources/**",
										"/v3/api-docs/**",
										"/hello/**",
										"/api/chat/rooms",
										"/ws/**" // Websocket 엔드포인트 인증 제외
								).permitAll()
								.anyRequest().authenticated() // 그 외 요청은 인증 필요
				)
				// 커스텀 필터를 UsernamePasswordAuthenticationFilter 앞에 추가
				.addFilterBefore(new CustomFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class)
				.sessionManagement(session -> session.sessionCreationPolicy((SessionCreationPolicy.STATELESS)));
		// 세션 사용 안 함
		return http.build();

	}

	// CORS 설정 추가 (SecurityConfig에서 직접 관리)
	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedOrigins(List.of("http://localhost:3000", "http://54.180.63.180:3000", "http://localhost:8080", "http://localhost:63342", "http://54.180.63.180:80", "http://54.180.63.180", "https://jiangxy.github.io")); // 허용할 Origin
		configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
		configuration.setAllowedHeaders(List.of("*")); // 모든 헤더 허용
		configuration.setAllowCredentials(true); // 쿠키 포함 허용

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration); // 모든 경로에 적용
		return source;
	}


}
