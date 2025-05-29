package com.rebootcrew.trendly.user.controller;

import com.rebootcrew.trendly.common.config.JwtTokenProvider;
import com.rebootcrew.trendly.common.exception.ErrorCode;
import com.rebootcrew.trendly.user.domain.AuthResponse;
import com.rebootcrew.trendly.user.domain.RefreshTokenRequest;
import com.rebootcrew.trendly.user.service.UserService;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
	// JWT 토큰 관리 컨트롤러

	private final JwtTokenProvider jwtTokenProvider;
	private final UserService userService;


	@PostMapping("/refresh")
	public ResponseEntity<?> refreshToken(@RequestBody RefreshTokenRequest request) {
		// refresh token 검증 및 새로운 access token 발급 로직 구현
		// 예: DB 또는 캐시에서 해당 refresh token 정보를 확인한 후, 유효하면 새로운 access token 생성

		// TODO : refresh token 만료되었을 때
		String refreshToken = request.getRefreshToken();

		try {
			// refreshToken 검증
			if (refreshToken != null && jwtTokenProvider.validateToken(refreshToken, "refresh")) {
				Claims claims = jwtTokenProvider.getClaims(refreshToken);
				long userId = Long.parseLong(claims.getSubject());

				// TODO : 기존 accessToken 블랙리스트 처리 -> 기존 accessToken 을 어디서 받아올 것이냐?
				String newAccessToken = jwtTokenProvider.generateAccessToken(userId);
				Long accessTokenExpiresIn = jwtTokenProvider.getExpiration(newAccessToken);

				return ResponseEntity.ok(
						AuthResponse.builder()
								.accessToken(newAccessToken)
								.refreshToken(refreshToken)
								.accessTokenExpiresIn(accessTokenExpiresIn)
								.refreshTokenExpiresIn(jwtTokenProvider.getExpiration(refreshToken) - new Date().getTime())
								.tokenType("Bearer")
								.user(userService.getUserInfo(userId))
								.build()
				);
			}
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorCode.INTERNAL_SERVER_ERROR);
		}
		return null;
	}
}
