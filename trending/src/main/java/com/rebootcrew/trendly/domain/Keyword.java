package com.rebootcrew.trendly.domain;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import com.rebootcrew.trendly.common.domain.BaseEntity;
import com.rebootcrew.trendly.domain.enums.KeywordCategory;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "keyword")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Keyword extends BaseEntity {

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
    @Builder.Default
    private Set<KeywordCategory> categories = EnumSet.of(KeywordCategory.전체);

    @Column(name = "positive_count")
    @Builder.Default
    private Integer positiveCount=0;

    @Column(name = "neutral_count")
    @Builder.Default
    private Integer neutralCount=0;

    @Column(name = "negative_count")
    @Builder.Default
    private Integer negativeCount=0;

}