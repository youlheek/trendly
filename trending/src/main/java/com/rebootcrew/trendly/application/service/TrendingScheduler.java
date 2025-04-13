package com.rebootcrew.trendly.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TrendingScheduler {

    private final TrendingRankingInsertUpdate trendingRankingInsertUpdate;

    // 매일 오전 12시 5분 실행
    //TotalList 업데이트
    @Scheduled(cron = "0 5 0 * * *")
    public void scheduleInsertTotalList() {
        trendingRankingInsertUpdate.insertTotalListAsync();
    }

}