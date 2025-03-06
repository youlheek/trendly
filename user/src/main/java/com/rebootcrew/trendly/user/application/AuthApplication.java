package com.rebootcrew.trendly.user.application;

import com.rebootcrew.trendly.common.config.JwtTokenProvider;
import com.rebootcrew.trendly.common.exception.ErrorCode;
import com.rebootcrew.trendly.common.exception.UnauthorizedException;
import com.rebootcrew.trendly.user.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
			throw new UnauthorizedException(ErrorCode.UNAUTHORIZED);
		}

		long expiration = jwtTokenProvider.getExpiration(token);
		// TODO : Redis 연결 에러처리 필요?
		authService.invalidateTokens(token, expiration);
	}
}
