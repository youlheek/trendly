package com.rebootcrew.trendly.application.dto;

import com.rebootcrew.trendly.domain.enums.Platform;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class KeywordRankingResponseDto {
    private Long keywordId;
    private String keywordName;
    private Platform platform;
    private Integer currentRank;
    private Integer previousRank;
    private String changeStatus; // "UP", "DOWN", "NEW", "SAME"
} 