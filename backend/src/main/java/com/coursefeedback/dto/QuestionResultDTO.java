package com.coursefeedback.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuestionResultDTO {
    private String questionText;
    private double averageRating;
    private int responseCount;
    
    // Distribution of ratings (1-5 and the count of each)
    private Map<Integer, Integer> ratingDistribution;
}