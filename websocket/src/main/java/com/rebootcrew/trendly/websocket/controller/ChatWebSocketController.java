package com.rebootcrew.trendly.websocket.controller;

import com.rebootcrew.trendly.discussion.domain.dto.ChatMessageDto;
import com.rebootcrew.trendly.common.respository.UserRepository;
import com.rebootcrew.trendly.discussion.service.ChatRoomService;
import com.rebootcrew.trendly.websocket.application.ChatWebSocketApplication;
import com.rebootcrew.trendly.websocket.dto.ChatMessageRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ChatWebSocketController {

	private final ChatWebSocketApplication chatWebSocketApplication;


//	@MessageMapping("/rooms/{roomId}/enter")
//	public void enterChatRoom (
//			@DestinationVariable Long roomId,
//			@Header("nickname") String nickname,
////			@AuthenticationPrincipal UserDetails userDetails
//			Principal principal
//	) {
//		// User 객체 추출
//		String userId = principal.getName();
//		User user = userRepository.findByIdAndDeletedAtIsNull(Long.parseLong(userId))
//				.orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));
////		User user = userRepository.findById(Long.parseLong(userDetails.getUsername()))
////				.orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));
//
//		// 사용자 입장 처리
//		chatRoomService.enterChatRoom(roomId, user, nickname);
//
//		// 입장 알림 브로드캐스트
//		messagingTemplate.convertAndSend(
//				"/topic/rooms/" + roomId,
//				new BroadcastMessageResponse("USER_ENTERED", nickname + "님이 입장하셨습니다.")
//		);
//
//		// 기존 메시지 로드
//		List<ChatMessageDto> recentMessages =
//				chatWebSocketApplication.getRecentMessages(roomId, 0);
//		// TODO : 채팅방 기존 메시지 반환
//		messagingTemplate.convertAndSendToUser(
//				principal.getName(),   // 대상 사용자 식별 (예: 사용자 ID 또는 username)
//				"/user/queue/recentMessages",  // 대상 사용자용 queue 경로 (클라이언트는 "/user/queue/recentMessages"로 구독)
//				recentMessages           // 전송할 메시지 (예: 기존 채팅 메시지 리스트)
//		);
//
//	}

	/**
	 * 1) 메시지 전송
	 */
	@MessageMapping("/rooms/{roomId}/message.send")
//	@SendTo("/topic/rooms/{roomId}")
	public void sendMessage (@DestinationVariable Long roomId, ChatMessageRequest request) {
		System.out.println("Message received: " + request.getMessage()); // 디버깅용
		chatWebSocketApplication.handleSendMessage(
				roomId,
				request.getUserId(),
				request.getMessage()
		);
	}

	/**
	 * 2) 메시지 수정
	 */
//	@MessageMapping("/rooms/{roomId}/message.update")
//	public void updateMessage(@DestinationVariable Long roomId, ChatMessageRequest request) {
//		chatWebSocketApplication.handleUpdateMessage(
//				roomId,
//				request.getUserId(),
//				request.getMessageId(),
//				request.getMessage()
//		);
//	}

	/**
	 * 3) 메시지 삭제
	 */
	@MessageMapping("/rooms/{roomId}/message.delete")
	public void deleteMessage(@DestinationVariable Long roomId, ChatMessageDto request) {
		chatWebSocketApplication.handleDeleteMessage(
				roomId,
				request.getUserId(),
				request.getMessageId()
		);
	}
//
//	/**
//	 * 4) 댓글(Reply) 생성
//	 */
//	@MessageMapping("/rooms/{roomId}/reply.send")
//	public void sendReply(@DestinationVariable Long roomId, ChatCommentDto request) {
//		chatWebSocketApplication.handleSendReply(
//				roomId,
//				request.getUserId(),
//				request.getNickname(),
//				request.getParentMessageId(),
//				request.getContent()
//		);
//	}
//
//	/**
//	 * 5) 댓글 수정
//	 */
//	@MessageMapping("/rooms/{roomId}/reply.update")
//	public void updateReply(@DestinationVariable Long roomId, ChatReplyUpdateRequest request) {
//		chatWebSocketApplication.handleUpdateReply(
//				roomId,
//				request.getUserId(),
//				request.getReplyId(),
//				request.getNewContent()
//		);
//	}
//
//	/**
//	 * 6) 댓글 삭제
//	 */
//	@MessageMapping("/rooms/{roomId}/reply.delete")
//	public void deleteReply(@DestinationVariable Long roomId, ChatReplyDeleteRequest request) {
//		chatWebSocketApplication.handleDeleteReply(
//				roomId,
//				request.getUserId(),
//				request.getReplyId()
//		);
//	}
//
//	// ========= 좋아요 (ROOM) =========
//	@MessageMapping("/rooms/{roomId}/like.room")
//	public void likeRoom(@DestinationVariable Long roomId, ChatLikeReqeust.ChatLikeRoomRequest request) {
//		chatWebSocketApplication.handleLikeRoom(roomId, request.getUserId(), request.isLike());
//	}
//
//	// ========= 좋아요 (MESSAGE) =======
//	@MessageMapping("/rooms/{roomId}/like.message")
//	public void likeMessage(@DestinationVariable Long roomId, ChatLikeReqeust.ChatLikeMessageRequest request) {
//		chatWebSocketApplication.handleLikeMessage(roomId, request.getUserId(), request.getMessageId(), request.isLike());
//	}
//
//	// ========= 좋아요 (REPLY) =======
//	@MessageMapping("/rooms/{roomId}/like.reply")
//	public void likeReply(@DestinationVariable Long roomId, ChatLikeReplyRequest request) {
//		chatWebSocketApplication.handleLikeReply(roomId, request.getUserId(), request.getReplyId(), request.isLike());
//	}
//
//	/**
//	 * 8) 채팅방 닉네임 변경
//	 */
//	@MessageMapping("/rooms/{roomId}/nickname.update")
//	public void updateNickname(@DestinationVariable Long roomId, ChatNicknameUpdateRequest request) {
//		chatWebSocketApplication.handleNicknameUpdate(
//				roomId,
//				request.getUserId(),
//				request.getNewNickname()
//		);
//	}
//
//	/**
//	 * 9) 채팅방 나가기
//	 */
//	@MessageMapping("/rooms/{roomId}/leave")
//	public void leaveRoom(@DestinationVariable Long roomId, ChatLeaveRequest request) {
//		chatWebSocketApplication.handleLeaveRoom(
//				roomId,
//				request.getUserId()
//		);
//	}
}
