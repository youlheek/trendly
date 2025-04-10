package com.rebootcrew.trendly.common.domain.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.rebootcrew.trendly.common.exception.ErrorCode;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {
	private int status;
	private String error;
	private String message;
	private LocalDateTime timestamp;
	private String path; // 에러 발생한 API 경로

	private Object extra; // ✅ 추가


	public static ErrorResponse of(ErrorCode errorCode, String path, Object extra) {
		return ErrorResponse.builder()
				.status(errorCode.getHttpStatus().value())
				.error(errorCode.name()) // ✅ Enum의 errorCode 추가
				.message(errorCode.getDetail())
				.timestamp(LocalDateTime.now())
				.extra(extra) // extra가 null이면 JSON에 안 나감
				.path(path)
				.build();
	}
}
