package com.coursefeedback.repository;

import com.coursefeedback.entity.Course;
import com.coursefeedback.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CourseRepository extends JpaRepository<Course, Long> {
    List<Course> findByInstructor(User instructor);
}
