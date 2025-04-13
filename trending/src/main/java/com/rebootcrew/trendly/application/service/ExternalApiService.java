package com.rebootcrew.trendly.application.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.rebootcrew.trendly.domain.enums.FastApiEndpoints;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ExternalApiService {

    @Qualifier("fastApiWebClient")
    private final WebClient webClient;

    public Mono<JsonNode> getKeywordRankAsync(FastApiEndpoints type) {
        return fetchKeywordRank(type.getPath());
    }

    private Mono<JsonNode> fetchKeywordRank(String path) {
        return webClient.get()
                .uri(path)
                .retrieve()
                .bodyToMono(JsonNode.class)
                .onErrorResume(e -> {
                    // 에러 로깅 등
                    return Mono.empty();
                });
    }

}