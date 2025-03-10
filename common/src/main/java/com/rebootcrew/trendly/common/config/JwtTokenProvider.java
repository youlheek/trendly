package com.rebootcrew.trendly.common.config;

import com.rebootcrew.trendly.common.exception.CustomException;
import com.rebootcrew.trendly.common.exception.ErrorCode;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.data.redis.RedisSystemException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Date;
import java.util.List;

@Slf4j
@Component
public class JwtTokenProvider {

	@Value("${jwt.secret}")
	private String secretKey;

	// access token 유효시간
	@Value("${jwt.access-token.expiration}")
	private Long accessTokenExpiration;
	// 운영서버 (30분) 1000L * 60 * 30 * 1
	// 개발서버 (30초) 1000L * 30

	// refresh token 유효시간
	@Value("${jwt.refresh-token.expiration}")
	private Long refreshTokenExpiration;
	// 운영서버 (24시간) 1000L * 60 * 60 * 24
	// 개발서버 (1분) 1000L * 60

	private final RedisTemplate redisTemplate;
	private static final String BLACKLIST_PREFIX = "blacklist:";

	public JwtTokenProvider(RedisTemplate redisTemplate) {
		this.redisTemplate = redisTemplate;
	}


	// access token 생성
	public String generateAccessToken(Long userId) {
		return generateToken(userId, accessTokenExpiration);
	}

	// refresh token 생성
	public String generateRefreshToken(Long userId) {
		return generateToken(userId, refreshTokenExpiration);
	}

	// 공통 토큰 생성 로직
	private String generateToken(Long userId, Long expirationTime) {
		Claims claims = Jwts.claims()
				.setSubject(String.valueOf(userId));

		Date now = new Date();

		return Jwts.builder()
				.setClaims(claims)
				.setIssuedAt(now)
				.setExpiration(new Date(now.getTime() + expirationTime))
				.signWith(SignatureAlgorithm.HS256, secretKey)
				.compact();
	}

	// 토큰 유효성 검증
	public boolean validateToken(String token) {
		try {
			// 블랙리스트 체크
			if (redisTemplate.hasKey(BLACKLIST_PREFIX + token)) {
				throw new BadCredentialsException("Invalid token");  // AuthenticationException 하위 클래스
			}
			// 만료된 토큰인 경우 ExpiredJwtException 발생
			Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
			return true;
		} catch (RedisConnectionFailureException | RedisSystemException e) {
			log.error("❌ Redis 오류 - 블랙리스트 조회 실패: {}", e.getMessage());
			throw new CustomException(ErrorCode.REDIS_ERROR);
		} catch (ExpiredJwtException e) {
			throw new BadCredentialsException("Invalid token");  // AuthenticationException 하위 클래스
		}
	}

	// 토큰 만료시간 가져오기
	public long getExpiration(String token) {
		return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getExpiration().getTime();
	}

	// Spring Security에 등록할 Authentication 객체 생성
	public Authentication getAuthentication(String token) {
		Claims claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
		String userId = claims.getSubject();

		// subject 값이 null 또는 빈 문자열인지 체크
		if (userId == null || userId.trim().isEmpty()) {
			throw new CustomException(ErrorCode.INVALID_TOKEN);
		}

		String role = claims.get("role", String.class); // 역할 정보 추출 (없으면 null)
		List<SimpleGrantedAuthority> authorities = (role != null)
				? Collections.singletonList(new SimpleGrantedAuthority(role))
				: Collections.emptyList();

		UserDetails userDetails = new User(userId, "", authorities);
		return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
	}

	public Claims getClaims(String token) {
		return Jwts.parser()
				.setSigningKey(secretKey)
				.parseClaimsJws(token)
				.getBody();
	}

}
