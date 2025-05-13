package com.coursefeedback.mapper;

import com.coursefeedback.dto.*;
import com.coursefeedback.entity.Enrollment;

public class EnrollmentMapper {

    public static EnrollmentResponseDTO mapToDTO(Enrollment enrollment) {
        return EnrollmentResponseDTO.builder()
                .id(enrollment.getId())
                .enrolledAt(enrollment.getEnrolledAt())
                .student(StudentDTO.builder()
                        .id(enrollment.getStudent().getId())
                        .username(enrollment.getStudent().getUsername())
                        .email(enrollment.getStudent().getEmail())
                        .build())
                .course(CourseResponseDTO.builder()
                        .id(enrollment.getCourse().getId())
                        .name(enrollment.getCourse().getName())
                        .description(enrollment.getCourse().getDescription())
                        .instructor(InstructorDTO.builder()
                            .id(enrollment.getCourse().getInstructor().getId())
                              .username(enrollment.getCourse().getInstructor().getUsername())
                              .firstName(enrollment.getCourse().getInstructor().getFirstName())
                              .lastName(enrollment.getCourse().getInstructor().getLastName())
                        .build())
                     .build())
                .build();
              
    }
}
