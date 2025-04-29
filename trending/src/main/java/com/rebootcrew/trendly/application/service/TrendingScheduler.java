package com.rebootcrew.trendly.application.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;

import lombok.RequiredArgsConstructor;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class TrendingScheduler {

    private final TrendingRankingInsertUpdate trendingRankingInsertUpdate;

    // 매일 오전 12시 5분 실행
    //TotalList 업데이트
    @Scheduled(cron = "0 51 15 * * *", zone = "Asia/Seoul")
    // 서버가 켜진 뒤부터 7시 10분마다 계속 반복하고 싶다면:
//    @Scheduled(fixedRate = 430_000)  // 7분 10초 = 430,000ms
//    @PostConstruct  //어플리케이션 실행시 구동
    public void scheduleInsertTotalList() {
        System.out.println("스케줄러 실행됨: " + LocalDateTime.now());
        trendingRankingInsertUpdate.insertTotalListAsync();
    }

}