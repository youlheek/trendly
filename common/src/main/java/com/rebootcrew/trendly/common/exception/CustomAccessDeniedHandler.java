package com.rebootcrew.trendly.common.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rebootcrew.trendly.common.dto.ErrorResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
// Filter의 Exception 처리를 위한 Handler

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response,
					   AccessDeniedException accessDeniedException) throws IOException, ServletException {
		ErrorResponse errorResponse = ErrorResponse.of(ErrorCode.UNAUTHORIZED, request.getRequestURI());
		response.setStatus(HttpStatus.UNAUTHORIZED.value());
		response.setContentType("application/json;charset=UTF-8");
		// JSON 변환은 본인 환경에 맞게 ObjectMapper 등을 사용
		new ObjectMapper().writeValue(response.getWriter(), errorResponse);
	}
}
