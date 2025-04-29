package com.rebootcrew.trendly.repository;

import com.rebootcrew.trendly.application.dto.KeywordRankingResponseDto;
import com.rebootcrew.trendly.domain.KeywordPlatform;
import com.rebootcrew.trendly.domain.KeywordPlatformRanking;
import com.rebootcrew.trendly.domain.enums.KeywordCategory;
import com.rebootcrew.trendly.domain.enums.Platform;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Repository
public interface KeywordPlatformRankingRepository extends JpaRepository<KeywordPlatformRanking, Long> {

    List<KeywordPlatformRanking> findAllByRankingLessThan(int rank);

    @Query("""
        SELECT r FROM KeywordPlatformRanking r
        JOIN r.keywordPlatform kp
        JOIN kp.keyword k
        WHERE kp.platform = :platform
          AND :category MEMBER OF k.categories
          AND kp.firstSeenAt BETWEEN :fromTime AND :toTime
    """)
    List<KeywordPlatformRanking> findAllByPlatformAndCategoryAndPeriod(
            @Param("platform") Platform platform,
            @Param("category") KeywordCategory category,
            @Param("fromTime") LocalDateTime fromTime,
            @Param("toTime") LocalDateTime toTime
    );

    CompletableFuture<List<KeywordPlatformRanking>> findAllByKeywordPlatformFirstSeenAtBetweenAndKeywordPlatformPlatformAndKeywordPlatformKeywordCategoriesContaining(
            LocalDateTime startTime, LocalDateTime endTime, Platform platform, KeywordCategory category
    );

    @Query("""
    SELECT new com.rebootcrew.trendly.application.dto.KeywordRankingResponseDto(
        r.id,
        k.keywordName,
        COALESCE(r.ranking, 0),
        COALESCE(s.searchVolume, 0),
        cr.id
    )
    FROM KeywordPlatformRanking r
         JOIN r.keywordPlatform kp
         JOIN kp.keyword k
         LEFT JOIN ChatRoom cr ON cr.keyword = k
         LEFT JOIN KeywordPlatformStats s
                ON s.keywordPlatform = kp
               AND s.keyword = k
    WHERE kp.platform = :platform
      AND :category MEMBER OF k.categories
      AND kp.firstSeenAt BETWEEN :startTime AND :endTime
    """)

    List<KeywordRankingResponseDto> findRankingDtos(
            @Param("platform") Platform platform,
            @Param("category") KeywordCategory category,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime
    );

    Optional<KeywordPlatformRanking> findByKeywordPlatform(KeywordPlatform keywordPlatform);
}