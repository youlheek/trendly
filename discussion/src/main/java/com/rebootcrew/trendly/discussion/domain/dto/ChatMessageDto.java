package com.rebootcrew.trendly.discussion.domain.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Builder
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ChatMessageDto {
	private Long messageId;
	private String message;
	private Long userId;
	private String nickname;
	private Long roomId;
	private LocalDateTime createdAt;
//	private int likeCount;

}
