package com.rebootcrew.trendly.application.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.rebootcrew.trendly.domain.enums.FastApiEndpoints;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExternalApiService {

    @Qualifier("fastApiWebClient")
    private final WebClient webClient;

    public Mono<JsonNode> getKeywordRankAsync(FastApiEndpoints type) {
        log.info("[FastAPI 요청] {}", type.getPath());
        return webClient.get()
                .uri(type.getPath())
                .retrieve()
                .bodyToMono(JsonNode.class)
                .doOnSuccess(response -> log.info("[FastAPI 응답 완료] {}", response))
                .doOnError(error -> log.error("[FastAPI 요청 실패] {}", error.getMessage()));
    }

}