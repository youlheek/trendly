package com.rebootcrew.trendly.user.service;

import com.rebootcrew.trendly.common.config.JwtTokenProvider;
import com.rebootcrew.trendly.common.domain.User;
import com.rebootcrew.trendly.common.exception.CustomException;
import com.rebootcrew.trendly.common.exception.ErrorCode;
import com.rebootcrew.trendly.common.respository.UserRepository;
import com.rebootcrew.trendly.user.domain.KakaoUserResponse;
import com.rebootcrew.trendly.user.domain.SignUpForm;
import com.rebootcrew.trendly.user.dto.AuthResponse;
import com.rebootcrew.trendly.user.dto.UserDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.data.redis.RedisSystemException;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

	private final UserRepository userRepository;
	private final JwtTokenProvider jwtTokenProvider;
	private final StringRedisTemplate redisTemplate;

	private static final String BLACKLIST_PREFIX = "blacklist:";


	/**
	 * 카카오 로그인 처리 (토큰 발급)
	 * @param user
	 * @return AuthResponse (JWT 포함)
	 */
	public AuthResponse loginUser(User user) {
		// jwt 토큰을 발급해야함
		// 그리고 User 객체를 돌려줌

		return generateAuthResponse(user);
	}

	// 회원가입 처리
	@Transactional
	public AuthResponse signUpUser(KakaoUserResponse userInfo){
		SignUpForm form = SignUpForm.from(userInfo);
		User user = userRepository.save(form.toUser());
		System.out.println("회원가입이 완료되었습니다.");

		return generateAuthResponse(user);
	}

	/**
	 * JWT 토큰 발급 응답 생성
	 * @param user 저장된 사용자 정보
	 * @return AuthResponse (JWT 포함)
	 */
	private AuthResponse generateAuthResponse(User user) {
		String accessToken = jwtTokenProvider.generateAccessToken(user.getEmail());
		String refreshToken = jwtTokenProvider.generateRefreshToken(user.getEmail());
		Long accessTokenExpiresIn = jwtTokenProvider.getExpiration(accessToken);
		Long refreshTokenExpiresIn = jwtTokenProvider.getExpiration(refreshToken);

		return AuthResponse.builder()
				.accessToken(accessToken)
				.refreshToken(refreshToken)
				.accessTokenExpiresIn(accessTokenExpiresIn)
				.refreshTokenExpiresIn(refreshTokenExpiresIn)
				.tokenType("Bearer")
				.user(UserDto.fromEntity(user))
				.build();
	}

	/**
	 * 내부 토큰 무효화 처리
	 * 실제 구현에서는 데이터베이스, 캐시(Redis) 또는 블랙리스트 등록 등을 수행
	 * @param token 클라이언트가 보유한 우리 시스템의 토큰
	 */
	public void invalidateTokens(String token, long expiration) {
		// 예시: 토큰 만료 또는 삭제 처리 로직
		long now = System.currentTimeMillis();
		long ttl = (expiration - now) / 1000; // 초 단위 변환

		if (ttl <= 0) {
			log.warn("⚠️ 블랙리스트 추가 실패 - 토큰이 이미 만료됨: {}", token);
			return;
		}

		try {
			redisTemplate.opsForValue().set(BLACKLIST_PREFIX + token, "true");
		} catch (RedisConnectionFailureException | RedisSystemException e) {
			log.error("❌ Redis 오류 - 블랙리스트 추가 실패: {}", e.getMessage());
			throw new CustomException(ErrorCode.JWT_BLACKLIST_FAIL);
		}

		System.out.println("내부 토큰 만료 처리: " + token);
	}

	/**
	 * 블랙리스트에 있는지 확인 (로그아웃된 토큰인지 체크)
	 * @param token 확인할 JWT 액세스 토큰
	 * @return 블랙리스트 여부
	 */
	public boolean isInvalidatedToken(String token) {
		try {
			return redisTemplate.hasKey(BLACKLIST_PREFIX + token);
		} catch (RedisConnectionFailureException | RedisSystemException e) {
			log.error("❌ Redis 오류 - 블랙리스트 조회 실패: {}", e.getMessage());
			throw new CustomException(ErrorCode.REDIS_ERROR);
		}
	}
}
