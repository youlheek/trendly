package com.rebootcrew.trendly.application.dto.jsondto;

import lombok.Data;
import java.util.List;

@Data
public class KeywordTrendResponse {
    private String type;
    private List<KeywordJsonContent> contents;
}