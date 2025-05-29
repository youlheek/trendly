package com.rebootcrew.trendly.common.exception;

import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {
	private final ErrorCode errorCode;
	private Object extra; // ✅ 부가 정보 담는 필드


	public CustomException(ErrorCode errorCode) {
		super(errorCode.getDetail());
		this.errorCode = errorCode;
	}

	public CustomException(ErrorCode errorCode, Object extra) {
		super(errorCode.getDetail());
		this.errorCode = errorCode;
		this.extra = extra;
	}
}
