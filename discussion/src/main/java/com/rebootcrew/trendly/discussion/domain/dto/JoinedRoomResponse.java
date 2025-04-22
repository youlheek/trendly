package com.rebootcrew.trendly.discussion.domain.dto;

import lombok.Getter;

@Getter
public class JoinedRoomResponse {
	private Long roomId;
	private String keyword;
	private int participantCount;

	public JoinedRoomResponse(Long roomId, String keyword, int participantCount) {
		this.roomId = roomId;
		this.keyword = keyword;
		this.participantCount = participantCount;
	}

	// Getter, Setter
}
