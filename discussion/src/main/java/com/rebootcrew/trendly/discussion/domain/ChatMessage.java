package com.rebootcrew.trendly.discussion.domain;

import com.rebootcrew.trendly.common.domain.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class ChatMessage {

	@Id
	@Column(nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String message;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "chatRoom_id")
	private ChatRoom chatRoom;

	@Column(name = "created_at")
	private LocalDateTime createdAt;

	@Column(name = "deleted_at")
	private LocalDateTime deletedAt;

	@OneToMany(mappedBy = "message")
	private List<ChatComment> comments = new ArrayList<>();
}
