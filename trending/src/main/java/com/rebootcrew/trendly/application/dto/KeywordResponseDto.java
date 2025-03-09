package com.rebootcrew.trendly.application.dto;

import java.time.LocalDateTime;
import java.util.Set;

import com.rebootcrew.trendly.domain.enums.KeywordCategory;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class KeywordResponseDto {
    private Long id;
    private String keywordName;
    private Set<KeywordCategory> categories;
    private Integer positiveCount;
    private Integer neutralCount;
    private Integer negativeCount;
    private LocalDateTime lastUpdatedAt;
} 