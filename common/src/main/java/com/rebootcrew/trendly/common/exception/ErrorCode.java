package com.rebootcrew.trendly.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum ErrorCode {
	// 필요한 ErrorCode 작성
	ALREADY_REGISTERED_USER(HttpStatus.BAD_REQUEST, "이미 가입된 회원입니다.");

	private final HttpStatus httpStatus;
	private final String detail;
}
