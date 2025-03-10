package com.rebootcrew.trendly.common.config.filter;

import com.rebootcrew.trendly.common.config.JwtTokenProvider;
import com.rebootcrew.trendly.common.exception.CustomException;
import com.rebootcrew.trendly.common.exception.ErrorCode;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

		if (header != null && header.startsWith("Bearer ")) {
			String token = header.substring(7);
			if (token != null && jwtTokenProvider.validateToken(token)) {
				// 토큰에서 사용자 정보 추출 -> Authentication 객체 생성
				Authentication auth = jwtTokenProvider.getAuthentication(token);

				// SecurityContext에 auth(인증 정보) 저장
				SecurityContextHolder.getContext().setAuthentication(auth);
			}
			// 필터에서 예외를 던지면 SpringSecurity 가 이를 제대로 처리하지 못하고 클라이언트에 예외가 노출될 수 있으므로
			// 인증 실패 시 예외를 던지지 않고 SecurityContextHolder에 인증 정보를 저장하지 않은 채로 요청을 진행시킴
//			else {
//				throw new CustomException(ErrorCode.UNAUTHORIZED);
//			}
		}

		filterChain.doFilter(request, response);
	}
}
