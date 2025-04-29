package com.rebootcrew.trendly.application.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.rebootcrew.trendly.application.TrendingMapper;
import com.rebootcrew.trendly.application.dto.jsondto.KeywordJsonContent;
import com.rebootcrew.trendly.domain.Keyword;
import com.rebootcrew.trendly.domain.KeywordPlatform;
import com.rebootcrew.trendly.domain.KeywordPlatformRanking;
import com.rebootcrew.trendly.domain.KeywordPlatformStats;
import com.rebootcrew.trendly.domain.enums.FastApiEndpoints;
import com.rebootcrew.trendly.domain.enums.KeywordCategory;
import com.rebootcrew.trendly.domain.enums.Platform;
import com.rebootcrew.trendly.repository.KeywordPlatformRankingRepository;
import com.rebootcrew.trendly.repository.KeywordPlatformRepository;
import com.rebootcrew.trendly.repository.KeywordPlatformStatsRepository;
import com.rebootcrew.trendly.repository.KeywordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@Service
@RequiredArgsConstructor
public class TrendingRankingInsertUpdate {

    private final ExternalApiService externalApiService;
    private final TrendingMapper trendingMapper;
    private final KeywordRepository keywordRepository;
    private final KeywordPlatformRepository keywordPlatformRepository;
    private final KeywordPlatformRankingRepository keywordPlatformRankingRepository;
    private final KeywordPlatformStatsRepository keywordPlatformStatsRepository;


    @Autowired
    @Qualifier("customTaskExecutor")
    private Executor asyncExecutor;

    //FastAPI에서 전체 트렌드 키워드(top, bottom)를 가져와 DB에 삽입
    @Async
    public CompletableFuture<Void> insertTotalListAsync() {
        CompletableFuture<JsonNode> topFuture =
                externalApiService.getKeywordRankAsync(FastApiEndpoints.TOP_TOTAL).toFuture();

        CompletableFuture<JsonNode> bottomFuture =
                externalApiService.getKeywordRankAsync(FastApiEndpoints.BOTTOM_TOTAL).toFuture();

        // top 먼저 insert
        CompletableFuture<Void> topInsert = topFuture.thenComposeAsync(top -> {
            List<KeywordJsonContent> topRecords = trendingMapper.mapToKeywordRecords(top, "total");
            return insertFromKeywordContentAsync(topRecords, true); // top은 true
        }, asyncExecutor);

        // bottom insert
        CompletableFuture<Void> bottomInsert = bottomFuture.thenComposeAsync(bottom -> {
            List<KeywordJsonContent> bottomRecords = trendingMapper.mapToKeywordRecords(bottom, "total");
            return insertFromKeywordContentAsync(bottomRecords, false); // bottom은 false
        }, asyncExecutor);

        // 둘 다 완료될 때까지 기다림
        return CompletableFuture.allOf(topInsert, bottomInsert);
    }

