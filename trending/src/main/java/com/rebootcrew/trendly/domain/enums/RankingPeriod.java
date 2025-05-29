package com.rebootcrew.trendly.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum RankingPeriod {
    REAL_TIME("realTime"),
    DAILY("daily"),
    WEEKLY("weekly"),
    MONTHLY("monthly");

    private final String value;

    // Enum value로 변환
    public static RankingPeriod fromValue(String value) {
        return Arrays.stream(RankingPeriod.values())
                .filter(period -> period.value.equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid period value: " + value));
    }
}