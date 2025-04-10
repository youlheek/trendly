package com.rebootcrew.trendly.discussion.application;

import com.rebootcrew.trendly.common.domain.User;
import com.rebootcrew.trendly.common.exception.CustomException;
import com.rebootcrew.trendly.common.exception.ErrorCode;
import com.rebootcrew.trendly.discussion.domain.ChatRoomMember;
import com.rebootcrew.trendly.discussion.domain.dto.*;
import com.rebootcrew.trendly.discussion.repository.ChatRoomMemberRepository;
import com.rebootcrew.trendly.discussion.service.ChatRoomService;
import com.rebootcrew.trendly.discussion.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static com.rebootcrew.trendly.discussion.domain.enums.ChatRoomMemeberStatus.ACTIVE;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatApplication {

	private final ChatService chatService;
	private final ChatRoomService chatRoomService;
	private final ApplicationEventPublisher eventPublisher; // 스프링에서 이벤트 쏘는 도구
	private final ChatRoomMemberRepository chatRoomMemberRepository;


	@Transactional
	public ChatRoomResponse enterChatRoom(Long roomId, User user, String nickname) {
		ChatRoomMemberDto memberDto = null;

		if (nickname == null) {
			// 재입장
			ChatRoomMember member = chatRoomMemberRepository.findByChatRoom_IdAndUser_IdAndStatus(roomId, user.getId(), ACTIVE)
					.orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_ACTIVE_CHAT_ROOM_MEMBER));
			memberDto = ChatRoomMemberDto.builder()
					.id(member.getId())
					.chatRoomId(member.getChatRoom().getId())
					.userId(member.getUser().getId())
					.nickname(member.getNickname())
					.build();
		}
		// if (nickname == null) 재입장 {
		// 		if (채팅방에 사용자가 ACTIVE 상태인지 체크)
		//			if (Status.LEFT) -> throw new CustomException(채팅방에 사용자가 존재X)
		// 			else -> return ChatRoomResponse

		else {
			// 최초입장
			// 1. 사용자 입장 처리 - 사용자 닉네임 반환해야 함
			memberDto = chatRoomService.enterChatRoom(roomId, user, nickname);
		}

		// 2. 사용자 입장 이벤트 생성
		UserEnteredChatRoomEvent event = new UserEnteredChatRoomEvent(roomId, memberDto.getNickname());

		// 3. 이 이벤트를 시스템에 알려주기 (발행)
		eventPublisher.publishEvent(event);


		// 4. 클라이언트 측 응답 객체 만들기 (nicknameMap + recentMessages)
		return makeChatRoomResponse(roomId, 0L);
	}

	public ChatRoomResponse makeChatRoomResponse(Long roomId, Long messageId) {
		// 1. 최근 메시지 조회
		List<ChatMessageDto> recentMessages = chatService.getRecentMessages(roomId, 10, messageId);

		// 2. 메시지에 등장한 senderId 목록 추출
		Set<Long> senderIds = recentMessages.stream()
				.map(ChatMessageDto::getUserId) // userId만 추출 (map은 변환)
				.collect(Collectors.toSet());

		// TODO : 후에 Redis 에 캐싱하는 리팩토링 고려
		// 3. nickname Map 생성
		List<NicknameDto> nicknameDtoList =
				chatRoomMemberRepository.findNicknamesByChatRoomIdAndUserIds(roomId, senderIds);

		Map<Long, String> nicknameMap = new HashMap<>();
		for (NicknameDto dto : nicknameDtoList) {
			nicknameMap.put(dto.getUserId(), dto.getNickname());
		}

		// 4. 닉네임이 없는 경우 기본값 처리 (예: 탈퇴자 / 예외)
		for (Long senderId : senderIds) {
			nicknameMap.putIfAbsent(senderId, "[알 수 없음]");
		}

		// 5. 응답 DTO 구성
		return new ChatRoomResponse(recentMessages, nicknameMap);
	}

	public List<JoinedRoomResponse> getJoinedRooms(Long userId) {
		List<ChatRoomMember> joinedRooms = chatRoomMemberRepository.findByUser_IdAndStatus(userId, ACTIVE);

		return joinedRooms.stream()
				.map(rooms -> {
					Long roomId = rooms.getChatRoom().getId();
					String keyword = rooms.getChatRoom().getKeyword().getKeywordName();
					int activeMemberCnt = chatRoomMemberRepository
							.countByChatRoom_IdAndStatus(roomId, ACTIVE);

					return new JoinedRoomResponse(roomId, keyword, activeMemberCnt);
				}).collect(Collectors.toList());
	}

}