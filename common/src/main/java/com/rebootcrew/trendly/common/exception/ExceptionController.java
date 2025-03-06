package com.rebootcrew.trendly.common.exception;

import com.rebootcrew.trendly.common.dto.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
@Slf4j
// 전역 예외 처리 & 전역 설정을 위한 클래스
public class ExceptionController {
	// TODO : 각 에러 클래스를 나누는 기준은?

	@ExceptionHandler(CustomException.class)
	public ResponseEntity<ErrorResponse> customRequestException (
			final CustomException ex, HttpServletRequest request) {
		log.error("CustomException 발생: {}", ex.getMessage());
		return ResponseEntity.status(ex.getErrorCode().getHttpStatus())
				.body(ErrorResponse.of(ex.getErrorCode(), request.getRequestURI()));
	}

	@ExceptionHandler(UnauthorizedException.class)
	public ResponseEntity<ErrorResponse> handleUnauthorizedException(
			final UnauthorizedException ex, HttpServletRequest request) {
		log.error("UnauthorizedException 발생: {}", ex.getMessage());
		return ResponseEntity.status(ex.getErrorCode().getHttpStatus())
				.body(ErrorResponse.of(ex.getErrorCode(), request.getRequestURI()));
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handleException(
			final Exception ex, HttpServletRequest request) {
		log.error("예상치 못한 예외 발생: {}", ex.getMessage());
		return ResponseEntity.status(500)
				.body(ErrorResponse.builder()
						.status(500)
						.error("INTERNAL_SERVER_ERROR")
						.message("서버 내부 오류가 발생했습니다.")
						.timestamp(LocalDateTime.now())
						.path(request.getRequestURI())
						.build());
	}

	@Getter
	@ToString
	@AllArgsConstructor
	public static class ExceptionResponse {
		private ErrorCode errorCode;
		private String message;
	}
}
