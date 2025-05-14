package com.coursefeedback.mapper;

import com.coursefeedback.dto.CourseResponseDTO;
import com.coursefeedback.dto.InstructorDTO;
import com.coursefeedback.entity.Course;

public class coursemapper {

    public static CourseResponseDTO mapToDTO(Course course) {
        if (course == null || course.getInstructor() == null) {
            throw new IllegalArgumentException("Course or instructor is null");
        }

        InstructorDTO instructor = InstructorDTO.builder()
            .id(course.getInstructor().getId())
            .username(course.getInstructor().getUsername())
            .firstName(course.getInstructor().getFirstName())
            .lastName(course.getInstructor().getLastName())
            .build();

        return CourseResponseDTO.builder()
            .id(course.getId())
            .name(course.getName())
            .description(course.getDescription())
            .instructor(instructor)
            .build();
    }
}
