package com.rebootcrew.trendly.repository;

import com.rebootcrew.trendly.domain.Keyword;
import com.rebootcrew.trendly.domain.KeywordPlatformRanking;
import com.rebootcrew.trendly.domain.enums.KeywordCategory;
import com.rebootcrew.trendly.domain.enums.Platform;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Repository
public interface KeywordPlatformRankingRepository extends JpaRepository<KeywordPlatformRanking, Long> {

    @Async
    CompletableFuture<List<KeywordPlatformRanking>> findAllByRankLessThan(int rank);

    @Async
    @Query("SELECT r FROM KeywordPlatformRanking r WHERE r.keywordPlatform.platform = ?1 AND " +
            "r.keywordPlatform.keyword.categories = ?2 " +
            "AND r.keywordPlatform.firstSeenAt > ?3 AND r.keywordPlatform.firstSeenAt < ?4")
    CompletableFuture<List<KeywordPlatformRanking>> findByPlatformAndCategoriesAndPeriodsFileter(
            Platform platform,
            KeywordCategory category,
            LocalDateTime fromTime,
            LocalDateTime endTime
    );


}