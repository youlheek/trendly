package com.rebootcrew.trendly.user.application;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.rebootcrew.trendly.common.config.JwtTokenProvider;
import com.rebootcrew.trendly.user.domain.UserDto;
import com.rebootcrew.trendly.common.domain.UserForm;
import com.rebootcrew.trendly.user.service.AuthService;
import com.rebootcrew.trendly.user.service.KakaoService;
import com.rebootcrew.trendly.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserApplication {

	private final UserService userService;
	private final JwtTokenProvider jwtTokenProvider;
	private final AuthService authService;
	private final KakaoService kakaoService;


	/**
	 * 사용자 정보 조회
	 * @param userId
	 * @return UserDto
	 */
	public UserDto getMyInfo(Long userId) {
		return userService.getUserInfo(userId);
	}

	/**
	 * 사용자 정보 수정
	 * @param userId
	 * @param form
	 * @return UserDto
	 */
	public UserDto updateUser(Long userId, UserForm form) {
		return userService.updateUser(userId, form);
	}

	/**
	 * 회원 탈퇴
	 * @param userId
	 * @return
	 */
	@Transactional
	public String deleteUser(Long userId, String token) {

		// 카카오 연결 끊기
		UserDto userInfo = userService.getUserInfo(userId);
		kakaoService.kakaoUnlinck(userInfo.getKakaoUserId());

		// Redis 블랙리스트 처리
		long expiration = jwtTokenProvider.getExpiration(token);
		authService.invalidateTokens(token, expiration);

		userService.deleteUser(userId);
		return "회원 탈퇴가 완료되었습니다.";
	}

}
