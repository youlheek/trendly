package com.rebootcrew.trendly.user.service;

import com.rebootcrew.trendly.common.config.JwtTokenProvider;
import com.rebootcrew.trendly.common.domain.User;
import com.rebootcrew.trendly.common.respository.UserRepository;
import com.rebootcrew.trendly.user.domain.KakaoUserResponse;
import com.rebootcrew.trendly.user.domain.SignUpForm;
import com.rebootcrew.trendly.user.dto.AuthResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KakaoService {

	private final UserRepository userRepository;
	private final JwtTokenProvider jwtTokenProvider;

	// 4. 회원 정보(email) 조회
	public boolean isEmailExist(String email) {
		return userRepository.findByEmail(email).isPresent();
	}

	public AuthResponse loginUser(KakaoUserResponse userInfo) {
		// TODO : 로그인 로직 구현 (토큰 발급)
		String accessToken = jwtTokenProvider.generateAccessToken(userInfo.getKakaoAccount().getEmail());
		String refreshToken = jwtTokenProvider.generateRefreshToken(userInfo.getKakaoAccount().getEmail());

		return AuthResponse.builder()
				.accessToken(accessToken)
				.refreshToken(refreshToken)
				.user(userInfo)
				.build();
	}

	// 5. 회원가입 처리
	public AuthResponse signUpUser(KakaoUserResponse userInfo){
		// TODO : 에러나면 rollback -> DB 저장 X
		SignUpForm form = SignUpForm.from(userInfo);
		User user = userRepository.save(form.toUser());
		System.out.println("회원가입이 완료되었습니다.");

		// access token 발급 & refreshToken
		String accessToken = jwtTokenProvider.generateAccessToken(userInfo.getKakaoAccount().getEmail());
		String refreshToken = jwtTokenProvider.generateRefreshToken(userInfo.getKakaoAccount().getEmail());

		return AuthResponse.builder()
				.accessToken(accessToken)
				.refreshToken(refreshToken)
				.user(userInfo)
				.build();
	}

	/**
	 * 내부 토큰 무효화 처리
	 * 실제 구현에서는 데이터베이스, 캐시(Redis) 또는 블랙리스트 등록 등을 수행
	 * @param token 클라이언트가 보유한 우리 시스템의 토큰
	 */
	public void invalidateTokens(String token) {
		// 예시: 토큰 만료 또는 삭제 처리 로직
		System.out.println("내부 토큰 만료 처리: " + token);
		// TODO: 실제 토큰 저장소에서 해당 토큰을 만료시키는 로직 구현
	}
}
