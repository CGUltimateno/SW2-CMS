package com.coursefeedback.service;

import com.coursefeedback.dto.CourseResponseDTO;
import com.coursefeedback.dto.CourseWithFeedbackDTO;  // Add this import
import com.coursefeedback.entity.Course;
import com.coursefeedback.entity.User;

import java.util.List;

public interface ICourseService {
    CourseResponseDTO createCourse(Course course, String instructorUsername);
    List<CourseResponseDTO> getCoursesByInstructor(String username);
    List<CourseResponseDTO> getAllCourses();
    CourseResponseDTO getCourseById(Long id);
    
    // Add this new method to the interface
    List<CourseWithFeedbackDTO> getEnrolledCoursesWithFeedbackInfo(String username);
}