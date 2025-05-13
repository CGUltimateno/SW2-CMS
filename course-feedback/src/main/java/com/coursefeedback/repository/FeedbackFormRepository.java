package com.coursefeedback.repository;

import com.coursefeedback.entity.FeedbackForm;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedbackFormRepository extends JpaRepository<FeedbackForm, Long> {
}
