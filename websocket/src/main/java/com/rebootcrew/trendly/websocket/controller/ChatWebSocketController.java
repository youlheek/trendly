package com.rebootcrew.trendly.websocket.controller;

import com.rebootcrew.trendly.websocket.application.ChatWebSocketApplication;
import com.rebootcrew.trendly.websocket.dto.ChatLikeReqeust;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ChatWebSocketController {

	private final ChatWebSocketApplication chatWebSocketApplication;

	/**
	 * 1) 메시지 전송
	 */
	@MessageMapping("/rooms/{roomId}/message.send")
	public void sendMessage(@DestinationVariable Long roomId, ChatMessageRequest request) {
		chatWebSocketApplication.handleSendMessage(
				roomId,
				request.getUserId(),
				request.getNickname(),
				request.getContent()
		);
	}

	/**
	 * 2) 메시지 수정
	 */
	@MessageMapping("/rooms/{roomId}/message.update")
	public void updateMessage(@DestinationVariable Long roomId, ChatMessageUpdateRequest request) {
		chatWebSocketApplication.handleUpdateMessage(
				roomId,
				request.getUserId(),
				request.getMessageId(),
				request.getNewContent()
		);
	}

	/**
	 * 3) 메시지 삭제
	 */
	@MessageMapping("/rooms/{roomId}/message.delete")
	public void deleteMessage(@DestinationVariable Long roomId, ChatMessageDeleteRequest request) {
		chatWebSocketApplication.handleDeleteMessage(
				roomId,
				request.getUserId(),
				request.getMessageId()
		);
	}

	/**
	 * 4) 댓글(Reply) 생성
	 */
	@MessageMapping("/rooms/{roomId}/reply.send")
	public void sendReply(@DestinationVariable Long roomId, ChatReplyRequest request) {
		chatWebSocketApplication.handleSendReply(
				roomId,
				request.getUserId(),
				request.getNickname(),
				request.getParentMessageId(),
				request.getContent()
		);
	}

	/**
	 * 5) 댓글 수정
	 */
	@MessageMapping("/rooms/{roomId}/reply.update")
	public void updateReply(@DestinationVariable Long roomId, ChatReplyUpdateRequest request) {
		chatWebSocketApplication.handleUpdateReply(
				roomId,
				request.getUserId(),
				request.getReplyId(),
				request.getNewContent()
		);
	}

	/**
	 * 6) 댓글 삭제
	 */
	@MessageMapping("/rooms/{roomId}/reply.delete")
	public void deleteReply(@DestinationVariable Long roomId, ChatReplyDeleteRequest request) {
		chatWebSocketApplication.handleDeleteReply(
				roomId,
				request.getUserId(),
				request.getReplyId()
		);
	}

	// ========= 좋아요 (ROOM) =========
	@MessageMapping("/rooms/{roomId}/like.room")
	public void likeRoom(@DestinationVariable Long roomId, ChatLikeReqeust.ChatLikeRoomRequest request) {
		chatWebSocketApplication.handleLikeRoom(roomId, request.getUserId(), request.isLike());
	}

	// ========= 좋아요 (MESSAGE) =======
	@MessageMapping("/rooms/{roomId}/like.message")
	public void likeMessage(@DestinationVariable Long roomId, ChatLikeReqeust.ChatLikeMessageRequest request) {
		chatWebSocketApplication.handleLikeMessage(roomId, request.getUserId(), request.getMessageId(), request.isLike());
	}

	// ========= 좋아요 (REPLY) =======
	@MessageMapping("/rooms/{roomId}/like.reply")
	public void likeReply(@DestinationVariable Long roomId, ChatLikeReplyRequest request) {
		chatWebSocketApplication.handleLikeReply(roomId, request.getUserId(), request.getReplyId(), request.isLike());
	}

	/**
	 * 8) 채팅방 닉네임 변경
	 */
	@MessageMapping("/rooms/{roomId}/nickname.update")
	public void updateNickname(@DestinationVariable Long roomId, ChatNicknameUpdateRequest request) {
		chatWebSocketApplication.handleNicknameUpdate(
				roomId,
				request.getUserId(),
				request.getNewNickname()
		);
	}

	/**
	 * 9) 채팅방 나가기
	 */
	@MessageMapping("/rooms/{roomId}/leave")
	public void leaveRoom(@DestinationVariable Long roomId, ChatLeaveRequest request) {
		chatWebSocketApplication.handleLeaveRoom(
				roomId,
				request.getUserId()
		);
	}
}
