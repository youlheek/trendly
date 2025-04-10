package com.rebootcrew.trendly.discussion.domain.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class NicknameDto {
	private Long userId;
	private String nickname;

	public NicknameDto(Long userId, String nickname) {
		this.userId = userId;
		this.nickname = nickname;
	}
}
