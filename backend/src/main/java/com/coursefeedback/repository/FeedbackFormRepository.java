package com.coursefeedback.repository;

import com.coursefeedback.entity.Course;
import com.coursefeedback.entity.FeedbackForm;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedbackFormRepository extends JpaRepository<FeedbackForm, Long> {
    List<FeedbackForm> findByAssignedCoursesContaining(Course course);
}
