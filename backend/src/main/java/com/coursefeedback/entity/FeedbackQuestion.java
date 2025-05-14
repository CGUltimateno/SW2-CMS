package com.coursefeedback.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name = "feedback_questions")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FeedbackQuestion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "question_text", nullable = false)
    private String questionText;
    
    // Use JsonProperty to ensure proper mapping with frontend
    @JsonProperty("isRatingQuestion") 
    @Column(name = "is_rating_question", nullable = false)
        private boolean ratingQuestion;

    @Enumerated(EnumType.STRING)
    @Column(name = "target")
    private QuestionTarget target;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "form_id")
    @JsonBackReference
    private FeedbackForm form;
        
    // Add this explicit getter to ensure proper serialization
    @JsonProperty("isRatingQuestion")
    public boolean isRatingQuestion() {
        return ratingQuestion;
    }

    
    public void setRatingQuestion(boolean ratingQuestion) {
        this.ratingQuestion = ratingQuestion;
    }
}