    //키워드 리스트를 병렬로 저장 (비동기 작업)
    public CompletableFuture<Void> insertFromKeywordContentAsync(List<KeywordJsonContent> records, Boolean isTop) {
        List<CompletableFuture<Void>> futures = new ArrayList<>();

        for (KeywordJsonContent record : records) {
            String date = record.getDate();

            for (List<Object> keywordEntry : record.getKeywords()) {
                String keywordName = (String) keywordEntry.get(0);
                int volume = (int) keywordEntry.get(1);

                CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                    Keyword keyword = findOrCreateKeyword(keywordName);
                    KeywordPlatform platform = findOrCreateKeywordPlatform(keyword, date);

                    int rank = record.getKeywords().indexOf(keywordEntry) + 1;

                    // bottom일 때는 rank 조정
                    if (Boolean.FALSE.equals(isTop)) {
                        rank = record.getKeywords().size() - rank + 6;
                    }

                    createOrUpdateKeywordPlatformRanking(platform, rank);
                    createKeywordPlatformStats(platform, keyword, volume, date);

                }, asyncExecutor);

                futures.add(future);
            }
        }

        return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
    }

    private Keyword findOrCreateKeyword(String keywordName) {
        return keywordRepository.findByKeywordName(keywordName)
                .join()
                .orElseGet(() -> {
                    Keyword newKeyword = Keyword.builder()
                            .keywordName(keywordName)
                            .categories(Set.of(KeywordCategory.전체))
                            .positiveCount(0)
                            .neutralCount(0)
                            .negativeCount(0)
                            .build();
                    return keywordRepository.save(newKeyword);
                });
    }

    /**
     * 키워드 + 날짜 기준으로 플랫폼 존재 여부를 확인하고 없으면 생성
     */
    private KeywordPlatform findOrCreateKeywordPlatform(Keyword keyword, String date) {
        LocalDateTime firstSeenAt = LocalDateTime.parse(date + "T00:00:00");

        return keywordPlatformRepository.findByKeywordAndFirstSeenAt(keyword, firstSeenAt)
                .orElseGet(() -> {
                    KeywordPlatform keywordPlatform = KeywordPlatform.builder()
                            .keyword(keyword)
                            .platform(Platform.전체)
                            .firstSeenAt(firstSeenAt)
                            .build();
                    return keywordPlatformRepository.save(keywordPlatform);
                });
    }

    /**
     * 키워드 플랫폼 랭킹을 업데이트하거나 새로 생성
     */
    private void createOrUpdateKeywordPlatformRanking(KeywordPlatform keywordPlatform, int currentRanking) {
        KeywordPlatformRanking existingRanking = keywordPlatformRankingRepository.findByKeywordPlatform(keywordPlatform)
                .orElse(null);

        if (existingRanking != null) {
            existingRanking.setPreviousRank(existingRanking.getRanking());
            existingRanking.setRanking(currentRanking);
            keywordPlatformRankingRepository.save(existingRanking);
        } else {
            KeywordPlatformRanking newRanking = KeywordPlatformRanking.builder()
                    .keywordPlatform(keywordPlatform)
                    .ranking(currentRanking)
                    .previousRank(null)
                    .build();
            keywordPlatformRankingRepository.save(newRanking);
        }
    }


    private void createKeywordPlatformStats(KeywordPlatform keywordPlatform, Keyword keyword, int currentVolume, String date) {
        LocalDateTime recordedAt = LocalDateTime.parse(date + "T00:00:00");

        KeywordPlatformStats lastStat = keywordPlatform.getStats().stream()
                .max((a, b) -> a.getRecordedAt().compareTo(b.getRecordedAt()))
                .orElse(null);

        Integer previousVolume = lastStat != null ? lastStat.getSearchVolume() : null;

        KeywordPlatformStats stats = KeywordPlatformStats.builder()
                .keywordPlatform(keywordPlatform)
                .keyword(keyword)
                .searchVolume(currentVolume)
                .previousVolume(previousVolume)
                .recordedAt(recordedAt)
                .build();

        // 연관관계 설정 (JPA 양방향 연계)
        keywordPlatform.addStats(stats);

        // 명시적으로 저장해야 실제 insert가 일어남
        keywordPlatformStatsRepository.save(stats);
    }

//    public void deleteKeyword(Long keywordId) {
//        ChatRoom chatRoom = chatRoomRepository.findByKeywordId(keywordId);
//        if (chatRoom != null) {
//            chatRoomRepository.delete(chatRoom);
//        }
//        keywordRepository.deleteById(keywordId);
//    }


//    public void insertGoogleList() {
//        externalApiService.getKeywordRank(FastApiEndpoints.TOP_GOOGLE);
//
//        externalApiService.getKeywordRank(FastApiEndpoints.BOTTOM_GOOGLE);
//
//        return ;
//    }
//
//    public void insertLastGoogleList() {
//        externalApiService.getKeywordRank(FastApiEndpoints.LAST_TOP_GOOGLE);
//
//        externalApiService.getKeywordRank(FastApiEndpoints.LAST_BOTTOM_GOOGLE);
//
//        return ;
//    }

}
