package com.rebootcrew.trendly.user.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rebootcrew.trendly.user.domain.KakaoTokenResponse;
import com.rebootcrew.trendly.user.domain.KakaoUserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class KakaoService {

	private final RestTemplate restTemplate;
	private final ObjectMapper objectMapper;

	@Value("${spring.security.oauth2.client.registration.kakao.client-id}")
	private String kakaoClientId;
	@Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}")
	private String redirectUri;

	private static final String TOKEN_URL = "https://kauth.kakao.com/oauth/token";
	private static final String USER_INFO_URL = "https://kapi.kakao.com/v2/user/me";


	// 2. 카카오 authorization code & access token 발급 요청
	public KakaoTokenResponse getAccessToken(String code) throws JsonProcessingException {

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("grant_type", "authorization_code");
		params.add("client_id", kakaoClientId);
		params.add("redirect_uri", redirectUri);
		params.add("code", code);

		// TODO : 에러처리
		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
		ResponseEntity<String> response =
				restTemplate.exchange(
						TOKEN_URL,
						HttpMethod.POST,
						request,
						String.class);

		return objectMapper.readValue(response.getBody(), KakaoTokenResponse.class);
	}

	// 3. 카카오로부터 회원 정보 받아오기
	public KakaoUserResponse getUserInfo(String accessToken) {

		// HTTP 헤더에 Bearer 토큰 추가
		HttpHeaders headers = new HttpHeaders();
		headers.setBearerAuth(accessToken);

		// HTTP 요청 생성
		HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

		// 사용자 정보 조회 API 호출 (응답을 String으로 받은 후 DTO로 변환)
		ResponseEntity<String> responseEntity =
				restTemplate.exchange(
						USER_INFO_URL,
						HttpMethod.GET,
						requestEntity,
						String.class);

		try {
//			return responseEntity.getBody();
			return objectMapper.readValue(responseEntity.getBody(), KakaoUserResponse.class);
		} catch (Exception e) {
			throw new RuntimeException("카카오 사용자 정보 조회에 실패했습니다.");
		}
	}
}
