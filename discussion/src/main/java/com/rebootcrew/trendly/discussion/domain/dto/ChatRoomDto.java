package com.rebootcrew.trendly.discussion.domain.dto;

import com.rebootcrew.trendly.common.domain.enums.ChatRoomStatus;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ChatRoomDto {
	private Long keywordId;
	private ChatRoomStatus status;

}
