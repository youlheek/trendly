package com.rebootcrew.trendly.discussion.domain.service;

import com.rebootcrew.trendly.discussion.domain.enums.ChatRoomMemeberStatus;
import com.rebootcrew.trendly.discussion.repository.ChatRoomMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ChatRoomValidator {

	private final ChatRoomMemberRepository chatRoomMemberRepository;


	// 닉네임 중복체크
	public boolean validateDuplicateNickname(Long roomId, String nickname) {
		return chatRoomMemberRepository.existsByNicknameAndChatRoom_IdAndStatus(nickname, roomId, ChatRoomMemeberStatus.ACTIVE);
	}

}
