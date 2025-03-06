package com.rebootcrew.trendly.common.exception;

import lombok.Getter;

@Getter
public class UnauthorizedException extends RuntimeException {
	private final ErrorCode errorCode;

	public UnauthorizedException(ErrorCode errorCode) {
		super(errorCode.getDetail());
		this.errorCode = errorCode;
	}
}
