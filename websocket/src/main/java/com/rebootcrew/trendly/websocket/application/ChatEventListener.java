package com.rebootcrew.trendly.websocket.application;

import com.rebootcrew.trendly.discussion.domain.dto.UserEnteredChatRoomEvent;
import com.rebootcrew.trendly.websocket.dto.BroadcastMessageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatEventListener {

	private final SimpMessageSendingOperations messagingTemplate;

	@EventListener
	public void onUserEnteredChatRoom(UserEnteredChatRoomEvent event) {
		// 이벤트가 발생하면 이 메서드가 자동으로 불린다

		// 1. WebSocket 용 DTO (=BroadcastMessageResponse) 생성
		BroadcastMessageResponse response = new BroadcastMessageResponse(
				"USER_ENTERED", event.getNickname() + "님이 입장하셨습니다."
		);

		// 2. STOMP 브로드캐스트
		messagingTemplate.convertAndSend(
				"/topic/rooms/" + event.getRoomId(),
				response
		);
	}
}
