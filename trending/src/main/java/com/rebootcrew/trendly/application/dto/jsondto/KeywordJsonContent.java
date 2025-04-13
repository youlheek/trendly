package com.rebootcrew.trendly.application.dto.jsondto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class KeywordJsonContent {
    private String date;
    private List<List<Object>> keywords;
}