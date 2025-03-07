package com.rebootcrew.trendly.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

//	@PostMapping("/refresh")
//	public ResponseEntity<TokenResponse> refreshToken(@RequestBody RefreshTokenRequest request) {
//		// refresh token 검증 및 새로운 access token 발급 로직 구현
//		// 예: DB 또는 캐시에서 해당 refresh token 정보를 확인한 후, 유효하면 새로운 access token 생성
//	}
}
