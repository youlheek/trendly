package com.rebootcrew.trendly.discussion.domain;

import com.rebootcrew.trendly.common.domain.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Builder
@Getter @Setter
@NoArgsConstructor@AllArgsConstructor
public class ChatComment {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String comment;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "message_id")
	private ChatMessage message;

	@Column(name ="created_at")
	private LocalDateTime createdAt;
}
