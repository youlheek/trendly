package com.rebootcrew.trendly.application.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.rebootcrew.trendly.domain.enums.KeywordCategory;
import com.rebootcrew.trendly.domain.enums.Platform;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class KeywordRankingListResponseDto {
    private Platform platform;
    private KeywordCategory category;
    private LocalDateTime recordedAt;
    private List<RankingItem> rankings;

    @Getter
    @Builder
    public static class RankingItem {
        private Integer rank;
        private Long keywordId;
        private String keyword;
        private Integer previousRank;
        private String changeStatus; // "UP", "DOWN", "NEW", "SAME"
        private Boolean isChattable; // 채팅하기 버튼 활성화 여부
    }
} 