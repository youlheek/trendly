package com.rebootcrew.trendly.common.domain;

import com.rebootcrew.trendly.common.domain.enums.LikeType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class Likes {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	@Column(nullable = false)
	private Long targetId;

	@Enumerated(EnumType.STRING)
	private LikeType targetType;

	private boolean isLiked = Boolean.TRUE;
}
