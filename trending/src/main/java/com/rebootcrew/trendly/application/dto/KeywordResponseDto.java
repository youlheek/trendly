package com.rebootcrew.trendly.application.dto;

import java.time.LocalDateTime;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.rebootcrew.trendly.domain.enums.KeywordCategory;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor // 기본 생성자 추가
@AllArgsConstructor // 모든 필드를 포함하는 생성자 추가
public class KeywordResponseDto {
    private Long id;
    private String keywordName;
    private Set<KeywordCategory> categories;
    private Integer positiveCount;
    private Integer neutralCount;
    private Integer negativeCount;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") // JSON 직렬화 포맷 설정
    private LocalDateTime lastUpdatedAt;
}