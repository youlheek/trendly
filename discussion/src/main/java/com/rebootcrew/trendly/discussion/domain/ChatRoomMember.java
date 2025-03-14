package com.rebootcrew.trendly.discussion.domain;

import com.rebootcrew.trendly.common.domain.User;
import com.rebootcrew.trendly.discussion.domain.enums.ChatRoomMemeberStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Builder
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class ChatRoomMember {

	@Id
	@Column(nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String nickname;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "chatRoom_id")
	private ChatRoom chatRoom;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private ChatRoomMemeberStatus status = ChatRoomMemeberStatus.ACTIVE;
	// 채팅방에 들어갈 때 ACTIVE 를 기본값으로 사용

	@Column(name = "joined_at")
	private LocalDateTime joinedAt;


	public ChatRoomMember(User user, ChatRoom chatRoom, String nickname) {
		this.user = user;
		this.chatRoom = chatRoom;
		this.nickname = nickname;
		this.joinedAt = LocalDateTime.now();
	}
}
