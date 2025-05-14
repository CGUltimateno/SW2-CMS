package com.coursefeedback.dto;

import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FeedbackSummaryDTO {
    private String courseName;
    private String formTitle;
    private int totalResponses;
    private List<QuestionSummary> questions;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class QuestionSummary {
        private String questionText;
        private boolean isRatingQuestion;
        private Double averageRating;       // nullable if not a rating question
        private List<String> textAnswers;
       private String target; // empty if rating question
    }
}
