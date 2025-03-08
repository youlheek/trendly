package com.rebootcrew.trendly.discussion.domain;

import com.rebootcrew.trendly.discussion.domain.enums.ChatRoomStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class ChatRoom {

	@Id
	@Column(nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	// TODO : keyword와 연관된 사항 정리
	// TODO : keyword 엔티티와 연결
	@Column(name = "keyword_id", nullable = false)
	private Long keywordId;

	@Enumerated(EnumType.STRING) // enum 을 문자열로 저장
	@Column(nullable = false)
	private ChatRoomStatus status = ChatRoomStatus.OPEN;
	// 채팅방이 생성될 때 OPEN 을 기본값으로 사용

	@Column(name = "created_at")
	private LocalDateTime createdAt;


	@OneToMany(mappedBy = "chatRoom")
	private List<ChatRoomMember> members = new ArrayList<>();

}
