package com.rebootcrew.trendly.application;

import com.rebootcrew.trendly.application.dto.KeywordResponseDto;
import com.rebootcrew.trendly.domain.Keyword;
import org.springframework.stereotype.Component;

@Component
public class TrendingMapper {

    public static KeywordResponseDto toKeywordResponseDto(Keyword keyword) {
        return KeywordResponseDto.builder()
                .id(keyword.getId())
                .keywordName(keyword.getKeywordName())
                .categories(keyword.getCategories()) // 카테고리 추가
                .positiveCount(keyword.getPositiveCount())
                .neutralCount(keyword.getNeutralCount())
                .negativeCount(keyword.getNegativeCount())
                .lastUpdatedAt(keyword.getUpdatedAt()) // 최종 업데이트 시간 추가
                .build();
    }
}
