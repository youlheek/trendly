package com.rebootcrew.trendly.websocket.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BroadcastMessageResponse {
	private String eventType; // 예: LIKE_ROOM, UNLIKE_ROOM, LIKE_MESSAGE, etc.
	private Object data;      // 실제 업데이트된 엔티티 DTO}
}