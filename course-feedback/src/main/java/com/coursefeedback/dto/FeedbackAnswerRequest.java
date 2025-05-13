package com.coursefeedback.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FeedbackAnswerRequest {
    private Long questionId;
    private Integer rating;        // null if not a rating question
    private String textAnswer;     // null if a rating question
}
