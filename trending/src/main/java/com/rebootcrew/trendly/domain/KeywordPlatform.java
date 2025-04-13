package com.rebootcrew.trendly.domain;

import com.rebootcrew.trendly.common.domain.BaseEntity;
import com.rebootcrew.trendly.domain.enums.Platform;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "keyword_platform")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KeywordPlatform extends BaseEntity {  // BaseEntity 상속

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "keyword_platform_id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "keyword_id", nullable = false)
    private Keyword keyword;

    @Enumerated(EnumType.STRING)
    @Column(name = "platform", nullable = false)
    private Platform platform;  // ENUM (구글, 트위터, 네이버 등)

    @Column(name = "first_seen_at", nullable = true)  // 최초 감지 시간 유지
    private LocalDateTime firstSeenAt;


    // =============== 1:N (KeywordPlatformRanking) ===============
    @OneToMany(mappedBy = "keywordPlatform", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<KeywordPlatformRanking> rankings = new ArrayList<>();

    // =============== 1:N (KeywordPlatformStats) ===============
    @OneToMany(mappedBy = "keywordPlatform", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<KeywordPlatformStats> stats = new ArrayList<>();

    // 편의 메서드 (연관관계 동기화)
    public void addRanking(KeywordPlatformRanking ranking) {
        rankings.add(ranking);
        ranking.setKeywordPlatform(this);
    }

    public void removeRanking(KeywordPlatformRanking ranking) {
        rankings.remove(ranking);
        ranking.setKeywordPlatform(null);
    }

    public void addStats(KeywordPlatformStats stat) {
        stats.add(stat);
        stat.setKeywordPlatform(this);
    }

    public void removeStats(KeywordPlatformStats stat) {
        stats.remove(stat);
        stat.setKeywordPlatform(null);
    }

    // created_at을 오버라이드하여 DB에서 제외 (중요!)
    @Override
    public LocalDateTime getCreatedAt() {
        return null;  //  Hibernate가 필드를 무시하도록 설정
    }
}