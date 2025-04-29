package com.rebootcrew.trendly.repository;

import com.rebootcrew.trendly.domain.KeywordPlatformStats;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface KeywordPlatformStatsRepository extends JpaRepository<KeywordPlatformStats, Long> {

    // 특정 키워드 플랫폼에 해당하는 통계 조회
    List<KeywordPlatformStats> findByKeywordPlatform_Id(Long keywordPlatformId);

    // 특정 키워드에 해당하는 통계 조회
    List<KeywordPlatformStats> findByKeyword_Id(Long keywordId);

    @Query(value = """
        SELECT s.search_volume
        FROM keyword_platform_stats s
        JOIN keyword_platform kp ON s.keyword_id2 = kp.keyword_id2
        JOIN keyword k ON kp.keyword_id2 = k.keyword_id
        WHERE k.keyword_name = :keywordName
    """, nativeQuery = true)
    List<Integer> findSearchVolumesByKeywordName(@Param("keywordName") String keywordName);


    // 특정 시간 이후로 기록된 통계 조회
    List<KeywordPlatformStats> findByRecordedAtAfter(LocalDateTime after);

    // 키워드 ID와 플랫폼 ID를 기준으로 통계 조회
    List<KeywordPlatformStats> findByKeyword_IdAndKeywordPlatform_Id(Long keywordId, Long keywordPlatformId);
}
