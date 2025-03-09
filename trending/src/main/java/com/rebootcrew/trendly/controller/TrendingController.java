package com.rebootcrew.trendly.controller;

import com.rebootcrew.trendly.domain.enums.KeywordCategory;
import com.rebootcrew.trendly.domain.enums.Platform;
import com.rebootcrew.trendly.application.service.TrendingService;
import com.rebootcrew.trendly.domain.enums.RankingPeriod;
import lombok.RequiredArgsConstructor;
import com.rebootcrew.trendly.application.dto.KeywordResponseDto;
import com.rebootcrew.trendly.application.dto.KeywordRankingListResponseDto;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/trending")
@RequiredArgsConstructor
public class TrendingController {

    private final TrendingService trendingService;

    /**키워드 전체 조회 (필터 포함) */
    @GetMapping("/keywords")
    public ResponseEntity<List<KeywordResponseDto>> getAllKeywords() {
        return ResponseEntity.ok(trendingService.getAllKeywords());
    }

    /**특정 플랫폼별 키워드 필터링 */
    @GetMapping("/keywords/filter")
    public ResponseEntity<List<KeywordResponseDto>> getKeywordsByPlatform(
            @RequestParam Platform platform,
            @RequestParam(required = false) KeywordCategory category) {
        return ResponseEntity.ok(trendingService.getKeywords(category, platform));
    }

    /** [실시간] 키워드 랭킹 */
    @GetMapping("/keywords/ranking/realtime")
    public ResponseEntity<KeywordRankingListResponseDto> getRealtimeRanking(
            @RequestParam(required = false) Platform platform,
            @RequestParam(required = false) KeywordCategory category,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime currentTime) {
        return ResponseEntity.ok(trendingService.getRealtimeByFilterDays(4, platform, category, currentTime));
    }

    /** [일간] 키워드 랭킹 */
    @GetMapping("/keywords/ranking/daily")
    public ResponseEntity<KeywordRankingListResponseDto> getDailyRanking(
            @RequestParam(required = false) Platform platform,
            @RequestParam(required = false) KeywordCategory category,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime currentTime) {
        return ResponseEntity.ok(trendingService.getRealtimeByFilterDays(1, platform, category, currentTime));
    }


    /** [주간] 키워드 랭킹 */
    @GetMapping("/keywords/ranking/weekly")
    public ResponseEntity<KeywordRankingListResponseDto> getWeeklyRanking(
            @RequestParam(required = false) Platform platform,
            @RequestParam(required = false) KeywordCategory category,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime currentTime) {
        return ResponseEntity.ok(trendingService.getRealtimeByFilterDays(7, platform, category, currentTime));
    }

    /** [월간] 키워드 랭킹 */
    @GetMapping("/keywords/ranking/monthly")
    public ResponseEntity<KeywordRankingListResponseDto> getMonthlyRanking(
            @RequestParam(required = false) Platform platform,
            @RequestParam(required = false) KeywordCategory category,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime currentTime) {
        return ResponseEntity.ok(trendingService.getRealtimeByFilterDays(30, platform, category, currentTime));
    }

//
//    /**키워드 상세 조회 */
//    @GetMapping("/keywords/{keywordId}")
//    public ResponseEntity<KeywordResponseDto> getKeywordDetail(@PathVariable Long keywordId) {
//        return ResponseEntity.ok(trendingService.getKeywordDetail(keywordId));
//    }

    /** 키워드 검색 */
    @GetMapping("/keywords/search")
    public ResponseEntity<List<KeywordResponseDto>> searchKeywords(
            @RequestParam String keyword,
            @RequestParam(required = false) KeywordCategory category) {
        return ResponseEntity.ok(trendingService.searchKeywords(keyword));
    }

    /** 키워드와 관련된 채팅방 조회 */
    @GetMapping("/keywords/chat/{keywordId}")
    public ResponseEntity<ChatRoomResponse> getChatRoomByKeyword(@PathVariable Long keywordId) {
        return ResponseEntity.ok(trendingService.getChatRoomByKeyword(keywordId));
    }
}
