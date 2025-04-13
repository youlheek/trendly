package com.rebootcrew.trendly.domain.enums;

public enum FastApiEndpoints {
    TOP_TOTAL("/api/v1/keywords-rank/top"),
    BOTTOM_TOTAL("/api/v1/keywords-rank/bottom"),
    TOP_GOOGLE("/api/v1/google-rank/top"),
    BOTTOM_GOOGLE("/api/v1/google-rank/bottom"),
    LAST_TOP_TOTAL("/api/v1/last-keywords-rank/top"),
    LAST_BOTTOM_TOTAL("/api/v1/last-keywords-rank/bottom"),
    LAST_TOP_GOOGLE("/api/v1/last-google-rank/top"),
    LAST_BOTTOM_GOOGLE("/api/v1/last-google-rank/bottom");

    private final String path;
    FastApiEndpoints(String path) {
        this.path = path;
    }
    public String getPath() {
        return path;
    }
}