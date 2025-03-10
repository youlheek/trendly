package com.rebootcrew.trendly.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync  // 명확하게 Async 활성화
public class AsyncConfig {

    @Bean(name = "customTaskExecutor")
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);  // 최소 스레드 개수
        executor.setMaxPoolSize(10);  // 최대 스레드 개수
        executor.setQueueCapacity(100); // 대기 큐 크기
        executor.setThreadNamePrefix("Async-Executor-"); // 디버깅 용이하도록 스레드 이름 설정
        executor.initialize();
        return executor;
    }
}
