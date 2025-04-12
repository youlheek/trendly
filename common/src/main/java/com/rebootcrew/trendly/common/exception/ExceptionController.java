package com.rebootcrew.trendly.common.exception;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.rebootcrew.trendly.common.domain.dto.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.support.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

@ControllerAdvice
@Slf4j
// 전역 예외 처리 & 전역 설정을 위한 클래스
public class ExceptionController {
	// TODO : 각 에러 클래스를 나누는 기준은?

	@ExceptionHandler(CustomException.class)
	public ResponseEntity<ErrorResponse> customRequestException (
			final CustomException ex, HttpServletRequest request) {
		log.error("CustomException 발생: {}", ex.getMessage());

		if (ex.getExtra() != null) {
			return ResponseEntity
				.status(ex.getErrorCode().getHttpStatus())
				.body(ErrorResponse.of(ex.getErrorCode(), request.getRequestURI(), ex.getExtra()));
		}

		return ResponseEntity
				.status(ex.getErrorCode().getHttpStatus())
				.body(ErrorResponse.of(ex.getErrorCode(), request.getRequestURI(), null));
	}

	@ExceptionHandler(JwtAuthenticationException.class)
	public ResponseEntity<ErrorResponse> handleUnauthorizedException(
			final JwtAuthenticationException ex, HttpServletRequest request) {
		log.error("JwtAuthenticationException 발생: {}", ex.getMessage());
		return ResponseEntity.status(ex.getErrorCode().getHttpStatus())
				.body(ErrorResponse.of(ex.getErrorCode(), request.getRequestURI(), null));
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorResponse> handleValidationExceptions(
			final MethodArgumentNotValidException ex, HttpServletRequest request) {
		log.error("MethodArgumentNotValidException 발생: {}", ex.getMessage());
		return ResponseEntity.status(ErrorCode.INVALID_INPUT.getHttpStatus())
				.body(ErrorResponse.of(ErrorCode.INVALID_INPUT, request.getRequestURI(), null));
	}

	@ExceptionHandler(JsonParseException.class)
	public ResponseEntity<ErrorResponse> handleJsonParseException(
			JsonParseException ex, HttpServletRequest request) {
		log.error("JsonParseException 발생: {}", ex.getMessage());
		return ResponseEntity.status(ErrorCode.INVALID_JSON_FORMAT.getHttpStatus())
				.body(ErrorResponse.of(ErrorCode.INVALID_JSON_FORMAT, request.getRequestURI(), null));
	}

	@ExceptionHandler(InvalidFormatException.class)
	public ResponseEntity<ErrorResponse> handleInvalidFormatException(
			InvalidFormatException ex, HttpServletRequest request) {
		log.error("InvalidFormatException 발생: {}", ex.getMessage());
		return ResponseEntity.status(ErrorCode.INVALID_INPUT.getHttpStatus())
				.body(ErrorResponse.of(ErrorCode.INVALID_INPUT, request.getRequestURI(), null));
	}

	@ExceptionHandler(DateTimeParseException.class)
	public ResponseEntity<ErrorResponse> handleDateTimeParseException(
			DateTimeParseException ex, HttpServletRequest request) {
		log.error("DateTimeParseException 발생: {}", ex.getMessage());
		return ResponseEntity.status(ErrorCode.INVALID_INPUT.getHttpStatus())
				.body(ErrorResponse.of(ErrorCode.INVALID_INPUT, request.getRequestURI(), null));
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

}
