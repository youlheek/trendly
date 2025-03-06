package com.rebootcrew.trendly.user.dto;

import com.rebootcrew.trendly.common.domain.User;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class UserDto {
	// TODO : 이렇게 정보를 때려박아도 되는거냐......?
	private Long id;
	private String email;
	private String nickname;

	private LocalDateTime birthDate;
	private String gender;

	private LocalDateTime createDate;
	private LocalDateTime modifiedDate;

	public static UserDto fromEntity(User user) {
		return UserDto.builder()
				.id(user.getId())
				.email(user.getEmail())
				.nickname(user.getNickname())
				.birthDate(user.getBirthDate())
				.gender(user.getGender())
				.createDate(user.getCreatedAt())
				.modifiedDate(user.getUpdatedAt())
				.build();
	}

}
