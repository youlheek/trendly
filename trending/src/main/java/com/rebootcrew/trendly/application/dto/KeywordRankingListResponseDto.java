package com.rebootcrew.trendly.application.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.rebootcrew.trendly.domain.enums.KeywordCategory;
import com.rebootcrew.trendly.domain.enums.Platform;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class KeywordRankingListResponseDto {
    private KeywordCategory category; // 예: "전체"
    private Platform platform; // 예: "구글"
    private String date;     // 예: "2024-03-14"
    private String period;   // 예: "daily", "weekly"

    private List<KeywordRankingResponseDto> keywordsPlatformRanking;

    // 필요한 생성자나 메서드가 있으면 추가
}