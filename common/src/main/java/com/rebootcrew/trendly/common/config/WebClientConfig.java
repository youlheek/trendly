package com.rebootcrew.trendly.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import org.springframework.web.reactive.function.client.WebClient;

@Configuration
class WebClientConfig {
    //  WebClient Bean 설정
    @Bean
    public WebClient fastApiWebClient(WebClient.Builder builder) {
        return builder
                .baseUrl("http://keyword-scrapper:8088")  // FastAPI가 Docker Compose로 통신하는 주소
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }
}
