package com.rebootcrew.trendly.discussion.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class ChatRoomResponse {
	private List<ChatMessageDto> chatMessageDto;
	private Map<Long, String> nicknameMap;

}
