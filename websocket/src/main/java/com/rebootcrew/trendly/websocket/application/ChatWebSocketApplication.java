package com.rebootcrew.trendly.websocket.application;

import com.rebootcrew.trendly.websocket.dto.BroadcastMessageResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatWebSocketApplication {

	private final SimpMessagingTemplate messagingTemplate;
	private final ChatService chatService; // discussion 모듈

	public void handleLikeRoom(Long roomId, Long userId, boolean isLike) {
		ChatRoomDto updatedRoom = (isLike)
				? chatService.likeRoom(roomId, userId)
				: chatService.unlikeRoom(roomId, userId);

		messagingTemplate.convertAndSend(
				"/topic/rooms/" + roomId + "/like",
				new BroadcastMessageResponse(isLike ? "LIKE_ROOM" : "UNLIKE_ROOM", updatedRoom)
		);
	}

	public void handleLikeMessage(Long roomId, Long userId, Long messageId, boolean isLike) {
		ChatMessageDto updatedMsg = (isLike)
				? chatService.likeMessage(roomId, userId, messageId)
				: chatService.unlikeMessage(roomId, userId, messageId);

		messagingTemplate.convertAndSend(
				"/topic/rooms/" + roomId + "/message",
				new BroadcastMessageResponse(isLike ? "LIKE_MESSAGE" : "UNLIKE_MESSAGE", updatedMsg)
		);
	}

	public void handleLikeReply(Long roomId, Long userId, Long replyId, boolean isLike) {
		ChatReplyDto updatedReply = (isLike)
				? chatService.likeReply(roomId, userId, replyId)
				: chatService.unlikeReply(roomId, userId, replyId);

		messagingTemplate.convertAndSend(
				"/topic/rooms/" + roomId + "/reply",
				new BroadcastMessageResponse(isLike ? "LIKE_REPLY" : "UNLIKE_REPLY", updatedReply)
		);
	}

	// 나머지 메시지/댓글/닉네임 변경/나가기 등도 유사하게 handleXXX(...) 작성
}

