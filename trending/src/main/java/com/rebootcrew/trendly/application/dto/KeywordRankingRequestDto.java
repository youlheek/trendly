package com.rebootcrew.trendly.application.dto;

import com.rebootcrew.trendly.domain.enums.KeywordCategory;
import com.rebootcrew.trendly.domain.enums.Platform;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class KeywordRankingRequestDto {
    private Platform platform;       // 쿼리 파라미터 platform
    private KeywordCategory category = KeywordCategory.전체;       // 쿼리 파라미터 KeywordCategory
    private String currentTime;    // 쿼리 파라미터 currentTime
    private int days;              // 쿼리 파라미터 days (없으면 default)

    // 필요한 생성자/빌더/검증로직 등
}