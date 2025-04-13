package com.rebootcrew.trendly.application.dto;

import com.rebootcrew.trendly.domain.enums.Platform;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class KeywordRankingResponseDto {
    private Long id;            // keywordId 등
    private String keywordName; // 키워드명
    private int rank;       // 순위
    private int volume;     // 검색량, 언급량
    private Long roomId;     // 해당 키워드의 방ID (1:1 관계)
} 