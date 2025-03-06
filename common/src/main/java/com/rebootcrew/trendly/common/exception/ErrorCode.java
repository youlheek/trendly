package com.rebootcrew.trendly.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum ErrorCode {
	// 필요한 ErrorCode 작성

	// 400 Bad Request
	ALREADY_REGISTERED_USER(HttpStatus.BAD_REQUEST, "이미 가입된 회원입니다."),
	ALREADY_VERIFY(HttpStatus.BAD_REQUEST,"이미 인증이 완료되었습니다."),
	EXPIRE_CODE(HttpStatus.BAD_REQUEST,"인증 시간이 만료되었습니다."),
	WRONG_VERIFICATION(HttpStatus.BAD_REQUEST,"잘못된 인증 시도입니다."),

	// 401 Unauthorize
	UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "유효한 토큰이 필요합니다."),
	LOGIN_CHECK_FAIL(HttpStatus.UNAUTHORIZED, "아이디와 패스워드를 확인해 주세요."),


	// 403 Forbidden
	ACCESS_DENIED(HttpStatus.FORBIDDEN, "접근 권한이 없습니다"),

	// 404 Not Found
	RESOURCE_NOT_FOUND(HttpStatus.NOT_FOUND, "요청한 리소스를 찾을 수 없습니다."),
	NOT_FOUND_USER(HttpStatus.NOT_FOUND, "일치하는 회원이 없습니다."),

	// 500 Internal Server Error
	INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류가 발생했습니다."),
	REDIS_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Redis 처리 중 오류가 발생했습니다."),
	JWT_BLACKLIST_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "토큰 블랙리스트 처리 중 오류가 발생했습니다."),



	;
	private final HttpStatus httpStatus;
	private final String detail;
}
