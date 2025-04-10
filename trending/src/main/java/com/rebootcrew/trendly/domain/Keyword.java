package com.rebootcrew.trendly.domain;

import com.rebootcrew.trendly.common.domain.BaseEntity;
import com.rebootcrew.trendly.domain.enums.KeywordCategory;
import jakarta.persistence.*;
import lombok.*;
import java.util.Set;

@Entity
@Table(name = "keyword")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Keyword extends BaseEntity {  //BaseEntity 상속

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "keyword_id")
    private Long id;

    @Column(name = "keyword_name", nullable = false, length = 255)
    private String keywordName;

    @Enumerated(EnumType.STRING)
    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "keyword_category", joinColumns = @JoinColumn(name = "keyword_id"))
    @Column(name = "category")
    private Set<KeywordCategory> categories;  // ENUM SET (카테고리)

    @Column(name = "positive_count")
    private Integer positiveCount;

    @Column(name = "neutral_count")
    private Integer neutralCount;

    @Column(name = "negative_count")
    private Integer negativeCount;
}