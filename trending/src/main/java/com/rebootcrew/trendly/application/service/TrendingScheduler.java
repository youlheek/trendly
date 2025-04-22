package com.rebootcrew.trendly.application.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class TrendingScheduler {

    private final TrendingRankingInsertUpdate trendingRankingInsertUpdate;

    // 매일 오전 12시 5분 실행
    //TotalList 업데이트
    @Scheduled(cron = "0 5 17 * * *", zone = "Asia/Seoul")
    public void scheduleInsertTotalList() {
        trendingRankingInsertUpdate.insertTotalListAsync();
    }

}