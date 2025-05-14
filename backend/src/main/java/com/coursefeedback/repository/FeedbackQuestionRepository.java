package com.coursefeedback.repository;

import com.coursefeedback.entity.FeedbackQuestion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedbackQuestionRepository extends JpaRepository<FeedbackQuestion, Long> {
}
