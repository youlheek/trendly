package com.rebootcrew.trendly.websocket.application;

import com.rebootcrew.trendly.discussion.domain.dto.ChatMessageDto;
import com.rebootcrew.trendly.common.exception.CustomException;
import com.rebootcrew.trendly.discussion.domain.ChatRoom;
import com.rebootcrew.trendly.discussion.repository.ChatRoomRepository;
import com.rebootcrew.trendly.discussion.service.ChatService;
import com.rebootcrew.trendly.websocket.dto.BroadcastMessageResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.rebootcrew.trendly.common.exception.ErrorCode.*;
import static com.rebootcrew.trendly.discussion.domain.enums.ChatRoomMemeberStatus.ACTIVE;


@Slf4j
@Service
@RequiredArgsConstructor
public class ChatWebSocketApplication {

	private final SimpMessagingTemplate messagingTemplate;
	private final ChatService chatService; // discussion 모듈
	private final ChatRoomRepository chatRoomRepository;


	/**
	 * 채팅방 메시지 전송
	 * @param roomId
	 * @param userId
	 * @param message
	 */
	@Transactional
	public void handleSendMessage(Long roomId, Long userId, String message) {
		// 1. 채팅방이 존재하는지 확인 (상태 확인)
		ChatRoom chatRoom = chatRoomRepository.findChatRoomById(roomId)
				.orElseThrow(() -> new CustomException(NOT_FOUND_ROOM, ""));

		// 2. 사용자가 채팅방에 존재하는지 체크
		boolean isJoined = chatRoom.getMembers().stream()
				.anyMatch(member -> member.getUser().getId().equals(userId) &&
						member.getStatus() == ACTIVE
				);
		if (!isJoined) {
			throw new CustomException(NOT_FOUND_ACTIVE_CHAT_ROOM_MEMBER, "");
		}

		// 3. message DB 저장
		if (message.length() > 255) {
			throw new CustomException(MESSAGE_TOO_LONG, "");
		}
		ChatMessageDto chatMessageDto = chatService.saveMessage(roomId, userId, message);

		messagingTemplate.convertAndSend(
				"/topic/rooms/" + roomId,
				new BroadcastMessageResponse("NEW_MESSAGE", chatMessageDto));
	}

	public void handleDeleteMessage(Long roomId, Long userId, Long messageId) {
		ChatMessageDto chatMessageDto = chatService.deleteMessage(roomId, userId, messageId);
	}


//
//	public void handleLikeRoom(Long roomId, Long userId, boolean isLike) {
//		ChatRoomDto updatedRoom = (isLike)
//				? chatService.likeRoom(roomId, userId)
//				: chatService.unlikeRoom(roomId, userId);
//
//		messagingTemplate.convertAndSend(
//				"/topic/rooms/" + roomId + "/like",
//				new BroadcastMessageResponse(isLike ? "LIKE_ROOM" : "UNLIKE_ROOM", updatedRoom)
//		);
//	}
//
//	public void handleLikeMessage(Long roomId, Long userId, Long messageId, boolean isLike) {
//		ChatMessageDto updatedMsg = (isLike)
//				? chatService.likeMessage(roomId, userId, messageId)
//				: chatService.unlikeMessage(roomId, userId, messageId);
//
//		messagingTemplate.convertAndSend(
//				"/topic/rooms/" + roomId + "/message",
//				new BroadcastMessageResponse(isLike ? "LIKE_MESSAGE" : "UNLIKE_MESSAGE", updatedMsg)
//		);
//	}
//
//	public void handleLikeReply(Long roomId, Long userId, Long replyId, boolean isLike) {
//		ChatCommentDto updatedComment = (isLike)
//				? chatService.likeReply(roomId, userId, replyId)
//				: chatService.unlikeReply(roomId, userId, replyId);
//
//		messagingTemplate.convertAndSend(
//				"/topic/rooms/" + roomId + "/reply",
//				new BroadcastMessageResponse(isLike ? "LIKE_REPLY" : "UNLIKE_REPLY", updatedComment)
//		);
//	}

	// 나머지 메시지/댓글/닉네임 변경/나가기 등도 유사하게 handleXXX(...) 작성
}

