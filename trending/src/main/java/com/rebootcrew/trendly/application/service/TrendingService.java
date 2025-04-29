package com.rebootcrew.trendly.application.service;

import com.rebootcrew.trendly.application.TrendingMapper;
import com.rebootcrew.trendly.application.dto.KeywordRankingListResponseDto;
import com.rebootcrew.trendly.application.dto.KeywordRankingRequestDto;
import com.rebootcrew.trendly.application.dto.KeywordRankingResponseDto;
import com.rebootcrew.trendly.application.dto.KeywordResponseDto;
import com.rebootcrew.trendly.domain.KeywordPlatform;
import com.rebootcrew.trendly.domain.KeywordPlatformRanking;
import com.rebootcrew.trendly.domain.enums.KeywordCategory;
import com.rebootcrew.trendly.domain.enums.Platform;
import com.rebootcrew.trendly.repository.KeywordPlatformRankingRepository;
import com.rebootcrew.trendly.repository.KeywordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class TrendingService {

    private final KeywordPlatformRankingRepository keywordPlatformRankingRepository;
    private final KeywordRepository keywordRepository;
    private final TrendingMapper trendingMapper;
    private final KeywordPlatformRankingRepository keywordRankingRepository;

    @Async("customTaskExecutor")
    @Transactional(readOnly = true)
    public CompletableFuture<List<KeywordResponseDto>> getAllKeywords() {
        return keywordRepository.findAllAsync()
                .thenApply(keywords -> keywords.stream()
                        .map(trendingMapper::toKeywordResponseDto)
                        .collect(Collectors.toList()));
    }

//    public List<KeywordRankingResponseDto> getKeywords(KeywordCategory category, Platform platform){
//        List<Keyword> keywords = keywordRepository.findAllByCategoriesContaining(category.toString()).get();
//        return;
//    }

    public KeywordRankingListResponseDto getRealtimeByFilterDays(
            KeywordRankingRequestDto rankingRequestDto) {

        int days = rankingRequestDto.getDays();
        Platform platform = rankingRequestDto.getPlatform();
        KeywordCategory category = rankingRequestDto.getCategory();

        // *일 전부터 현재까지 조회
        LocalDateTime startTime = LocalDateTime.now().minusDays(days);
        LocalDateTime endTime = LocalDateTime.now();

//        List<KeywordPlatformRanking> rankingss =
//                keywordRankingRepository
//                        .findAllByPlatformAndCategoriesAndPeriodsFileter(platform, category, startTime, endTime)
//                        .join(); // 혹은 .get()

        // *일간의 데이터를 조회
        List<KeywordRankingResponseDto> rankings = keywordRankingRepository.findRankingDtos(platform, category, startTime, endTime);

        return (trendingMapper.toRankingListResponseDto(rankingRequestDto, rankings, days));
    }

//    /** 비동기 키워드 검색 */
//    @Async
//    public CompletableFuture<KeywordResponseDto> searchKeyword(String keyword) {
//        return keywordRepository.findByKeywordName(keyword) // 비동기 데이터 조회
//                .thenApply(optionalKeyword -> optionalKeyword
//                        .map(TrendingMapper::toKeywordResponseDto) // DTO 변환
//                        .orElseThrow(() -> new RuntimeException("Keyword not found: " + keyword))); // 예외 처리
//    }

}
