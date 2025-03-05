package com.rebootcrew.trendly.user.dto;

import com.rebootcrew.trendly.user.domain.KakaoUserResponse;
import lombok.Builder;
import lombok.Data;

@Data
@Builder // 확장 가능성을 고려해 Builder 패턴으로 설정
public class AuthResponse {
	private String accessToken;
	private String refreshToken;
	private KakaoUserResponse user;
}
