package com.rebootcrew.trendly.common.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.envers.AuditOverride;

@Entity
@Builder
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@AuditOverride
public class Likes extends BaseEntity {
	@Id
	@Column(name = "id", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "user_id", nullable = false)
	private Long userId;
	@Column(name = "target_id", nullable = false)
	private	Long targetId;
	@Column(name = "target_type", nullable = false)
	private String targetType;

	@Column(name = "is_liked", nullable = false)
	private Boolean isLiked;
}
