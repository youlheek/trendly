package com.rebootcrew.trendly.discussion.domain;

import com.rebootcrew.trendly.common.domain.enums.ChatRoomStatus;
import com.rebootcrew.trendly.domain.Keyword;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "chat_rooms")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class ChatRoom {

	@Id
	@Column(nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "keyword_id", nullable = false)
	private Keyword keyword;

	@Enumerated(EnumType.STRING) // enum 을 문자열로 저장
	@Column(nullable = false)
	private ChatRoomStatus status = ChatRoomStatus.OPEN;
	// 채팅방이 생성될 때 OPEN 을 기본값으로 사용

	@Column(name = "created_at")
	private LocalDateTime createdAt;


	@OneToMany(mappedBy = "chatRoom")
	private List<ChatRoomMember> members = new ArrayList<>();

}
