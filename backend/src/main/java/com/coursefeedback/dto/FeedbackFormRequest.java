package com.coursefeedback.dto;

import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FeedbackFormRequest {
    private String title;
    private String description;
    private List<FeedbackQuestionRequest> questions;
}
