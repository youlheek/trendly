package com.rebootcrew.trendly.websocket.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		// 클라이언트가 WebSocket연결을 요청할 엔드포인트 지정
		registry.addEndpoint("/websocket")
				.setAllowedOrigins("*") // 모든 도메인에서 연결 허용
				.withSockJS(); // SockJS 를 사용해서 WebSocket을 지원하지 않는 브라우저에서도 연결할 수 있도록 대체 프로토콜 사용
	}

	@Override
	public void configureMessageBroker(MessageBrokerRegistry registry) {
		// 메시지 브로커 활성화 및 기본 프리픽스 설정
		// 메시지 브로커 -> 구독자에게 전달할 메시지
		registry.enableSimpleBroker("/topic");

		// 클라이언트가 보낸 메시지의 경로 프리픽스 지정
		// 클라이언트가 보내는 메시지 중 실제 서버에서 처리할 메시지는 /app 으로 시작하는 경로를 사용하게 지정
		registry.setApplicationDestinationPrefixes("/app");
	}
}
