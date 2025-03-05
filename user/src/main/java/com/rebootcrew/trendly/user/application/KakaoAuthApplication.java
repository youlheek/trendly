package com.rebootcrew.trendly.user.application;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.rebootcrew.trendly.user.domain.KakaoUserResponse;
import com.rebootcrew.trendly.user.dto.AuthResponse;
import com.rebootcrew.trendly.user.service.KakaoService;
import com.rebootcrew.trendly.user.service.KakaoExternalService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class KakaoAuthApplication {

	private final KakaoService kakaoService;
	private final KakaoExternalService kakaoExternalService;

	@Value("${spring.security.oauth2.client.registration.kakao.client-id}")
	private String kakaoClientId;
	@Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}")
	private String loginRedirectUri;
	@Value("${kakao.logout-redirect-uri}")
	private String logoutRedirectUri;

	// redirect url 전송 & redirect url 로 인가 코드 받기
	public String getKakaoLoginUrl() {
		return "https://kauth.kakao.com/oauth/authorize?" +
				"client_id=" + kakaoClientId +
				"&redirect_uri=" + loginRedirectUri +
				"&response_type=code&prompt=login";
	}

	// 전체 콜백 처리 로직
	public AuthResponse handleKakaoCallback(String code) throws JsonProcessingException {
		// 1. 토큰 발급
		String accessToken = kakaoExternalService.getAccessToken(code).getAccessToken();

		// 2. 사용자 정보 조회
		KakaoUserResponse userInfo = kakaoExternalService.getUserInfo(accessToken);

		// 3. 회원가입 / 로그인 분기 처리
		if (kakaoService.isEmailExist(userInfo.getKakaoAccount().getEmail())) {
			// 로그인처리
			return kakaoService.loginUser(userInfo);
		} else {
			// 회원가입
			return kakaoService.signUpUser(userInfo);
		}
	}

	/**
	 * 클라이언트 토큰 만료 후 카카오 로그아웃 URL 생성
	 * @param token 우리 시스템의 access token
	 * @return 카카오 로그아웃 URL (리다이렉트 시 사용)
	 */
	public String getKakaoLogoutUrl(String token) {
		// 내부 토큰 만료 처리
		kakaoService.invalidateTokens(token);

		return "https://kauth.kakao.com/oauth/logout?"
				+ "client_id=" + kakaoClientId
				+ "&redirect_uri=" + logoutRedirectUri;
	}

}
