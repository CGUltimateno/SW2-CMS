package com.coursefeedback.dto;

import com.coursefeedback.entity.QuestionTarget;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FeedbackQuestionRequest {
    private String questionText;
    private boolean isRatingQuestion;
    private QuestionTarget target;
}
