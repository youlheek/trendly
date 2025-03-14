package com.rebootcrew.trendly.common.config.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rebootcrew.trendly.common.dto.ErrorResponse;
import com.rebootcrew.trendly.common.exception.ErrorCode;
import com.rebootcrew.trendly.common.exception.JwtAuthenticationException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
	@Override
	public void commence(HttpServletRequest request,
						 HttpServletResponse response,
						 AuthenticationException authException) throws IOException, ServletException {

		ErrorCode errorCode = (authException instanceof JwtAuthenticationException)
				? ((JwtAuthenticationException) authException).getErrorCode()
				: ErrorCode.INVALID_TOKEN; // 기본값
		// 예외 타입 확인: authException instanceof UnauthorizedException으로 전달된 예외가 UnauthorizedException인지 확인


//		response.setStatus(HttpStatus.UNAUTHORIZED.value());
		response.setStatus(errorCode.getHttpStatus().value());
		response.setContentType("application/json;charset=UTF-8");
		// 응답 설정: ErrorCode에 정의된 HTTP 상태 코드(예: 401 Unauthorized)를 응답에 설정하고, JSON 형식으로 응답을 보낼 준비


		// 1) Map을 사용해 직접 JSON 구조를 만듦
		Map<String, Object> errorBody = new LinkedHashMap<>();
		errorBody.put("status", errorCode.getHttpStatus().value());
		errorBody.put("error", errorCode.name());
		errorBody.put("message", errorCode.getDetail());
		errorBody.put("timestamp", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));

//		errorBody.put("status", HttpStatus.UNAUTHORIZED.value());
//		errorBody.put("error", "INVALID_TOKEN");
//		errorBody.put("message", "유효하지 않은 토큰입니다.");  // 필요 시 토큰 예외 메시지로 교체

		errorBody.put("path", request.getRequestURI());

		// 2) ObjectMapper로 Map → JSON 문자열 변환
		String jsonResponse = new ObjectMapper().writeValueAsString(errorBody);

		// 3) 응답에 JSON 쓰기
		response.getWriter().write(jsonResponse);
	}
}
