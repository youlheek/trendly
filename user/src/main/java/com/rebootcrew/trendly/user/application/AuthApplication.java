package com.rebootcrew.trendly.user.application;

import com.rebootcrew.trendly.common.config.JwtTokenProvider;
import com.rebootcrew.trendly.common.exception.ErrorCode;
import com.rebootcrew.trendly.common.exception.JwtAuthenticationException;
import com.rebootcrew.trendly.user.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.data.redis.RedisSystemException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthApplication {
	// 회원가입, 로그인, 로그아웃

	private final AuthService authService;
	private final JwtTokenProvider jwtTokenProvider;

	// Jwt 기반 로그아웃 처리
	public void logout(String token) {
		if (authService.isInvalidatedToken(token)) {
			throw new JwtAuthenticationException(ErrorCode.INVALID_TOKEN);
		}

		long expiration = jwtTokenProvider.getExpiration(token);

		try {
			authService.invalidateTokens(token, expiration);
		} catch (RedisConnectionFailureException | RedisSystemException e) {
			log.error("❌ Redis 오류 - 블랙리스트 조회 실패: {}", e.getMessage());
			throw new JwtAuthenticationException(ErrorCode.REDIS_ERROR);
		}
	}
}
