package com.rebootcrew.trendly.common.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.envers.AuditOverride;

import java.time.LocalDateTime;

@Entity
@Builder
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@AuditOverride
public class User extends BaseEntity {
	@Id
	@Column(name = "id", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column(unique = true, nullable = false)
	private String email;
	@Column(nullable = false)
	private String password;

	@Column(name = "birth_date")
	private LocalDateTime birthDate;
	private String gender;

}
