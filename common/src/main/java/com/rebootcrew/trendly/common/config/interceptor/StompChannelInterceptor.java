package com.rebootcrew.trendly.common.config.interceptor;

import com.rebootcrew.trendly.common.config.JwtTokenProvider;
import com.rebootcrew.trendly.common.exception.CustomException;
import com.rebootcrew.trendly.common.exception.ErrorCode;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class StompChannelInterceptor implements ChannelInterceptor {

	private final JwtTokenProvider jwtTokenProvider; // 직접 구현한 JWT 검증 로직

	public StompChannelInterceptor(JwtTokenProvider jwtTokenProvider) {
		this.jwtTokenProvider = jwtTokenProvider;
	}

	/**
	 * STOMP 레벨에서 넘어오는 모든 메시지를 가로채어 검사 가능.
	 * CONNECT 프레임일 경우, Authorization 헤더를 파싱해 인증 처리
	 */
	@Override
	public Message<?> preSend(Message<?> message, MessageChannel channel) {
		StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

		// 1) 메시지를 수정 가능 상태로
		accessor.setLeaveMutable(true);

		System.out.println("preSend called with command=" + accessor.getCommand() +
				", sessionId=" + accessor.getSessionId());

		// 2) CONNECT 시점에 JWT 검사 → 인증 성공 시 setUser(...)
		if (StompCommand.CONNECT.equals(accessor.getCommand())) {
			// 클라이언트가 보낸 STOMP 헤더에서 Authorization 헤더 뽑기
			String authHeader = accessor.getFirstNativeHeader("Authorization");
			if (authHeader != null && authHeader.startsWith("Bearer ")) {
				String token = authHeader.substring(7); // "Bearer " 이후
				// (1) 토큰 유효성 검증
				if (jwtTokenProvider.validateToken(token, "access")) {
					// (2) 인증정보를 만들어 SecurityContext 에 설정
					UsernamePasswordAuthenticationToken authentication =
							(UsernamePasswordAuthenticationToken) jwtTokenProvider.getAuthentication(token);
					SecurityContextHolder.getContext().setAuthentication(authentication);

					// (3) 이후 @MessageMapping 메서드 등에서 Principal 활용 가능
					accessor.setUser(authentication); // STOMP 세션에 인증 정보를 저장
					// 세션 속성에 토큰 저장
					accessor.getSessionAttributes().put("token", token);

					System.out.println("Session Principal set: " + authentication.getName() +
							", sessionId=" + accessor.getSessionId());
				} else {
					throw new CustomException(ErrorCode.INVALID_TOKEN, "");
				}
			} else {
				throw new CustomException(ErrorCode.NO_JWT_TOKEN, "");
			}

			// 3) SUBSCRIBE, SEND 등 다른 명령에서 Principal이 없다면 세션 토큰으로 복구
		} else if (accessor.getUser() == null) {
			// Principal이 없으면 세션 속성에서 토큰으로 복구
			String token = (String) accessor.getSessionAttributes().get("token");
			if (token != null && jwtTokenProvider.validateToken(token, "access")) {
				var authentication = (UsernamePasswordAuthenticationToken)
						jwtTokenProvider.getAuthentication(token);

				accessor.setUser(authentication);
				System.out.println("Principal restored from session for " + accessor.getCommand());
			} else {
				throw new CustomException(ErrorCode.NO_AUTHENTICATED, "");
			}
		}

		// 4) 변경된 헤더(Principal 포함)를 새 메시지로 빌드해 반환
		return MessageBuilder.createMessage(message.getPayload(), accessor.getMessageHeaders());
	}
}
