package com.rebootcrew.trendly.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rebootcrew.trendly.user.domain.KakaoTokenResponse;
import com.rebootcrew.trendly.user.domain.KakaoUserResponse;
import com.rebootcrew.trendly.user.application.KakaoAuthApplication;
import com.rebootcrew.trendly.user.dto.AuthResponse;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URI;

@RestController
@RequestMapping("/auth/kakao")
@RequiredArgsConstructor
public class KakaoAuthController {

	private final KakaoAuthApplication kakaoAuthApplication;
	private final ObjectMapper objectMapper;

	// ✅ 카카오 로그인 요청 → 카카오 로그인 URL 리턴
	@GetMapping("/login")
	public ResponseEntity<String> kakaoLogin() {

		return ResponseEntity.ok(kakaoAuthApplication.getKakaoLoginUrl());
	}

	// ✅ 카카오 콜백 처리 (인가 코드 -> 토큰, 사용자 정보 조회 -> 로그인/회원가입)
	@GetMapping("/callback")
	public ResponseEntity<AuthResponse> kakaoCallback(
			@RequestParam("code") String code) throws IOException {

		return ResponseEntity.ok(kakaoAuthApplication.handleKakaoCallback(code));
	}

	/**
	 * 카카오계정과 함께 로그아웃 API
	 * - 클라이언트는 헤더에 "Authorization: Bearer {우리 토큰}" 형식으로 요청
	 * - 내부 토큰 만료 후, 카카오 로그아웃 URL로 리다이렉트
	 */
	@GetMapping("/logout")
	public ResponseEntity<String> kakaoLogout(@RequestHeader("Authorization") String authHeader) {
		// "Bearer " 부분 제거하여 실제 토큰만 추출
		String token = authHeader.replace("Bearer ", "").trim();
		// accesstoken 으로 사용자 정보 조회 -> redis 에 저장된 사용자의 kakao token 꺼내기

		// 내부 토큰 만료 처리 &
		// 카카오 로그아웃 URL 로 리다이렉트
		String redirectUrl = kakaoAuthApplication.getKakaoLogoutUrl(token);

		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(URI.create(redirectUrl));

		return new ResponseEntity<>(headers, HttpStatus.FOUND); // 302 Redirect
	}

	/**
	 * 카카오 로그아웃 후 리다이렉트 될 엔드포인트 (Logout Redirect URI)
	 * 이곳에서 추가적인 서비스 로그아웃 처리나 초기 화면 리다이렉션 등을 구현할 수 있음.
	 */
	@GetMapping("/logout/callback")
	public ResponseEntity<String> logoutCallback() {
		// 추가적인 후속 처리 (예: 세션 삭제, 프론트엔드 초기화 등) 구현 가능
		return ResponseEntity.ok("로그아웃 처리 완료. 초기 화면으로 리다이렉트합니다.");
	}


}
