package com.rebootcrew.trendly.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
@EnableAsync
public class SchedulerConfig {
    // @Scheduled, @Async 사용을 위한 설정 클래스
}
