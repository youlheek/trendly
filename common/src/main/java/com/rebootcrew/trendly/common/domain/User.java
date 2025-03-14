package com.rebootcrew.trendly.common.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.Type;
import org.hibernate.envers.AuditOverride;
import org.hibernate.type.StandardBasicTypes;

import javax.lang.model.util.Types;
import java.time.LocalDate;
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
	private Long id;

	@Column(name = "kakao_user_id")
	private Long kakaoUserId;

	@Column(unique = true, nullable = false)
	private String email;
	private String password;

	@Column(name = "birth_date")
	private LocalDate birthDate;
	private String gender;

	@Column(name = "marketing_opt")
	private boolean marketingOpt; // 마케팅 정보 동의

	@Column(name = "deleted_at")
	private LocalDateTime deletedAt;


	public static User from (UserForm form) {
		return User.builder()
				.birthDate(form.getBirthDate())
				.gender(form.getGender())
				.marketingOpt(form.isMarketingOpt())
				.build();
	}
}
