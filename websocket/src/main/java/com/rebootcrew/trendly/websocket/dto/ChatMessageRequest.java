package com.rebootcrew.trendly.websocket.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ChatMessageRequest {
	private Long messageId;
	@NotBlank(message = "메시지를 입력해주세요.")
	@Size(max = 255, message = "메시지는 255자 이내로 입력해주세요.")
	private String message;
	@NotNull(message = "사용자 ID는 필수입니다.")
	private Long userId;
	private String nickname;
}
