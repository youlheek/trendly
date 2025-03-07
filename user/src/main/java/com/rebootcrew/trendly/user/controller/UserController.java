package com.rebootcrew.trendly.user.controller;

import com.rebootcrew.trendly.user.dto.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth/user")
@RequiredArgsConstructor
public class UserController {


//	@GetMapping("/me") // JWT 필요 ✅
//	public ResponseEntity<UserResponse> getMyInfo(@RequestHeader("Authorization") String token) {
//		return ResponseEntity.ok(userApplication.getMyInfo(token));
//	}
//
//	@PatchMapping("/update") // JWT 필요 ✅
//	public ResponseEntity<UserResponse> updateUser(
//			@RequestHeader("Authorization") String token,
//			@RequestBody UpdateUserRequest request
//	) {
//		return ResponseEntity.ok(userApplication.updateUser(token, request));
//	}
//
//	@DeleteMapping("/delete") // JWT 필요 ✅
//	public ResponseEntity<Void> deleteUser(@RequestHeader("Authorization") String token) {
//		userApplication.deleteUser(token);
//		return ResponseEntity.ok().build();
//	}
}
