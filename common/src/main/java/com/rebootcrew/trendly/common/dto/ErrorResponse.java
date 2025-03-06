package com.rebootcrew.trendly.common.dto;

import com.rebootcrew.trendly.common.exception.ErrorCode;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ErrorResponse {
	private int status;
	private String error;
	private String message;
	private LocalDateTime timestamp;
	private String path; // 에러 발생한 API 경로

	public static ErrorResponse of(ErrorCode errorCode, String path) {
		return ErrorResponse.builder()
				.status(errorCode.getHttpStatus().value())
				.error(errorCode.name()) // ✅ Enum의 errorCode 추가
				.message(errorCode.getDetail())
				.timestamp(LocalDateTime.now())
				.path(path)
				.build();
	}
}
