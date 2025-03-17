package com.rebootcrew.trendly.user.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
	private Long id;
	private String email;
	private String gender;
	private LocalDate birthDate;
	private boolean marketingOpt;

	public static UserResponse fromDto(UserDto userDto) {
		return UserResponse.builder()
				.id(userDto.getId())
				.email(userDto.getEmail())
				.gender(userDto.getGender())
				.birthDate(userDto.getBirthDate())
				.marketingOpt(userDto.isMarketingOpt())
				.build();
	}
}
