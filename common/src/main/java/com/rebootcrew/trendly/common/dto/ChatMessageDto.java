package com.rebootcrew.trendly.common.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ChatMessageDto {
	private Long id;
	private String message;
	private Long userId;
	private String nickname;
	private Long roomId;
	private LocalDateTime createdAt;
	private int likeCount;

}
