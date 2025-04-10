package com.rebootcrew.trendly.discussion.service;

import com.rebootcrew.trendly.discussion.domain.ChatRoomMember;
import com.rebootcrew.trendly.discussion.domain.dto.ChatMessageDto;
import com.rebootcrew.trendly.common.exception.CustomException;
import com.rebootcrew.trendly.common.respository.LikesRepository;
import com.rebootcrew.trendly.common.respository.UserRepository;
import com.rebootcrew.trendly.discussion.domain.ChatMessage;
import com.rebootcrew.trendly.discussion.domain.dto.NicknameDto;
import com.rebootcrew.trendly.discussion.domain.service.ChatRoomValidator;
import com.rebootcrew.trendly.discussion.repository.ChatMessageRepository;
import com.rebootcrew.trendly.discussion.repository.ChatRoomMemberRepository;
import com.rebootcrew.trendly.discussion.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.rebootcrew.trendly.common.exception.ErrorCode.*;
import static com.rebootcrew.trendly.discussion.domain.enums.ChatRoomMemeberStatus.ACTIVE;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatService {

	private final ChatMessageRepository chatMessageRepository;
	private final UserRepository userRepository;
	private final ChatRoomRepository chatRoomRepository;
	private final LikesRepository likesRepository;
	private final ChatRoomMemberRepository chatRoomMemberRepository;
	private final ChatRoomValidator chatRoomValidator;

	/**
	 * 최근 메시지 불러오기
	 */
	public List<ChatMessageDto> getRecentMessages(Long roomId, int limit, Long messageId) {
			Pageable pageable = PageRequest.of(0, limit);
			List<ChatMessage> messages;

			if (messageId == 0L) {
				// 입장 시 : 최신 메시지
				messages = chatMessageRepository.findRecentMessages(roomId, pageable);
			} else {
				// 스크롤 시 : 특정 메시지 이전
				LocalDateTime pivotTime = chatMessageRepository.findCreatedAtById(messageId);
				messages = chatMessageRepository.findMessagesBefore(roomId, pivotTime, pageable);
			}

		Collections.reverse(messages);

		return buildChatMessageDto(messages);
	}

	private List<ChatMessageDto> buildChatMessageDto(List<ChatMessage> msgList) {
		return msgList.stream()
				.map(msg -> ChatMessageDto.builder()
						.messageId(msg.getId())
						.message(msg.getMessage())
						.userId(msg.getUser().getId())
//						.nickname(msg.getChatRoomMember().getNickname())
//						.roomId(msg.getChatRoom().getId())
						.createdAt(msg.getCreatedAt())
						.build()
				)
				.collect(Collectors.toList());
	}

	// 채팅 메시지 저장
	public ChatMessageDto saveMessage(Long roomId, Long userId, String message) {

		ChatMessage chatMessage = ChatMessage.builder()
				.message(message)
				.user( // User 객체 연결
						userRepository.findByIdAndDeletedAtIsNull(userId)
						.orElseThrow(() -> new CustomException(NOT_FOUND_USER, "")))
				.chatRoomMember( // ChatRoomMember 연결
						chatRoomMemberRepository.findByChatRoom_IdAndUser_IdAndStatus(roomId, userId, ACTIVE)
								.orElseThrow(() -> new CustomException(NOT_FOUND_ACTIVE_CHAT_ROOM_MEMBER, "")))
				.chatRoom( // ChatRoom 연결
						chatRoomRepository.findById(roomId)
						.orElseThrow(() -> new CustomException(NOT_FOUND_ROOM, "")))
				.createdAt(LocalDateTime.now())
				.build();

		chatMessageRepository.save(chatMessage);
		return convertToDto(chatMessage);
	}

	private ChatMessageDto convertToDto(ChatMessage message) {
		ChatMessageDto dto = ChatMessageDto.builder()
				.message(message.getMessage())
				.messageId(message.getId())
				.userId(message.getUser().getId())
				.nickname(message.getChatRoomMember().getNickname())
				.roomId(message.getChatRoom().getId())
				.createdAt(message.getCreatedAt())
				.build();
		return dto;

		// TODO : ChatMessageDto 에 static from 메서드를 만들고 싶은데 이러면 순환 종속 오류가 생김
	}


	public ChatMessageDto deleteMessage(Long roomId, Long userId, Long messageId) {

		return null;
	}
}
