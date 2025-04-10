package com.rebootcrew.trendly.discussion.domain.dto;

import lombok.Getter;

@Getter
public class UserEnteredChatRoomEvent {

	private final Long roomId;
	private final String nickname;

	public UserEnteredChatRoomEvent(Long roomId, String nickname) {
		this.roomId = roomId;
		this.nickname = nickname;
	}
}
