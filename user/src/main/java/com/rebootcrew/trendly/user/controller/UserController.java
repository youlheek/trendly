package com.rebootcrew.trendly.user.controller;

import com.rebootcrew.trendly.common.exception.ErrorCode;
import com.rebootcrew.trendly.common.exception.JwtAuthenticationException;
import com.rebootcrew.trendly.user.application.UserApplication;
import com.rebootcrew.trendly.common.domain.UserForm;
import com.rebootcrew.trendly.user.domain.UserResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth/user")
@RequiredArgsConstructor
public class UserController {
	// 회원 관리 컨트롤러

	private final UserApplication userApplication;

	@GetMapping("/me") // JWT 필요 ✅
	public ResponseEntity<UserResponse> getMyInfo(@AuthenticationPrincipal UserDetails userDetails) {

		// 인증된 사용자가 없으면 예외 발생
		if (userDetails == null || userDetails.getUsername() == null) {
			throw new JwtAuthenticationException(ErrorCode.NOT_FOUND_USER);
		}

		try {
			Long userId = (long) Integer.parseInt(userDetails.getUsername());
			UserResponse response = UserResponse.fromDto(userApplication.getMyInfo(userId));
			return ResponseEntity.ok(response);
//			return ResponseEntity.ok(UserResponse.fromDto(userApplication.getMyInfo(userId)));
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException("유효하지 않은 사용자 ID 입니다.", e);
		}

	}

	@PatchMapping("/update") // JWT 필요 ✅
	public ResponseEntity<UserResponse> updateUser(
			@AuthenticationPrincipal UserDetails userDetails,
			@RequestBody @Valid UserForm request) { // @Valid 로 유효성 검사를 수행

		if (userDetails == null || userDetails.getUsername() == null) {
			throw new JwtAuthenticationException(ErrorCode.NOT_FOUND_USER);
		}

		try {
			Long userId = Long.parseLong(userDetails.getUsername());
			return ResponseEntity.ok(UserResponse.fromDto(userApplication.updateUser(userId, request)));
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException("유효하지 않은 사용자 ID 입니다.", e);
		}
	}

	@PatchMapping("/delete") // JWT 필요 ✅
	public ResponseEntity<String> deleteUser(HttpServletRequest request, @AuthenticationPrincipal UserDetails userDetails) {
		String token = request.getHeader("Authorization");
		if (token != null && token.startsWith("Bearer ")) {
			token = token.substring(7);
		} else {
			throw new JwtAuthenticationException(ErrorCode.INVALID_TOKEN);
		}

		if (userDetails == null || userDetails.getUsername() == null) {
			throw new JwtAuthenticationException(ErrorCode.NOT_FOUND_USER);
		}

		try {
			Long userId = (long) Integer.parseInt(userDetails.getUsername());
			return ResponseEntity.ok(userApplication.deleteUser(userId, token));
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException("유효하지 않은 사용자 ID 입니다.", e);

		}
	}
}
