package com.rebootcrew.trendly.repository;

import com.rebootcrew.trendly.domain.KeywordPlatform;
import com.rebootcrew.trendly.domain.enums.Platform;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Repository
public interface KeywordPlatformRepository extends JpaRepository<KeywordPlatform, Long> {

    @Async
    CompletableFuture<List<KeywordPlatform>> findAllByPlatform(Platform platform);
}
