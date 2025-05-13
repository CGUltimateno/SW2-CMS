package com.coursefeedback.repository;
import com.coursefeedback.entity.FeedbackAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
public interface FeedbackAnswerRepository extends JpaRepository<FeedbackAnswer, Long> {
}

