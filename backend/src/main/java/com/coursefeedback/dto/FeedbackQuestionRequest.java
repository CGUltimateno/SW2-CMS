package com.coursefeedback.dto;

import com.coursefeedback.entity.QuestionTarget;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class FeedbackQuestionRequest {
    private String questionText;
    
    // Accept BOTH property names from the frontend
    @JsonProperty(value = "ratingQuestion") 
    private boolean ratingQuestion;
    
    private String target;
    
    // Custom getter for compatibility
    public boolean isRatingQuestion() {
        return ratingQuestion;
    }
    
    // Additional setter that allows the frontend to use isRatingQuestion
    @JsonProperty("isRatingQuestion")
    public void setIsRatingQuestion(boolean isRating) {
        this.ratingQuestion = isRating;
        System.out.println("JSON property 'isRatingQuestion' set to: " + isRating);
    }
}