package com.rebootcrew.trendly.user.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rebootcrew.trendly.common.exception.CustomException;
import com.rebootcrew.trendly.common.exception.ErrorCode;
import com.rebootcrew.trendly.user.domain.KakaoTokenResponse;
import com.rebootcrew.trendly.user.domain.KakaoUserResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
@RequiredArgsConstructor
public class KakaoService {

	private final RestTemplate restTemplate;
	private final ObjectMapper objectMapper;

	@Value("${spring.security.oauth2.client.registration.kakao.client-id}")
	private String kakaoClientId;
	@Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}")
	private String redirectUri;
	private String AdminKey = "caee6569cdee407d05616ab5a63e1338";

	private static final String TOKEN_URL = "https://kauth.kakao.com/oauth/token";
	private static final String USER_INFO_URL = "https://kapi.kakao.com/v2/user/me";
	private static final String USER_SERVICE_TERM_URL = "https://kapi.kakao.com/v2/user/scopes";

	// 2. 카카오 authorization code & access token 발급 요청
	public KakaoTokenResponse getAccessToken(String code, String frontRedirectUrl) throws JsonProcessingException {

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("grant_type", "authorization_code");
		params.add("client_id", kakaoClientId);
		if (frontRedirectUrl == null) {
			params.add("redirect_uri", redirectUri);
		} else {
			params.add("redirect_uri", frontRedirectUrl);
		}
		params.add("code", code);

		// TODO : 에러처리
		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
		ResponseEntity<String> response =
				restTemplate.exchange(
						TOKEN_URL,
						HttpMethod.POST,
						request,
						String.class);

		if (response.getStatusCode() != HttpStatus.OK) {
			log.error("Kakao token request failed: {}", response.getBody());
			throw new CustomException(ErrorCode.KAKAO_TOKEN_REQUEST_FAILED);
		}
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
		System.out.println(responseEntity);

		try {
//			return responseEntity.getBody();
			return objectMapper.readValue(responseEntity.getBody(), KakaoUserResponse.class);
		} catch (Exception e) {
			throw new RuntimeException("카카오 사용자 정보 조회에 실패했습니다.");
		}
	}

	public void getUserServiceTerms(String accessToken, KakaoUserResponse user) {

//		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
//		params.add("target_id_type", "user_id");
//		params.add("target_id", user.getId().toString());
//		params.add("result", "app_service_terms");
//		HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(params, serviceTermHeaders);

		HttpHeaders serviceTermHeaders = new HttpHeaders();
		serviceTermHeaders.setBearerAuth(accessToken);

		HttpEntity<Void> requestEntity = new HttpEntity<>(serviceTermHeaders);

		ResponseEntity<String> responseEntity =
				restTemplate.exchange(
						USER_SERVICE_TERM_URL,
						HttpMethod.GET,
						requestEntity,
						String.class);

		System.out.println(responseEntity);

	}

	public void kakaoUnlinck(Long userId) {

		// 헤더 설정
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.parseMediaType("application/x-www-form-urlencoded;charset=utf-8"));
		headers.set("Authorization", "KakaoAK " + AdminKey);

		// 요청 파라미터 설정
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("target_id_type", "user_id");
		params.add("target_id", String.valueOf(userId));

		// HttpEntity 생성
		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

		// POST 요청 실행
		ResponseEntity<String> response =
				restTemplate.exchange(
						"https://kapi.kakao.com/v1/user/unlink",
						HttpMethod.POST,
						request,
						String.class);

		System.out.println("연결 끊기 성공 : " + response.getBody());
	}
}
