package com.rebootcrew.trendly.user.domain;

import com.rebootcrew.trendly.common.domain.User;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SignUpForm {
	private String email;
	private String nickname;

	// from 메서드 : 외부 데이터를 받아서 객체를 생성하는 팩토리 메서드로 사용되기 때문에 static으로 선언
	public static SignUpForm from(KakaoUserResponse user) {
		return SignUpForm.builder()
				.email(user.getKakaoAccount().getEmail())
				.nickname(user.getProperties().getNickname())
				.build();
	}

	// 변환 메서드 : SignUpForm -> User
	// 이미 만들어진 SignUpForm 인스턴스의 필드 값을 바탕으로 User 엔티티를 생성하는 인스턴스 메서드
	public User toUser() {
		return User.builder()
				.email(this.email)
				.nickname(this.nickname)
				.build();
	}
}
