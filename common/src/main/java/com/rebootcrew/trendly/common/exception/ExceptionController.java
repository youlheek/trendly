package com.rebootcrew.trendly.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
// 전역 예외 처리 & 전역 설정을 위한 클래스
public class ExceptionController {

	@ExceptionHandler(CustomException.class)
	public ResponseEntity<ExceptionResponse> customRequestException (
			final CustomException c) {
//		log.error(c.getMessage());
		return ResponseEntity.badRequest().body(new ExceptionResponse(c.getErrorCode(), c.getMessage()));
	}

	@Getter
	@ToString
	@AllArgsConstructor
	public static class ExceptionResponse {
		private ErrorCode errorCode;
		private String message;
	}
}
