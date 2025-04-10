package com.rebootcrew.trendly.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum ErrorCode {
	// 필요한 ErrorCode 작성

	// 400 Bad Request : 클라이언트의 요청이 잘못되었거나 유효하지 않을 때 사용됩니다. 요청 자체에 문제가 있어 서버가 이를 처리할 수 없는 경우입니다.
	ALREADY_REGISTERED_USER(HttpStatus.BAD_REQUEST, "이미 가입된 회원입니다."),
	ALREADY_VERIFY(HttpStatus.BAD_REQUEST,"이미 인증이 완료되었습니다."),
	ALREADY_USED_NICKNAME(HttpStatus.BAD_REQUEST, "이미 존재하는 닉네임입니다."),
	ALREADY_JOINED(HttpStatus.BAD_REQUEST, "이미 입장한 채팅방입니다."),
	INVALID_VERIFICATION(HttpStatus.BAD_REQUEST,"잘못된 인증 시도입니다."),
	INVALID_INPUT(HttpStatus.BAD_REQUEST, "잘못된 입력입니다."),
	INVALID_JSON_FORMAT(HttpStatus.BAD_REQUEST, "잘못된 JSON 형식입니다."),
	NOT_FOUND_ROOM(HttpStatus.BAD_REQUEST, "해당 채팅방이 존재하지 않거나 닫힌 상태입니다."),
	NOT_FOUND_ACTIVE_CHAT_ROOM_MEMBER(HttpStatus.BAD_REQUEST, "해당 채팅방에 활성화된 멤버를 찾을 수 없습니다."),
	NO_JWT_TOKEN(HttpStatus.BAD_REQUEST, "Header에 JWT 토큰이 없습니다."),
	MESSAGE_TOO_LONG(HttpStatus.BAD_REQUEST, "메시지는 255자 이내여야 합니다."),
	NICKNAME_TOO_LONG(HttpStatus.BAD_REQUEST, "닉네임은 20자 이내여야 합니다."),
	NO_MESSAGE_ID(HttpStatus.BAD_REQUEST, "message id가 누락되었습니다."),

	// 401 Unauthorize : 인증(Authentication)이 필요하거나 인증 정보가 잘못되었을 때 사용됩니다. 주로 로그인하지 않았거나, 토큰/비밀번호가 틀린 경우입니다.
	INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않거나 만료된 토큰입니다."),
	INVALID_TOKEN_TYPE(HttpStatus.UNAUTHORIZED, "유효한 토큰 타입이 아닙니다."),
	LOGIN_CHECK_FAIL(HttpStatus.UNAUTHORIZED, "아이디와 패스워드를 확인해 주세요."),
	KAKAO_TOKEN_REQUEST_FAILED(HttpStatus.UNAUTHORIZED, "카카오 토큰 발급에 실패했습니다."),
	NO_AUTHENTICATED(HttpStatus.UNAUTHORIZED, "인증정보가 유실되었습니다."),


	// 403 Forbidden : 클라이언트가 인증은 되었지만, 권한(Authorization)이 없어서 요청이 거부되었을 때 사용됩니다. 서버가 요청을 이해했지만, 접근을 허용하지 않는 경우입니다.
	ACCESS_DENIED(HttpStatus.FORBIDDEN, "접근 권한이 없습니다"),
	USER_ALREADY_DELETED_EXPIRED(HttpStatus.FORBIDDEN, "탈퇴한 회원입니다."),


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
