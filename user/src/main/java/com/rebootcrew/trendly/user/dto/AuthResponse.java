package com.rebootcrew.trendly.user.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder // 확장 가능성을 고려해 Builder 패턴으로 설정
public class AuthResponse {
	private String accessToken;
	private String refreshToken;

	private Long accessTokenExpiresIn;  // ✅ 액세스 토큰 만료 시간 추가
	private Long refreshTokenExpiresIn; // ✅ 리프레시 토큰 만료 시간 추가

	private String tokenType; // "Bearer"

	private UserDto user;
}
