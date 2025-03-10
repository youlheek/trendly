package com.rebootcrew.trendly.application.dto;

import com.rebootcrew.trendly.domain.enums.KeywordCategory;
import com.rebootcrew.trendly.domain.enums.Platform;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class TrendingRequestDto {
    private Set<KeywordCategory> categories; // 카테고리 필터
    private Platform platform; // 플랫폼 필터
    private Integer maxRank; // 최대 랭킹 필터
}