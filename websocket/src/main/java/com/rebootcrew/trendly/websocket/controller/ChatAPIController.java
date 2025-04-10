package com.rebootcrew.trendly.websocket.controller;

import com.rebootcrew.trendly.common.domain.User;
import com.rebootcrew.trendly.discussion.application.ChatApplication;
import com.rebootcrew.trendly.discussion.domain.dto.ChatRoomMemberDto;
import com.rebootcrew.trendly.common.exception.CustomException;
import com.rebootcrew.trendly.common.respository.UserRepository;
import com.rebootcrew.trendly.discussion.domain.dto.ChatRoomResponse;
import com.rebootcrew.trendly.discussion.domain.dto.NicknameDto;
import com.rebootcrew.trendly.discussion.service.ChatRoomService;
import com.rebootcrew.trendly.discussion.service.ChatService;
import com.rebootcrew.trendly.websocket.application.ChatWebSocketApplication;
import com.rebootcrew.trendly.websocket.dto.BroadcastMessageResponse;
import com.rebootcrew.trendly.discussion.domain.dto.JoinedRoomResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.rebootcrew.trendly.common.exception.ErrorCode.NOT_FOUND_USER;
import static com.rebootcrew.trendly.common.exception.ErrorCode.NO_MESSAGE_ID;

@RestController
@RequestMapping("/app")
@RequiredArgsConstructor
public class ChatAPIController {

	private final ChatRoomService chatRoomService;
	private final UserRepository userRepository;
	private final SimpMessagingTemplate messagingTemplate;
	private final ChatApplication chatApplication;


	/**
	 * 채팅방 입장
	 * @param roomId
	 * @param chatRoomMemberDto
	 * @param userDetails
	 * @return
	 */
	@PostMapping("/rooms/{roomId}/enter")
	public ChatRoomResponse enterChatRoom(
			@PathVariable Long roomId,
			@RequestBody(required = false) ChatRoomMemberDto chatRoomMemberDto,
			@AuthenticationPrincipal UserDetails userDetails
	) {
		// User 객체 추출
		User user = userRepository.findById(Long.parseLong(userDetails.getUsername()))
				.orElseThrow(() -> new CustomException(NOT_FOUND_USER, ""));
		String nickname = chatRoomMemberDto.getNickname() != null ? chatRoomMemberDto.getNickname() : null;

		return chatApplication.enterChatRoom(roomId, user, nickname);
	}

	/**
	 * 채팅방 메시지 더보기
	 * @param roomId
	 * @param messageId
	 * @return
	 */
	@GetMapping("/rooms/{roomId}/message.more")
	public ChatRoomResponse enterChatRoom(
			@PathVariable Long roomId,
			@RequestParam(required = false) Long messageId
	) {
		if (messageId == null) {
			throw new CustomException(NO_MESSAGE_ID);
		}
		return chatApplication.makeChatRoomResponse(roomId, messageId);
	}

	/**
	 * 참여 채팅방 목록
	 * @param userDetails
	 * @return
	 */
	@GetMapping("/rooms/my")
	public List<JoinedRoomResponse> userEnteredRoomList(
			@AuthenticationPrincipal UserDetails userDetails
	) {
		// User 객체 추출
		User user = userRepository.findById(Long.parseLong(userDetails.getUsername()))
				.orElseThrow(() -> new CustomException(NOT_FOUND_USER, ""));
		return chatApplication.getJoinedRooms(user.getId());
	}

	/**
	 * 닉네임 수정
	 * @param roomId
	 * @param userDetails
	 * @param chatRoomMemberDto
	 * @return
	 */
	@PatchMapping("/rooms/{roomId}/nickname.update")
	public NicknameDto updateNickname(
			@PathVariable Long roomId,
			@AuthenticationPrincipal UserDetails userDetails,
			@RequestBody ChatRoomMemberDto chatRoomMemberDto
	) {

		return chatRoomService.changeNickname(roomId, chatRoomMemberDto.getUserId(), chatRoomMemberDto.getNickname());
	}

	/**
	 * 채팅방 나가기
	 * @param roomId
	 * @param chatRoomMemberDto
	 * @param userDetails
	 * @return
	 */
	@PatchMapping("/rooms/{roomId}/left")
	public ResponseEntity<?> leftChatRoom(@PathVariable Long roomId,
										  @RequestBody ChatRoomMemberDto chatRoomMemberDto,
										  @AuthenticationPrincipal UserDetails userDetails) {
		// User 객체 추출
		User user = userRepository.findById(Long.parseLong(userDetails.getUsername()))
				.orElseThrow(() -> new CustomException(NOT_FOUND_USER, ""));

		// 사용자 퇴장 처리
		chatRoomService.leftChatRoom(roomId, user);

		// 퇴장 알림 브로드캐스트
		messagingTemplate.convertAndSend(
				"/topic/rooms/" + roomId,
				new BroadcastMessageResponse("USER_LEFTED", chatRoomMemberDto.getNickname() + "님이퇴장하셨습니다.")
		);

		return ResponseEntity.ok("채팅방 나가기 성공");
	}
}

