package com.coursefeedback.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

import com.coursefeedback.entity.QuestionTarget;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FeedbackQuestionDTO {
    private Long id;
    private String questionText;
    private boolean isRatingQuestion;
    private QuestionTarget target; 
}

