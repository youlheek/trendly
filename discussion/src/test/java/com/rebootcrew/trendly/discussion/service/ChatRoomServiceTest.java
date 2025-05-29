package com.rebootcrew.trendly.discussion.service;

import com.rebootcrew.trendly.common.domain.User;
import com.rebootcrew.trendly.discussion.domain.dto.ChatRoomDto;
import com.rebootcrew.trendly.discussion.domain.dto.ChatRoomMemberDto;
import com.rebootcrew.trendly.common.domain.enums.ChatRoomStatus;
import com.rebootcrew.trendly.discussion.domain.ChatRoom;
import com.rebootcrew.trendly.discussion.domain.ChatRoomMember;
import com.rebootcrew.trendly.discussion.repository.ChatRoomRepository;
import com.rebootcrew.trendly.domain.Keyword;
import com.rebootcrew.trendly.trending.repository.KeywordRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ChatRoomServiceTest {

	// @InjectMocks : Mockito가 이 객체에 가짜 의존성을 자동으로 넣어줌
	@InjectMocks
	private ChatRoomService chatRoomService;

	// @Mock : 가짜 객체(들)
	@Mock
	private KeywordRepository keywordRepository;

	@Mock
	private ChatRoomRepository chatRoomRepository;

	@Mock
	private SimpMessagingTemplate simpMessagingTemplate;

	@BeforeEach // : 테스트가 실행되기 전에 항상 실행되는 준비 작업을 정의하는 부분
	void setUp() {
		MockitoAnnotations.openMocks(this);// Mockito 초기화
	}


	// 1. 기존 채팅방이 있고 상태가 변경되는 경우 : 상태 업데이트와 알림 호출 확인
	@Test
	void testHandleChatRoom_UpdateExistingRoom() {
		// Given : 입력 데이터
		ChatRoomDto chatRoomDto = ChatRoomDto.builder()
				.keywordId(1l)
				.status(ChatRoomStatus.CLOSE)
				.build();

		Keyword keyword = new Keyword();
		keyword.setId(1l);
		when(keywordRepository.findById(1L)).thenReturn(Optional.of(keyword));
		// : keywordRepository 가짜 객체에게 "ID가 1인 키워드를 찾으면 keyword를 반환해"라고 지시합니다.
		// Optional.of는 값이 있다는 뜻이에요.

		ChatRoom existingRoom = ChatRoom.builder()
				.id(1L)
				.keyword(keyword)
				.status(ChatRoomStatus.OPEN)
				.createdAt(LocalDateTime.now())
				.members(new ArrayList<>())
				.build();

		when(chatRoomRepository.findById(1L)).thenReturn(Optional.of(existingRoom));
		// : chatRoomRepository 가짜 객체에게 "키워드 ID가 1인 채팅방을 찾으면 existingRoom을 반환해"라고 지시합니다.
		when(chatRoomRepository.save(any(ChatRoom.class))).thenReturn(existingRoom);
		// : chatRoomRepository가 채팅방을 저장하면 existingRoom을 반환하도록 설정합니다.
		// any(ChatRoom.class)는 어떤 ChatRoom 객체라도 상관없다는 뜻이에요.

		// When
//		ChatRoomDto result = chatRoomService.handleChatRoom(chatRoomDto);
//
//		// Then
//		assertNotNull(result);
//		assertEquals(1L, result.getKeywordId());
//		assertEquals(ChatRoomStatus.CLOSE, result.getStatus());
//		verify(chatRoomRepository, times(1))
//				.save(existingRoom);
//		verify(simpMessagingTemplate, times(1))
//				.convertAndSend(eq("/topic/rooms/1"), eq("Status changed to CLOSE"));
//		verify(simpMessagingTemplate, never()).convertAndSend(eq("/topic/rooms"),
//				anyString());

	}

	// 2. 기존 채팅방이 없고 새로 생성되는 경우 OPEN 상태 : 채팅방 생성과 알림 호출 확인

	// 3. 기존 채팅방이 없고 CLOSE 상태로 생성하려는 경우 : 예외 발생 확인

	// 4. Keyword 가 존재하지 않는 경우 : 예외 발생 확인


	// 1. 정상적으로 사용자가 채팅방에 입장하는 경우
	@Test
	void testEnterChatRoom_Success() {

		// Given
		// 사용자 임의 설정
		Long roomId = 1L;
		User user = new User();
		user.setId(1L);

		// ChatRoomMember 값 임의 설정
		ChatRoomMemberDto chatRoomMemberDto = new ChatRoomMemberDto();
		chatRoomMemberDto.setNickname("testUser");

		// keyword 값 임의 설정
		Keyword keyword = new Keyword();
		keyword.setId(1L);

		// ChatRoom 값 임의 설정
		ChatRoom chatRoom = ChatRoom.builder()
				.id(roomId)
				.keyword(keyword)
				.status(ChatRoomStatus.OPEN)
				.createdAt(LocalDateTime.now())
				.members(new ArrayList<>())
				.build();

		//
		when(chatRoomRepository.findChatRoomById(roomId)).thenReturn(Optional.of(chatRoom));

		// When
//		ChatRoomDto result = chatRoomService.enterChatRoom(roomId, user, chatRoomMemberDto);

		// Then
//		assertNotNull(result);
//		assertEquals(1L, result.getKeywordId());
//		assertEquals(ChatRoomStatus.OPEN, result.getStatus());

		// chatRoom의 멤버 리스트에 추가되었는지 확인
		assertEquals(1, chatRoom.getMembers().size());
		ChatRoomMember addedMember = chatRoom.getMembers().get(0);

		assertEquals("testUser", addedMember.getNickname());
		assertEquals(user, addedMember.getUser());
		assertNotNull(addedMember.getJoinedAt());
	}

}