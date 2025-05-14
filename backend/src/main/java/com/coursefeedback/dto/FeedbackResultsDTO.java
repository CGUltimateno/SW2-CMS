package com.coursefeedback.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FeedbackResultsDTO {
    private Long courseId;
    private String courseName;
    private String courseDescription;
    private int totalResponses;
    private double averageRating;
    
    // Rating questions aggregated by question text
    private Map<String, QuestionResultDTO> ratingQuestionResults;
    
    // Text answers grouped by question text
    private Map<String, List<String>> textResponses;
}