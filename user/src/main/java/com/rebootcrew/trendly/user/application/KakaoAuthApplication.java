package com.rebootcrew.trendly.user.application;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.rebootcrew.trendly.common.domain.User;
import com.rebootcrew.trendly.common.respository.UserRepository;
import com.rebootcrew.trendly.user.domain.KakaoUserResponse;
import com.rebootcrew.trendly.user.dto.AuthResponse;
import com.rebootcrew.trendly.user.service.AuthService;
import com.rebootcrew.trendly.user.service.KakaoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class KakaoAuthApplication {

	private final AuthService authService;
	private final KakaoService kakaoService;
	private final UserRepository userRepository;

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

	/**
	 * 카카오 로그인 콜백 처리
	 * @param code
	 * @return AuthResponse (Jwt 토큰, 유저 정보)
	 * @throws JsonProcessingException
	 */
	public AuthResponse handleKakaoCallback(String code) throws JsonProcessingException {
		// 1. 토큰 발급
		String accessToken = kakaoService.getAccessToken(code).getAccessToken();

		// 2. 사용자 정보 조회
		KakaoUserResponse userInfo = kakaoService.getUserInfo(accessToken);

		// 3. DB에서 이메일 조회 (findByEmail 한 번만 실행!)
		Optional<User> existUser = userRepository.findByEmail(userInfo.getKakaoAccount().getEmail());

		// 4. 회원가입 / 로그인 분기 처리
		return existUser.map(user -> authService.loginUser(user)) // DB 조회 결과 그대로 전달
				.orElseGet(() -> authService.signUpUser(userInfo)); // 없는 경우 회원가입 진행
	}

//	/**
//	 * 클라이언트 토큰 만료 후 카카오 로그아웃 URL 생성
//	 * @param token 우리 시스템의 access token
//	 * @return 카카오 로그아웃 URL (리다이렉트 시 사용)
//	 */
//	public String getKakaoLogoutUrl(String token) {
//		// 내부 토큰 만료 처리
//		authService.invalidateTokens(token);
//
//		return "https://kauth.kakao.com/oauth/logout?"
//				+ "client_id=" + kakaoClientId
//				+ "&redirect_uri=" + logoutRedirectUri;
//	}

}
