package com.rebootcrew.trendly.domain;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

/**
 * keyword_platform_stats 테이블 매핑 예시
 * stat_id = PK
 * keyword_platform_id, keyword_id = FK
 */
@Entity
@Table(name = "keyword_platform_stats")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KeywordPlatformStats {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "stat_id", nullable = false)
    private Long statId;  // PK

    // ---------- 외래 키들 ----------
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "keyword_platform_id", nullable = false)
    private KeywordPlatform keywordPlatform;
    // keyword_platform 테이블과 N:1 관계

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "keyword_id", nullable = false)
    private Keyword keyword;
    // keyword 테이블과 N:1 관계

    // ---------- 일반 칼럼들 ----------
    @Column(name = "search_volume")
    private Integer searchVolume;

    @Column(name = "previous_volume")
    private Integer previousVolume;

    @Column(name = "recorded_at")
    private LocalDateTime recordedAt;

    // 필요한 로직, 연관관계 편의 메서드, etc.
}