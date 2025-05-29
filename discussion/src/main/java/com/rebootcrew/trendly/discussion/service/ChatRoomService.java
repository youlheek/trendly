package com.rebootcrew.trendly.discussion.service;

import com.rebootcrew.trendly.common.domain.User;
import com.rebootcrew.trendly.common.domain.enums.ChatRoomStatus;
import com.rebootcrew.trendly.discussion.domain.dto.ChatRoomDto;
import com.rebootcrew.trendly.common.exception.CustomException;
import com.rebootcrew.trendly.discussion.domain.ChatRoom;
import com.rebootcrew.trendly.discussion.domain.ChatRoomMember;
import com.rebootcrew.trendly.discussion.domain.dto.ChatRoomMemberDto;
import com.rebootcrew.trendly.discussion.domain.dto.NicknameDto;
import com.rebootcrew.trendly.discussion.domain.service.ChatRoomValidator;
import com.rebootcrew.trendly.discussion.repository.ChatRoomMemberRepository;
import com.rebootcrew.trendly.discussion.repository.ChatRoomRepository;
import com.rebootcrew.trendly.domain.Keyword;
import com.rebootcrew.trendly.trending.repository.KeywordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.rebootcrew.trendly.common.exception.ErrorCode.*;
import static com.rebootcrew.trendly.discussion.domain.enums.ChatRoomMemeberStatus.ACTIVE;
import static com.rebootcrew.trendly.discussion.domain.enums.ChatRoomMemeberStatus.LEFT;

@Service
@RequiredArgsConstructor
public class ChatRoomService {

	private final KeywordRepository keywordRepository;
	private final ChatRoomRepository chatRoomRepository;
	private final SimpMessagingTemplate messagingTemplate; // 다른곳(예: 웹 브라우저)으로 메시지를 보내는 도구
	private final ChatRoomMemberRepository chatRoomMemberRespository;
	private final ChatRoomValidator chatRoomValidator;
	private final ChatRoomMemberRepository chatRoomMemberRepository;


	/**
	 * 채팅방 초기 생성 및 상태 변경
	 *
	 * @param chatRoomDto
	 * @return
	 */
	public ChatRoomDto handleChatRoom(ChatRoomDto chatRoomDto) {
		// Keyword 존재 확인
		Keyword keyword = keywordRepository.findById(chatRoomDto.getKeywordId())
				.orElseThrow(() -> new IllegalArgumentException("Keyword not found"));

		// 기존 채팅방 확인
		Optional<ChatRoom> existingRoom =
				chatRoomRepository.findByKeywordId(chatRoomDto.getKeywordId());
		ChatRoom chatRoom;

		if (existingRoom.isPresent()) {
			// 잔존 키워드: 상태만 업데이트
			chatRoom = existingRoom.get();
			if (chatRoom.getStatus() != chatRoomDto.getStatus()) {
				chatRoom.setStatus(chatRoomDto.getStatus());
				chatRoomRepository.save(chatRoom);
				notifyRoomUpdate(chatRoom.getId(), "Status changed to " + chatRoomDto.getStatus());
			}
		} else if (chatRoomDto.getStatus() == ChatRoomStatus.OPEN) {
			// 신규 키워드: 채팅방 생성
			chatRoom = ChatRoom.builder()
					.keyword(keyword)
					.status(ChatRoomStatus.OPEN)
					.createdAt(LocalDateTime.now())
					.members(new ArrayList<>())
					.build();
			chatRoomRepository.save(chatRoom);
			notifyRoomCreation(chatRoom.getId());
		} else {
			throw new IllegalStateException("Cannot create room with CLOSE status for new keyword");
		}

		return ChatRoomDto.builder()
				.keywordId(chatRoom.getKeyword().getId())
				.status(chatRoom.getStatus())
				.build();
	}

	private void notifyRoomCreation(Long roomId) {
		// 채팅방 생성 알림
		// url 은 추후 변경 가능성 있음
		messagingTemplate.convertAndSend("/topic/rooms", "New room created: " + roomId);
	}

	private void notifyRoomUpdate(Long roomId, String message) {
		// 채팅방 상태 업데이트 알림
		messagingTemplate.convertAndSend("/topic/chat/" + roomId, message);
	}

