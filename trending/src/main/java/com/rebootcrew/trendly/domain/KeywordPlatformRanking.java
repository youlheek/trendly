package com.rebootcrew.trendly.domain;

import com.rebootcrew.trendly.common.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "keyword_platform_ranking")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KeywordPlatformRanking extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ranking_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "keyword_platform_id", nullable = false)
    private KeywordPlatform keywordPlatform;

    @Column(name = "platform_keyword_id", nullable = false)
    private Long platformKeywordId;

    @Column(name = "rank", nullable = false)
    private Integer rank;

    @Column(name = "previous_rank")
    private Integer previousRank;

    // created_at을 오버라이드하여 DB에서 제외
    @Override
    public LocalDateTime getCreatedAt() {
        return null;  //  Hibernate가 필드를 무시하도록 설정
    }
}