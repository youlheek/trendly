package com.rebootcrew.trendly.common.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Date;

@Component
public class JwtTokenProvider {
	// jwt 토큰 구현

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

	// access token 생성
	public String generateAccessToken(String email) {
		return generateToken(email, accessTokenExpiration);
	}

	// refresh token 생성
	public String generateRefreshToken(String email) {
		return generateToken(email, refreshTokenExpiration);
	}

	// 공통 토큰 생성 로직
	private String generateToken(String email, Long expirationTime){
		Claims claims = Jwts.claims()
				.setSubject(email);

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
			// 토큰 파싱 시도 (유효하지 않은 토큰이면 예외 발생)
			Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
			return true;
		} catch (Exception e) {
			// TODO : Exceptions 처리
			// 토큰이 만료되었거나, 서명이 올바르지 않거나, 기타 문제가 있는 경우 false 반환
			return false;
		}
	}

	// 토큰 만료시간 가져오기
	public long getExpiration(String token) {
		return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getExpiration().getTime();
	}

	// Spring Security에 등록할 Authentication 객체 생성
	public Authentication getAuthentication(String token) {
		Claims claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();

		UserDetails userDetails = new User(claims.getSubject(),"", Collections.emptyList());
		return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
	}

}
