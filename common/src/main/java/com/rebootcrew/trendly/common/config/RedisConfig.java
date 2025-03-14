package com.rebootcrew.trendly.common.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@EnableRedisRepositories
public class RedisConfig {

	@Value("${spring.redis.host}")
	private String redisHost;

	@Value("${spring.redis.port}")
	private int redisPort;

	@Bean
	public RedisConnectionFactory redisConnectionFactory() {
		// Redis 연결 설정 명시
		RedisStandaloneConfiguration redisConfig = new RedisStandaloneConfiguration();
		redisConfig.setHostName(redisHost);
		redisConfig.setPort(redisPort);
		// 필요 시 비밀번호 설정: redisConfig.setPassword(RedisPassword.of("your-password"));

		// Lettuce 클라이언트 사용 (기본적으로 Spring Boot에서 권장)
		LettuceConnectionFactory connectionFactory = new LettuceConnectionFactory(redisConfig);
		// 연결 초기화
		connectionFactory.afterPropertiesSet();
		return connectionFactory;
	}

	@Bean
	public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
		// RedisTemplate 인스턴스 생성
		// <String, Object>: 키는 문자열, 값은 모든 객체 타입 지원
		RedisTemplate<String, Object> template = new RedisTemplate<>();

		// Redis 연결 팩토리 설정
		// 실제 Redis 서버와의 연결을 관리
		template.setConnectionFactory(connectionFactory);

		// JSON 직렬화를 위한 ObjectMapper 생성
		ObjectMapper mapper = new ObjectMapper();

		// Java 8 날짜/시간 API (LocalDateTime 등) 직렬화 지원
		// 날짜 타입을 JSON으로 변환할 때 필요
		mapper.registerModule(new JavaTimeModule());

		// 커스텀 직렬화 암호화 전략
		template.setKeySerializer(new StringRedisSerializer());
		template.setValueSerializer(new GenericJackson2JsonRedisSerializer());

		return template;
	}
}
