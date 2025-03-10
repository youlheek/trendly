package com.rebootcrew.trendly.user.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
	private Long id;
	private String email;
	private String gender;
	private LocalDate birthdate;
	private boolean marketingOpt;

	public static UserResponse fromDto(UserDto userDto) {
		return UserResponse.builder()
				.id(userDto.getId())
				.email(userDto.getEmail())
				.gender(userDto.getGender())
				.birthdate(userDto.getBirthDate())
				.marketingOpt(userDto.isMarketingOpt())
				.build();
	}
}
