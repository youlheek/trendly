package com.rebootcrew.trendly.discussion.controller;

import com.rebootcrew.trendly.discussion.domain.dto.ChatRoomDto;
import com.rebootcrew.trendly.common.respository.UserRepository;
import com.rebootcrew.trendly.discussion.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController // @Controller + @ResponseBody : Http 요청에 대해 JSON 이나 XML 같은 데이터를 직접 반환할 때 사용됨
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatController {

	private final ChatRoomService chatRoomService;
	private final UserRepository userRepository;

//	private final ChatWebsocket

	/**
	 * 채팅방 생성 및 상태 업데이트
	 * @param chatRoomDto
	 * @return
	 */
	@PostMapping("/rooms")
	public ResponseEntity<ChatRoomDto> createOrUpdateChatRoom(@RequestBody ChatRoomDto chatRoomDto) {
		return ResponseEntity.ok(chatRoomService.handleChatRoom(chatRoomDto));
	}



}
