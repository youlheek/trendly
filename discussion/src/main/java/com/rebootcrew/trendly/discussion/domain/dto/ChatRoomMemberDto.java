package com.rebootcrew.trendly.discussion.domain.dto;

import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ChatRoomMemberDto {
	private Long id;
	@Size(max = 20, message = "닉네임은 20자 이내여야 합니다.")
	private String nickname;
	private Long chatRoomId;
	private Long userId;
//	private ChatRoomMemberStatus
}
