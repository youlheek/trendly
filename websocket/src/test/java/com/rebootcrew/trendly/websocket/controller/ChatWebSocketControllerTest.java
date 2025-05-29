package com.rebootcrew.trendly.websocket.controller;

import com.rebootcrew.trendly.common.domain.User;
import com.rebootcrew.trendly.discussion.domain.dto.ChatRoomMemberDto;
import com.rebootcrew.trendly.common.respository.UserRepository;
import com.rebootcrew.trendly.discussion.service.ChatRoomService;
import com.rebootcrew.trendly.websocket.application.ChatWebSocketApplication;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class ChatWebSocketControllerTest {

	@InjectMocks
	private ChatWebSocketController chatWebSocketController;

	@Mock
	private ChatWebSocketApplication chatWebSocketApplication;

	@Mock
	private UserRepository userRepository;

	@Mock
	private ChatRoomService chatRoomService;

	@Mock
	private UserDetails userDetails;

	@Mock
	private SimpMessagingTemplate messagingTemplate; // 추가: SimpMessagingTemplate 모킹

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this); // Mockito 초기화
	}


	@Test
	void testEnterChatRoom_Success() {
		// Given
		Long chatRoomId = 1L;
		ChatRoomMemberDto chatRoomMemberDto = new ChatRoomMemberDto();
		chatRoomMemberDto.setNickname("testUser");


		when(userDetails.getUsername()).thenReturn("1");
		User user = new User();
		user.setId(1L);
		when(userRepository.findById(1L)).thenReturn(Optional.of(user));

		// handleEnterChatRoom 호출시 아무 동작도 하지 않도록 모킹
		doNothing().when(chatWebSocketApplication).handleEnterChatRoom(any(), any(), any());

		// when
		chatWebSocketController.enterChatRoom(chatRoomId, chatRoomMemberDto, userDetails);

		// then
		verify(chatWebSocketApplication, times(1))
				.handleEnterChatRoom(eq(chatRoomId), eq(user), eq(chatRoomMemberDto));

	}

}