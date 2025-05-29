package com.rebootcrew.trendly.websocket.dto;

import lombok.Data;

public class ChatLikeReqeust {
	// websocket/dto/ChatLikeRoomRequest.java
	@Data
	public class ChatLikeRoomRequest {
		private Long userId;   // 어떤 유저가
		private boolean like;  // true=좋아요 / false=취소
	}

	@Data
	public class ChatLikeMessageRequest {
		private Long userId;
		private Long messageId;
		private boolean like;
	}

	@Data
	public class ChatLikeReplyRequest {
		private Long userId;
		private Long replyId;
		private boolean like;
	}

}
