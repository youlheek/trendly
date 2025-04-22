package com.rebootcrew.trendly.common.config;

import com.rebootcrew.trendly.common.config.interceptor.StompChannelInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker // STOMP 활성화
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

	private final StompChannelInterceptor stompChannelInterceptor;

	public WebSocketConfig(StompChannelInterceptor stompChannelInterceptor) {
		this.stompChannelInterceptor = stompChannelInterceptor;
	}


	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {

		registry.addEndpoint("/ws")
				.setAllowedOriginPatterns("*"); // CORS 설정
//				.withSockJS();
	}

	@Override
	public void configureMessageBroker(MessageBrokerRegistry registry) {
		// 클라이언트가 채팅 메시지를 받는 경로 e.g) /topic/chat/1
		// /topic : 채팅방 전체 알림, 다중 수신
		// /queue : 개인 메시지, 단일 수신
		registry.enableSimpleBroker("/topic", "/queue");

		// 클라이언트가 메시지 보낼 경로
		// 클라이언트가 메시지를 보내는 경로 e.g) /app/chat/1
		registry.setApplicationDestinationPrefixes("/app");
	}

	// ******* 핵심: InboundChannel에 Interceptor 등록해서 CONNECT 시 헤더를 확인 ********
	@Override
	public void configureClientInboundChannel(ChannelRegistration registration) {
		registration.interceptors(stompChannelInterceptor);
	}

/*
	@Override
	public void configureWebSocketTransport(WebSocketTransportRegistration registration) {
		registration.addDecoratorFactory(new WebSocketHandlerDecoratorFactory() {
			@Override
			public WebSocketHandler decorate(WebSocketHandler handler) {
				return handler;
			}
		});

//		registration.addDecoratorFactory(serverHandler -> {
//			System.out.println("WebSocket handler initialized: " + serverHandler);
//			return serverHandler;
//		});
	}

 */
}