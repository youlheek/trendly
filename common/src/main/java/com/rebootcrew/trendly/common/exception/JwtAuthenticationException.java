package com.rebootcrew.trendly.common.exception;

import lombok.Getter;
import org.springframework.security.core.AuthenticationException;
// 필터에서 jwt 토큰 관련 예외처리를 하기 위해서 AuthenticationException 을 상속받는다

@Getter
public class JwtAuthenticationException extends AuthenticationException {	private final ErrorCode errorCode;

	public JwtAuthenticationException(ErrorCode errorCode) {
		super(errorCode.getDetail());
		this.errorCode = errorCode;
	}
}