	/**
	 * 사용자가 채팅방에 최초입장 & LEFT 뒤 재입장
	 * @param roomId
	 * @param user
	 * @return ChatRoomDto(nickname)
	 */
	public ChatRoomMemberDto enterChatRoom(Long roomId, User user, String nickname) {
		// 1. 채팅방이 존재하는지 확인 (상태 확인)
		ChatRoom chatRoom = chatRoomRepository.findChatRoomById(roomId)
				.orElseThrow(() -> new CustomException(NOT_FOUND_ROOM, ""));

		// 2. 사용자가 이미 입장했는지 중복 체크
		ChatRoomMember member = chatRoomMemberRepository.findByChatRoom_IdAndUser_Id(roomId, user.getId());

		if (member != null && member.getStatus() == LEFT) {
			member.setStatus(ACTIVE);
			member.setNickname(nickname);
			return ChatRoomMemberDto.builder()
					.id(member.getId())
					.chatRoomId(member.getChatRoom().getId())
					.userId(member.getUser().getId())
					.nickname(member.getNickname())
					.build();
		} else if (member != null && member.getStatus() == ACTIVE) {
			// 여기 있으면 안됨 -> 이미 Application 단에서 처리되었어야 하는 건데 왜 여기까지 왔지?
			throw new CustomException(ALREADY_JOINED, "");
		} else {
			// 3. 사용자 닉네임 중복 체크
			boolean alreadyUsedNickname =
					chatRoomValidator.validateDuplicateNickname(roomId, nickname);
//				chatRoom.getMembers().stream()
//				.anyMatch(member -> member.getNickname().equals(nickname) &&
//						member.getStatus() == ChatRoomMemeberStatus.ACTIVE);
			if (alreadyUsedNickname) {
				throw new CustomException(ALREADY_USED_NICKNAME, "");
			}
			if (nickname.length() > 20) {
				throw new CustomException(NICKNAME_TOO_LONG, "");
			}


			// 4. 사용자를 채팅방 멤버로 저장
			// 새 ChatRoomMember 생성
			ChatRoomMember newMember = ChatRoomMember.builder()
					.nickname(nickname)
					.chatRoom(chatRoom)
					.user(user)
					.status(ACTIVE)
					.joinedAt(LocalDateTime.now())
					.build();

			// 채팅방의 멤버 목록에 추가 및 저장
			chatRoom.getMembers().add(newMember);
			chatRoomMemberRespository.save(newMember);
			return ChatRoomMemberDto.builder()
					.id(newMember.getId())
					.chatRoomId(newMember.getChatRoom().getId())
					.userId(newMember.getUser().getId())
					.nickname(newMember.getNickname())
					.build();
		}
	}

	@Transactional
	public void leftChatRoom(Long roomId, User user) {
		ChatRoomMember member =
				chatRoomMemberRepository.findByChatRoom_IdAndUser_IdAndStatus(roomId, user.getId(), ACTIVE)
						.orElseThrow(() -> new CustomException(NOT_FOUND_ACTIVE_CHAT_ROOM_MEMBER, ""));
		member.setStatus(LEFT);
	}

	@Transactional
	public void leftAllChatRoom(Long userId) {
		List<ChatRoomMember> member =
				chatRoomMemberRepository.findByUser_IdAndStatus(userId, ACTIVE);
		if (member == null || member.isEmpty()) {
			return; // 아무것도 하지 않음
		}
		member.stream().forEach(m -> m.setStatus(LEFT));
	}

	// Nickname 변경
	@Transactional
	public NicknameDto changeNickname(Long roomId, Long userId, String nickname) {

		// 1) 채팅방에 ACTIVE 인 User가 존재하는지 확인
		ChatRoomMember chatRoomMember = chatRoomMemberRepository
				.findByChatRoom_IdAndUser_IdAndStatus(roomId, userId, ACTIVE)
				.orElseThrow(() -> new CustomException(NOT_FOUND_ACTIVE_CHAT_ROOM_MEMBER, ""));


		// 2) 채팅방 내 닉네임 중복체크
		boolean alreadyUsedNickname = chatRoomValidator.validateDuplicateNickname(roomId, nickname);
		if (alreadyUsedNickname) {
			throw new CustomException(ALREADY_USED_NICKNAME, "");
		} else if (nickname.length() > 20) {
			throw new CustomException(NICKNAME_TOO_LONG, "");
		}

		// 3) 닉네임 저장
		chatRoomMember.setNickname(nickname);
		return NicknameDto.builder()
				.userId(userId)
				.nickname(nickname)
				.build();
	}
}
