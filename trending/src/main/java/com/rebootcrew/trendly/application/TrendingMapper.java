package com.rebootcrew.trendly.application;

import com.fasterxml.jackson.databind.JsonNode;
import com.rebootcrew.trendly.application.dto.KeywordRankingListResponseDto;
import com.rebootcrew.trendly.application.dto.KeywordRankingRequestDto;
import com.rebootcrew.trendly.application.dto.KeywordRankingResponseDto;
import com.rebootcrew.trendly.application.dto.KeywordResponseDto;
import com.rebootcrew.trendly.application.dto.jsondto.KeywordJsonContent;
import com.rebootcrew.trendly.application.dto.jsondto.KeywordTrendResponse;
import com.rebootcrew.trendly.domain.Keyword;
import com.rebootcrew.trendly.domain.KeywordPlatformRanking;
import com.rebootcrew.trendly.domain.enums.KeywordCategory;
import com.rebootcrew.trendly.domain.enums.Platform;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;


@Component
public class TrendingMapper {

    public KeywordResponseDto toKeywordResponseDto(Keyword keyword) {
        Set<KeywordCategory> categoriesSafe =
                Optional.ofNullable(keyword.getCategories())
                        .map(c -> new HashSet<>(c))  // 타입 명확함
                        .orElse(new HashSet<>());

        return KeywordResponseDto.builder()
                .id(keyword.getId())
                .keywordName(keyword.getKeywordName())
                .categories(categoriesSafe)
                .positiveCount(keyword.getPositiveCount())
                .neutralCount(keyword.getNeutralCount())
                .negativeCount(keyword.getNegativeCount())
                .lastUpdatedAt(keyword.getUpdatedAt())
                .build();
    }

    // 추가: 날짜별 키워드 그룹 추출 메서드
    public List<KeywordJsonContent> toKeywordDateGroupList(KeywordTrendResponse response) {
        return Optional.ofNullable(response)
                .map(KeywordTrendResponse::getContents)
                .orElse(List.of());
    }

    public KeywordRankingRequestDto toRequestDto(String platform, String currentTime, int days) {
        KeywordRankingRequestDto dto = new KeywordRankingRequestDto();
        dto.setCategory(KeywordCategory.valueOf("전체"));
        dto.setPlatform(Platform.valueOf(platform));
        dto.setCurrentTime(currentTime);
        dto.setDays(days);
        return dto;
    }

    public List<KeywordJsonContent> mapToKeywordRecords(JsonNode jsonNode, String platform) {
        List<KeywordJsonContent> result = new ArrayList<>();
        if (jsonNode == null || !jsonNode.has("contents")) return result;

        JsonNode contents = jsonNode.get("contents");

        for (JsonNode contentNode : contents) {
            String date = contentNode.has("date") ? contentNode.get("date").asText() : null;
            JsonNode keywordsNode = contentNode.get("keywords");

            List<List<Object>> keywordsList = new ArrayList<>();

            for (JsonNode keywordPair : keywordsNode) {
                String keyword = keywordPair.get(0).asText();
                int volume = keywordPair.get(1).asInt();

                List<Object> keywordEntry = new ArrayList<>();
                keywordEntry.add(keyword);
                keywordEntry.add(volume);

                keywordsList.add(keywordEntry);
            }

            result.add(new KeywordJsonContent(date, keywordsList));
        }

        return result;
    }

    public KeywordRankingResponseDto toRankingResponseDto(KeywordPlatformRanking entity, int searchVolume, Long roomId) {
        return KeywordRankingResponseDto.builder()
                .id(entity.getKeywordPlatform().getKeyword().getId())
                .keywordName(entity.getKeywordPlatform().getKeyword().toString())
                .rank(entity.getRanking())
                .volume(searchVolume)
                .roomId(roomId)
                .build();
    }

    public KeywordRankingListResponseDto toRankingListResponseDto(KeywordRankingRequestDto rankingRequestDto,
                                                                  List<KeywordRankingResponseDto> keywordsPlatformRanking,
                                                                  int days) {
        KeywordRankingListResponseDto dto = new KeywordRankingListResponseDto();
        dto.setPlatform(rankingRequestDto.getPlatform());
        dto.setCategory(rankingRequestDto.getCategory());
        dto.setDate(rankingRequestDto.getCurrentTime());
        dto.setKeywordsPlatformRanking(keywordsPlatformRanking);

        String periods = "realTime";
        if(days == 1) {
            periods = "daily";
        }
        else if (days == 7) {
            periods = "weekly";
        }
        else if (days == 28) {
            periods = "monthly";
        }

        dto.setPeriod(periods);

        return dto;
    }

    public LocalDate dateConverter(String dateString){
        // 2) 형식 지정 (yyyy-MM-dd)
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        // 3) 문자열 -> LocalDate 변환
        return LocalDate.parse(dateString, formatter);
    }


}
