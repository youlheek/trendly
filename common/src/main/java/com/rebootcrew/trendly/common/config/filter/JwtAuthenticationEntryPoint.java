package com.rebootcrew.trendly.common.config.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rebootcrew.trendly.common.dto.ErrorResponse;
import com.rebootcrew.trendly.common.exception.ErrorCode;
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

		response.setStatus(HttpStatus.UNAUTHORIZED.value());
		response.setContentType("application/json;charset=UTF-8");

//		ErrorResponse errorResponse = ErrorResponse.of(ErrorCode.EXPIRED_TOKEN, request.getRequestURI());
//		String jsonResponse = new ObjectMapper().writeValueAsString(errorResponse);
//		response.getWriter().write(jsonResponse);


		// 1) Map을 사용해 직접 JSON 구조를 만듦
		Map<String, Object> errorBody = new LinkedHashMap<>();
		errorBody.put("status", HttpStatus.UNAUTHORIZED.value());
		errorBody.put("error", "EXPIRED_TOKEN");
		errorBody.put("message", "유효하지 않은 토큰입니다.");  // 필요 시 토큰 예외 메시지로 교체
		errorBody.put("timestamp", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
		errorBody.put("path", request.getRequestURI());

		// 2) ObjectMapper로 Map → JSON 문자열 변환
		String jsonResponse = new ObjectMapper().writeValueAsString(errorBody);

		// 3) 응답에 JSON 쓰기
		response.getWriter().write(jsonResponse);
	}
}
