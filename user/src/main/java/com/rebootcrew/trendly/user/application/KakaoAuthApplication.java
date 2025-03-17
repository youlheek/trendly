package com.rebootcrew.trendly.user.application;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.rebootcrew.trendly.common.config.JwtTokenProvider;
import com.rebootcrew.trendly.common.domain.User;
import com.rebootcrew.trendly.common.exception.CustomException;
import com.rebootcrew.trendly.common.exception.ErrorCode;
import com.rebootcrew.trendly.common.respository.UserRepository;
import com.rebootcrew.trendly.user.domain.KakaoUserResponse;
import com.rebootcrew.trendly.user.domain.AuthResponse;
import com.rebootcrew.trendly.user.service.AuthService;
import com.rebootcrew.trendly.user.service.KakaoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class KakaoAuthApplication {

	private final AuthService authService;
	private final KakaoService kakaoService;
	private final UserRepository userRepository;
	private final JwtTokenProvider jwtTokenProvider;

	@Value("${spring.security.oauth2.client.registration.kakao.client-id}")
	private String kakaoClientId;
	@Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}")
	private String loginRedirectUri;
//	@Value("${kakao.logout-redirect-uri}")
//	private String logoutRedirectUri;


	// redirect url 전송 & redirect url 로 인가 코드 받기
	public String getKakaoLoginUrl() {
		return "https://kauth.kakao.com/oauth/authorize?" +
				"client_id=" + kakaoClientId +
				"&redirect_uri=" + loginRedirectUri +
				"&response_type=code&prompt=login";
	}

	/**
	 * 카카오 로그인 콜백 처리
	 *
	 * @param code
	 * @return AuthResponse (Jwt 토큰, 유저 정보)
	 * @throws JsonProcessingException
	 */
	public AuthResponse handleKakaoCallback(String code, String frontRedirectUrl) throws JsonProcessingException {
		// 1. 토큰 발급
		String accessToken = kakaoService.getAccessToken(code, frontRedirectUrl).getAccessToken();

		// 2. 사용자 정보 조회
		KakaoUserResponse userInfo = kakaoService.getUserInfo(accessToken);
		kakaoService.getUserServiceTerms(accessToken, userInfo);

		// 3. DB에서 이메일 조회 (findByEmail 한 번만 실행!)
		// 삭제 이력이 없는 계정이 있는지 확인
		// 있으면 -> 로그인
		// 없으면 -> 탈퇴 처리된 계정이 있는지 확인
		// 있으면 -> 날짜 확인
		// 7일 이내일 경우 -> 에러 반환
		// 7일 이후일 경우 -> 회원가입
		// 없으면 -> 회원가입

		// 4. 회원가입 / 로그인 분기 처리
		String email = userInfo.getKakaoAccount().getEmail();
		// 4-1. 활성화된 계정 조회
		Optional<User> activeUser = userRepository.findByEmailAndDeletedAtIsNull(email);
		if (activeUser.isPresent()) {
			// 활성화된 계정이 존재하면 로그인 처리
			User user = activeUser.get();
			return authService.loginUser(user);
		}

		// 4-2. 삭제 처리된 계정이 있는지 조회
		Optional<User> deletedUser = userRepository.findFirstByEmailAndDeletedAtIsNotNullOrderByDeletedAtDesc(userInfo.getKakaoAccount().getEmail());
		if (deletedUser.isPresent()) {
			User user = deletedUser.get();

			// 탈퇴한 경우 -> 7일 내인지 확인
			LocalDateTime now = LocalDateTime.now();
			LocalDateTime deletedAtPlus7days = user.getDeletedAt().plusDays(7);

			if (now.isAfter(deletedAtPlus7days)) {
				// 회원가입 처리 (재가입)
				return authService.signUpUser(userInfo);
			} else {
				throw new CustomException(ErrorCode.USER_ALREADY_DELETED_EXPIRED);
			}
		}

		// 4-3. active한 계정도, 삭제 기록도 없는 경우 -> 신규 회원가입
		return authService.signUpUser(userInfo);
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
