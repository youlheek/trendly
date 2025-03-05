package com.rebootcrew.trendly.common.config.filter;

import com.rebootcrew.trendly.common.config.JwtTokenProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class CustomFilter extends OncePerRequestFilter {

	private final JwtTokenProvider jwtTokenProvider;

	@Override
	protected void doFilterInternal(HttpServletRequest request,
									HttpServletResponse response,
									FilterChain filterChain) throws ServletException, IOException {

		// HTTP Header 에서 토큰 추출
		String header = request.getHeader("Authorization");
		String token = null;

		if (header != null && header.startsWith("Bearer ")) {
			token = header.substring(7);
		}

		if (token != null && jwtTokenProvider.validateToken(token)) {
			// 토큰에서 사용자 정보 추출 -> Authentication 객체 생성
			Authentication auth = jwtTokenProvider.getAuthentication(token);

			// SecurityContext에 auth(인증 정보) 저장
			SecurityContextHolder.getContext().setAuthentication(auth);
		} else {
			throw new ServletException("Invalid token");
		}

		filterChain.doFilter(request, response);
	}
}
