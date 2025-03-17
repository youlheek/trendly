package com.rebootcrew.trendly.discussion.service;

import com.rebootcrew.trendly.common.dto.ChatMessageDto;
import com.rebootcrew.trendly.common.exception.CustomException;
import com.rebootcrew.trendly.common.exception.ErrorCode;
import com.rebootcrew.trendly.common.respository.UserRepository;
import com.rebootcrew.trendly.discussion.domain.ChatMessage;
import com.rebootcrew.trendly.discussion.repository.ChatMessageRepository;
import com.rebootcrew.trendly.discussion.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatService {

	private ChatMessageRepository chatMessageRepository;
	private UserRepository userRepository;
	private ChatRoomRepository chatRoomRepository;

	public ChatMessageDto saveMessage(Long roomId, Long userId, String nickname, String message) {

		ChatMessage chatMessage = ChatMessage.builder()
				.message(message)
				.user(userRepository.findById(userId)
						.orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER)))
				.chatRoom(chatRoomRepository.findById(roomId)
						.orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_ROOM)))
				.createdAt(LocalDateTime.now())
				.build();
		chatMessageRepository.save(chatMessage);
		return convertToDto(chatMessage);
	}

	private ChatMessageDto convertToDto(ChatMessage message) {
		ChatMessageDto dto = new ChatMessageDto();
		dto.setId(message.getId());
		dto.setMessage(message.getMessage());
		// TODO : ... 매핑 로직
		return dto;

		// TODO : ChatMessageDto 에 static from 메서드를 만들고 싶은데 이러면 순환 종속 오류가 생김
	}
}
