package com.rebootcrew.trendly.application.service;

import com.rebootcrew.trendly.application.dto.KeywordRankingListResponseDto;
import com.rebootcrew.trendly.application.dto.KeywordRankingResponseDto;
import com.rebootcrew.trendly.domain.enums.KeywordCategory;
import com.rebootcrew.trendly.domain.enums.Platform;

import java.time.LocalDateTime;
import java.util.List;

public class TrendingService {

    public List<KeywordRankingResponseDto> getRealtimeByFilterHours(int times, Platform platform, KeywordCategory category, LocalDateTime currentTime) {
        // 현재 시간에서 4시간 전 시간 계산
        LocalDateTime startTime = LocalDateTime.now().minusHours(times);
        LocalDateTime endTime = LocalDateTime.now();

        // DB에서 최근 4시간 동안의 키워드 검색량 필터링
        return keywordRankingRepository.findByTimeRange(startTime, endTime, platform, category);
    }

    public List<KeywordRankingResponseDto> getAllKeywords(){

    }

    public List<KeywordRankingResponseDto> getKeywords(KeywordCategory category, Platform platform){

    }

    public KeywordRankingListResponseDto getRealtimeByFilterDays(int days, Platform platform, KeywordCategory category, LocalDateTime currentTime) {
        // 7일 전부터 현재까지 조회
        LocalDateTime startTime = LocalDateTime.now().minusDays(days);
        LocalDateTime endTime = LocalDateTime.now();

        // 7일간의 데이터를 조회
        List<KeywordRankingResponseDto> rankings = keywordRankingRepository.findByTimeRange(startTime, endTime, platform, category);

        return new KeywordRankingListResponseDto(rankings);
    }

    public List<KeywordRankingResponseDto> searchKeywords(String keyword){

    }

}
